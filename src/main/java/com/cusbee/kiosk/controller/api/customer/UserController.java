package com.cusbee.kiosk.controller.api.customer;

import com.cusbee.kiosk.bean.UserBean;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.repository.UsersRepository;
import com.google.common.base.Throwables;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
@Api("Customer API")
@RestController
@RequestMapping(value = "/api/customer")
public class UserController {

    @Autowired
    private UsersRepository usersRepository;

    @ApiOperation(
        value = "Returns current user profile",
        notes = "In case if user is authenticated and his session is still live returns profile of latter, " +
            "otherwise the empty response will be returned.",
        response = UserBean.class
    )
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ResponseContainer<UserBean> getCurrentUserProfile() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return ResponseContainer.Builder.<UserBean>builder()
                .code(200)
                .data(UserBean.toBean(usersRepository.findByUsername(user.getUsername())))
                .build();
        } catch (Exception ex) {
            return ResponseContainer.Builder.<UserBean>builder()
                .code(500)
                .message(Throwables.getRootCause(ex).getMessage())
                .build();
        }
    }
}
