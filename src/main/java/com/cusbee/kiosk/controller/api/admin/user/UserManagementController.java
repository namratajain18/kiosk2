package com.cusbee.kiosk.controller.api.admin.user;

import com.cusbee.kiosk.bean.UserBean;
import com.cusbee.kiosk.controller.PageContainer;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.entity.Users;
import com.cusbee.kiosk.repository.UsersRepository;
import com.twilio.sdk.TwilioRestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahorbat on 02.02.17.
 */
@RestController
@RequestMapping(value = "/api")
public class UserManagementController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TokenStore tokenStore;

    private static final String ACCOUNT_SID = "ACada01deec6a500eac96aac363919a00b";
    private static final String AUTH_TOKEN = "6b93f20406bd056453fd9d753c56ef01";


    //todo: to be removed to propriet place after client confirmation with TWILIO
    @RequestMapping(value = "/admin/test", method = RequestMethod.GET)
    public void test () {
        try {
            TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

            // Build a filter for the MessageList
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Body", "Hello, World!"));
            params.add(new BasicNameValuePair("To", "+380990469063")); //Add real number here
            params.add(new BasicNameValuePair("From", "+19168502935"));

            MessageFactory messageFactory = client.getAccount().getMessageFactory();
            Message message = messageFactory.create(params);
            System.out.println(message.getSid());
        }
        catch (TwilioRestException e) {
            System.out.println(e.getErrorMessage());
        }
    }

    //todo: add parameters with paging
    // 0 - current page 10 - elements per page
    @RequestMapping(value = "/admin/all", method = RequestMethod.GET)
    public ResponseContainer<List<UserBean>> listAllUsers(@RequestParam(value = "perPage", required = true) int perPage,
                                                          @RequestParam(value = "page",  required = true) int page) {
        ResponseContainer<List<UserBean>> response = new ResponseContainer<>();
        Page<Users> pagableUsers = usersRepository.findAll(new PageRequest(page, perPage));
        response.setData(UserBean.toBeanList(pagableUsers.getContent()));
        response.setCode(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.name());
        response.setPageContainer(new PageContainer(pagableUsers.getTotalElements(),pagableUsers.getTotalPages()));
        return response;
    }

    //todo: Move messages to property file
    @RequestMapping(value = "/admin/new", method = RequestMethod.POST)
    public ResponseContainer<UserBean> createUser(@RequestBody Users user) {
        ResponseContainer<UserBean> response = new ResponseContainer<>();
        Users existingUser = usersRepository.findByUsername(user.getUsername());
        if(existingUser == null) {
            usersRepository.save(user);
            response.setData(UserBean.toBean(user));
            response.setCode(HttpStatus.OK.value());
            response.setMessage("User successfully created");
            return response;
        } else {
            response.setData(UserBean.toBean(existingUser));
            response.setCode(HttpStatus.ALREADY_REPORTED.value());
            response.setMessage("User with such email already exist");
            return response;
        }
    }

    @RequestMapping(value = "/admin/{id}", method = RequestMethod.PUT)
    public ResponseContainer<UserBean> updateUser(@PathVariable("id") long id, @RequestBody Users user) {
        Users existingUser = usersRepository.findById(id);
        ResponseContainer<UserBean> response = new ResponseContainer<>();
        if (existingUser != null) {
            existingUser.setPassword(user.getPassword());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setRole(user.getRole());
            existingUser.setBan(user.getBan());
            existingUser.setUsername(user.getUsername());
            usersRepository.save(existingUser);
            response.setData(UserBean.toBean(existingUser));
            response.setMessage("User updated successfully");
            response.setCode(HttpStatus.OK.value());
            return response;
        } else {
            response.setMessage("User is not found in the system");
            response.setCode(HttpStatus.NO_CONTENT.value());
            return response;
        }
    }

    @RequestMapping(value = "/admin/{id}", method = RequestMethod.DELETE)
    public ResponseContainer<String> deleteUser(@PathVariable("id") Long id){
        ResponseContainer<String> response = new ResponseContainer<>();
        Users existingUser = usersRepository.findById(id);
        if(existingUser != null) {
            usersRepository.delete(existingUser);
            response.setMessage("User removed successfully");
            response.setCode(HttpStatus.OK.value());
            return response;
        } else {
            response.setMessage("User is not found in the system");
            response.setCode(HttpStatus.NO_CONTENT.value());
            return response;
        }

    }

    //todo Discuss with UI dev what to return
    @RequestMapping(value = "/admin/logout", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void logout(OAuth2Authentication authentication) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(tokenStore.getAccessToken(authentication).getValue()));
    }
}
