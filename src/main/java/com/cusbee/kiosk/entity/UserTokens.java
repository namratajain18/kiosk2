package com.cusbee.kiosk.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author mxmind
 * @version 1.0.0.Alpha1
 * @since 1.0.0.Alpha1
 */
@Entity
@Table(name = "user_tokens")
public class UserTokens extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -1228652106688673018L;
    public static final int MAX_LIVE_TIME = 15;

    public enum TokenStatus {
        VALID, INVALID, EXPIRED
    }

    @Column(name = "passcode")
    private String passcode;

    @Column(nullable = false, columnDefinition = "int default 0")
    private TokenStatus tokenStatus = TokenStatus.VALID;

    @Column(name = "creation_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date creationTime;

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public TokenStatus getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(TokenStatus tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public static final class Builder {
        private Users user;
        private String passcode;
        private Long id;
        private TokenStatus tokenStatus = TokenStatus.VALID;
        private String modifiedBy;
        private Date creationTime;
        private Date modifiedDate;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder user(Users user) {
            this.user = user;
            return this;
        }

        public Builder passcode(String passcode) {
            this.passcode = passcode;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder tokenStatus(TokenStatus tokenStatus) {
            this.tokenStatus = tokenStatus;
            return this;
        }

        public Builder modifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
            return this;
        }

        public Builder creationTime(Date creationTime) {
            this.creationTime = creationTime;
            return this;
        }

        public Builder modifiedDate(Date modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public UserTokens build() {
            UserTokens userTokens = new UserTokens();

            userTokens.setPasscode(passcode);
            userTokens.setId(id);
            userTokens.setTokenStatus(tokenStatus);
            userTokens.setModifiedBy(modifiedBy);
            userTokens.setCreationTime(creationTime);
            userTokens.setModifiedDate(modifiedDate);
            return userTokens;
        }
    }
}
