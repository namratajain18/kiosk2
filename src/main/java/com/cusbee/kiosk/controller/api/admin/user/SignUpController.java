package com.cusbee.kiosk.controller.api.admin.user;

import com.cusbee.kiosk.bean.UserBean;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.entity.Users;
import com.cusbee.kiosk.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by ahorbat on 04.02.17.
 */
@Controller
@RequestMapping(value = "/api")
public class SignUpController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TokenStore tokenStore;

    //todo extract logic to separae method with adding user from admin page
    @RequestMapping(value = "/customer/signup", method = RequestMethod.POST)
    public ResponseContainer<UserBean> createUser(@RequestBody Users user) {
        ResponseContainer<UserBean> response = new ResponseContainer<>();
        Users existingUser = usersRepository.findByUsername(user.getUsername());
        if(existingUser == null) {
            usersRepository.save(user);
            response.setData(UserBean.toCustomerBean(user));
            response.setCode(HttpStatus.OK.value());
            response.setMessage("User successfully created");
            return response;
        } else {
            response.setData(UserBean.toCustomerBean(existingUser));
            response.setCode(HttpStatus.ALREADY_REPORTED.value());
            response.setMessage("User with such email already exist");
            return response;
        }
    }

    //todo Discuss with UI dev what to return
    @RequestMapping(value = "/customer/logout", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void logout(OAuth2Authentication authentication) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(tokenStore.getAccessToken(authentication).getValue()));
    }

}
