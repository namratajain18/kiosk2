package com.cusbee.kiosk.bean;

import com.cusbee.kiosk.entity.Users;
import com.cusbee.kiosk.enums.Ban;
import com.cusbee.kiosk.enums.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahorbat on 03.02.17.
 */
public class UserBean {

    private Long id;
    private String username;
    private String role;
    private String ban;
    private String phoneNumber;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }

    public String getPhoneNumber() {return phoneNumber;}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public static UserBean toBean(Users user){
        UserBean userBean = new UserBean();
        userBean.setId(user.getId());
        userBean.setUsername(user.getUsername());
        userBean.setPhoneNumber(user.getPhoneNumber());
        userBean.setRole(user.getRole().getDescription());
        userBean.setBan(user.getBan().getDescription());

     return userBean;
    }

    public static UserBean toCustomerBean(Users user){
        UserBean userBean = new UserBean();
        userBean.setId(userBean.getId());
        userBean.setUsername(user.getUsername());
        userBean.setPhoneNumber(user.getPhoneNumber());
        userBean.setRole(Role.ROLE_CUSTOMER.getDescription());
        userBean.setBan(Ban.ENABLED.getDescription());

        return userBean;
    }

    public static List<UserBean> toBeanList (List<Users> usersList ) {
        List<UserBean> userBeanList = new ArrayList<>();
        for (Users users : usersList) {
            userBeanList.add(UserBean.toBean(users));
        }
        return userBeanList;
    }
}
