package com.cusbee.kiosk.entity;

import com.cusbee.kiosk.enums.PaymentStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ahorbat on 29.05.17.
 */
@Entity
@Table(name = "payments")
public class Payments extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -4256263134671715859L;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id")
    private CreditCards card;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date date = new Date();

    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.NOT_PAYED;

    @Column(name = "comment")
    private String comment;

    @Column(name = "refund")
    private String refund;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public CreditCards getCard() {
        return card;
    }

    public void setCard(CreditCards card) {
        this.card = card;
        if (this.card.getEncryption() != null) {
            cardNumber = card.getPan();
        }
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public static final class Builder {
        private String name;
        private CreditCards card;
        private String cardNumber;
        private Integer amount;
        private Long id;
        private Date date = new Date();
        private PaymentStatus status = PaymentStatus.NOT_PAYED;
        private String comment;
        private String refund;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder card(CreditCards card) {
            this.card = card;
            return this;
        }

        public Builder cardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public Builder amount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder status(PaymentStatus status) {
            this.status = status;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder refund(String refund) {
            this.refund = refund;
            return this;
        }

        public Payments build() {
            Payments payments = new Payments();
            payments.setName(StringUtils.defaultString(name));
            if (card != null) {
                payments.setCard(card);
            }
            if (cardNumber != null) {
                payments.setCardNumber(cardNumber);
            }
            payments.setAmount(amount);
            payments.setId(id);
            if (date != null) {
                payments.setDate(date);
            }
            if (status != null) {
                payments.setStatus(status);
            }
            payments.setComment(comment);
            payments.setRefund(refund);
            return payments;
        }
    }
}
