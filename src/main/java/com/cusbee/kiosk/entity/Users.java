package com.cusbee.kiosk.entity;

import com.cusbee.kiosk.enums.Ban;
import com.cusbee.kiosk.enums.Role;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ahorbat on 02.02.17.
 */
@Entity
@Table(name = "users")
public class Users extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 8926949528400972392L;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "phone")
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "enabled", nullable = false)
    private Ban ban = Ban.ENABLED;

    @Column(name = "ROLE", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "token_id")
    private UserTokens token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Ban getBan() {
        return ban;
    }

    public void setBan(Ban ban) {
        this.ban = ban;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserTokens getToken() {
        return token;
    }

    public void setToken(UserTokens token) {
        this.token = token;
    }

    public static final class Builder {
        private String username;
        private String phoneNumber;
        private String password;
        private String fullName;
        private Long id;
        private Ban ban = Ban.ENABLED;
        private Role role;
        private UserTokens token;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder ban(Ban ban) {
            this.ban = ban;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder token(UserTokens token) {
            this.token = token;
            return this;
        }

        public Users build() {
            Users users = new Users();
            users.setUsername(username);
            users.setPhoneNumber(phoneNumber);
            users.setPassword(password);
            users.setId(id);
            users.setBan(ban);
            users.setRole(role);
            users.token = this.token;
            users.fullName = this.fullName;
            return users;
        }
    }
}
