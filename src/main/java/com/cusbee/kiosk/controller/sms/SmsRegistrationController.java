package com.cusbee.kiosk.controller.sms;

import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.entity.UserTokens;
import com.cusbee.kiosk.entity.Users;
import com.cusbee.kiosk.enums.Role;
import com.cusbee.kiosk.repository.UserTokensRepository;
import com.cusbee.kiosk.repository.UsersRepository;
import com.google.common.base.Throwables;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * @author vzdomishchuk
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/mobile/")
public class SmsRegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsRegistrationController.class);

    private static final String ACCOUNT_SID = "AC15460338c5deac47051b5bc5336f0982";
    private static final String AUTH_TOKEN = "063670cb8a2a711693e40b43783838c8";
    private static final String ONLY_DIGITS = "[^\\d]+";

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserTokensRepository tokensRepository;

    @RequestMapping(
        value = "/sms/passcode",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseContainer<Void> passcode(@RequestParam(value = "userphone") String userphone) {
        int code = 200;
        String message;
        Users user;

        String phoneNumber = userphone.replaceAll(ONLY_DIGITS, StringUtils.EMPTY);

        try {
            String passcode;
            message = String.format("The existing user is returned for phone number: %s", phoneNumber);
            user = usersRepository.findByPhoneNumber(phoneNumber);
            if (user == null) {
                message = String.format("The new user is created for phone number: %s", phoneNumber);

                passcode = generateNewPasscode();
                UserTokens token = UserTokens.Builder.builder()
                    .creationTime(Date.from(Instant.now()))
                    .passcode(passcode)
                    .tokenStatus(UserTokens.TokenStatus.VALID)
                    .build();

                user = Users.Builder.builder()
                    .phoneNumber(phoneNumber)
                    .username(phoneNumber)
                    .password(passcode)
                    .role(Role.ROLE_ADMIN) // temporary removed, ROLE_CUSTOMER
                    .token(tokensRepository.save(token))
                    .build();

                user = usersRepository.save(user);
                LOGGER.debug("The first passcode: {} was generated for  new user: {}", passcode, user.getId());
            } else {
                UserTokens token = user.getToken();
                if (token != null && getTokenLiveDuration(token) <= UserTokens.MAX_LIVE_TIME) {
                    passcode = token.getPasscode();
                } else {
                    if (token == null) {
                        token = UserTokens.Builder.builder()
                            .creationTime(Date.from(Instant.now()))
                            .passcode(generateNewPasscode())
                            .tokenStatus(UserTokens.TokenStatus.VALID)
                            .build();

                        user.setToken(tokensRepository.save(token));
                    } else {
                        token.setPasscode(generateNewPasscode());
                        token.setCreationTime(Date.from(Instant.now()));
                        user.setToken(tokensRepository.save(token));
                    }

                    passcode = token.getPasscode();
                    user = usersRepository.save(user);

                    LOGGER.debug("The new passcode: {} was generated for user: {}", token.getPasscode(), user.getId());
                }
            }
            authenticate(user).sendSms(String.format("Your code no: %s", passcode), phoneNumber);
        } catch (Exception ex) {
            code = 500;
            message = Throwables.getRootCause(ex).getMessage();
        }

        return ResponseContainer.Builder.<Void>builder()
            .message(message)
            .code(code)
            .build();
    }

    @RequestMapping(
        value = "/oauth/passcode",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Object authenticateByPasscode(
        @RequestParam(value = "userphone") String userphone,
        @RequestParam(value = "passcode") String passcode,
        HttpServletRequest request
    ) {
        String message;
        int code = 500;
        try {
            userphone = Objects.requireNonNull(userphone, "The user phone should not be empty");
            passcode = Objects.requireNonNull(passcode, "The passcode should not be empty");

            Users user = usersRepository.findByPhoneNumber(userphone.replaceAll(ONLY_DIGITS, StringUtils.EMPTY));
            if (user != null) {
                UserTokens token = user.getToken();
                if (token != null && token.getPasscode().equals(passcode)) {
                    if (getTokenLiveDuration(token) <= UserTokens.MAX_LIVE_TIME) {
                        return authenticate(user).redirect(user.getPhoneNumber(), request);
                    } else {
                        message = String.format("The passcode: %s is expired", token.getPasscode());
                        code = 410;
                    }
                } else {
                    message = String.format("The token is stalled, passcode: %s, userphone: %s", passcode, userphone);
                    code = 401;
                    LOGGER.error(message);
                }
            } else {
                message = "Unable to find user";
                code = 404;
                LOGGER.error(message);
            }
        } catch (Exception ex) {
            message = Throwables.getRootCause(ex).getMessage();
            LOGGER.error(message, ex);
        }
        return ResponseContainer.Builder.<Void>builder()
            .code(code)
            .message(message)
            .build();
    }

    private SmsRegistrationController authenticate(Users user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user.getUsername(),
            user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return this;
    }

    private ResponseEntity<Object> redirect(String userphone, HttpServletRequest request) {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userphone", userphone);
        map.add("grand_token", "password");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        String redirection = String.format(
            "%s://%s:%s/oauth/token",
            request.getScheme(), request.getLocalName(), request.getLocalPort()
        );
        return template.postForEntity(redirection, entity, Object.class);
    }

    private String generateNewPasscode() {
        int maxAttempts = 3;
        int attempt = 0;

        String passcode;
        do {
            passcode = RandomStringUtils.randomNumeric(8);
            attempt++;
        } while (tokensRepository.findByPasscode(passcode) != null && attempt < maxAttempts);
        return passcode;
    }

    private void sendSms(String text, String number) {
        try {
            TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

            // Build a filter for the MessageList
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("To", String.format("+%s", number)));
            params.add(new BasicNameValuePair("Body", text));
            params.add(new BasicNameValuePair("From", "+19177463308"));

            MessageFactory messageFactory = client.getAccount().getMessageFactory();
            Message message = messageFactory.create(params);
            LOGGER.debug("The sms is sent successfully, message SID: {}", message.getSid());
        } catch (TwilioRestException ex) {
            LOGGER.error("The exception is raised during the sms sending", ex);
        }
    }

    private long getTokenLiveDuration(UserTokens token) {
        return Duration.ofMillis(System.currentTimeMillis() - token.getCreationTime().getTime()).toMinutes();
    }
}
