package com.cusbee.kiosk.entity;

import com.cusbee.kiosk.config.ApplicationContext;
import com.cusbee.kiosk.controller.api.customer.payment.CreditCard;
import com.cusbee.kiosk.controller.api.customer.payment.Encryption;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity
@Table(name = "credit_card")
public class CreditCards extends AbstractEntity implements CreditCard, Referenced {

    protected CreditCards() {
        //do not allow direct instantiation -- must at least be package private for bytecode instrumentation
        //this complies with JPA specification requirements for entity construction
    }

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Transient
    private Encryption encryption;

    @Column(name = "pan", nullable = false, unique = true)
    private String pan;

    @Column(name = "expiration_month", nullable = false)
    private Integer expirationMonth;

    @Column(name = "expiration_year", nullable = false)
    private Integer expirationYear;

    @Column(name = "name_on_card", nullable = false)
    private String nameOnCard;

    @Transient
    private String cvvCode;

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getPan() {
        if (encryption == null) {
            encryption = ApplicationContext.getContext().map(ctx -> ctx.getBean(Encryption.class)).orElse(new Encryption());
        }
        return encryption.decrypt(pan);
    }

    @Override
    public void setPan(String pan) {
        if (encryption == null) {
            encryption = ApplicationContext.getContext().map(ctx -> ctx.getBean(Encryption.class)).orElse(new Encryption());
        }
        this.pan = encryption.encrypt(pan);
    }

    @Override
    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    @Override
    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    @Override
    public Integer getExpirationYear() {
        return expirationYear;
    }

    @Override
    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }

    @Override
    public String getNameOnCard() {
        return nameOnCard;
    }

    @Override
    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    @Override
    public String getCvvCode() {
        return cvvCode;
    }

    @Override
    public void setCvvCode(String cvvCode) {
        this.cvvCode = cvvCode;
    }

    @Override
    public Encryption getEncryption() {
        return encryption;
    }

    @Override
    public void setEncryption(Encryption encryptionModule) {
        this.encryption = encryptionModule;
    }

    public static final class Builder {

        private Long userId;
        private Long id;
        private Encryption encryption;
        private String pan;
        private String modifiedBy;
        private Integer expirationMonth;
        private Integer expirationYear;
        private String nameOnCard;
        private Date modifiedDate;
        private String cvvCode;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder encryption(Encryption encryption) {
            this.encryption = encryption;
            return this;
        }

        public Builder pan(String pan) {
            this.pan = pan;
            return this;
        }

        public Builder modifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
            return this;
        }

        public Builder expirationMonth(Integer expirationMonth) {
            this.expirationMonth = expirationMonth;
            return this;
        }

        public Builder expirationYear(Integer expirationYear) {
            this.expirationYear = expirationYear;
            return this;
        }

        public Builder nameOnCard(String nameOnCard) {
            this.nameOnCard = nameOnCard;
            return this;
        }

        public Builder modifiedDate(Date modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public Builder cvvCode(String cvvCode) {
            this.cvvCode = cvvCode;
            return this;
        }

        public CreditCards build() {
            CreditCards creditCards = new CreditCards();
            creditCards.setUserId(userId);
            creditCards.setId(id);
            creditCards.setEncryption(encryption);
            if (StringUtils.isNotBlank(pan)) {
                creditCards.setPan(pan);
            }
            creditCards.setModifiedBy(modifiedBy);
            creditCards.setExpirationMonth(expirationMonth);
            creditCards.setExpirationYear(expirationYear);
            creditCards.setNameOnCard(nameOnCard);
            creditCards.setModifiedDate(modifiedDate);
            creditCards.setCvvCode(cvvCode);
            return creditCards;
        }
    }
}
