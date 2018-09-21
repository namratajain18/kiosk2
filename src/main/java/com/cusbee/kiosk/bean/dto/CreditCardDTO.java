package com.cusbee.kiosk.bean.dto;

import com.cusbee.kiosk.controller.api.customer.payment.CreditCardType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
@Dto
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(
    value = "CreditCardDTO",
    description = "The data transfer object designed to hold and transfer visible information about Credit Card of User"
)
public class CreditCardDTO<T> {

    @ApiModelProperty(value = "The parent transaction object. Not used", readOnly = true, position = 1000)
    protected T parent;

    @ApiModelProperty(value = "The map of additional details related to user and credit card relation. Not used", readOnly = true)
    protected Map<String, Object> additionalFields;

    @ApiModelProperty(
        value = "The first and last name of card holder. Upper-cased. The names should be placed in the same order as on card.",
        required = true,
        readOnly = true
    )
    protected String creditCardHolderName;

    @ApiModelProperty(
        value = "The type of credit card",
        required = true,
        readOnly = true,
        dataType = "CreditCardType",
        allowableValues = "MASTERCARD,VISA,AMEX,DINERSCLUB_CARTEBLANCHE,DISCOVER,ENROUTE,JCB"
    )
    protected String creditCardType;

    @ApiModelProperty(
        value = "The number of credit card, It is required only, while credit card is being saved",
        notes = "Should contains only digits",
        required = true
    )
    protected String creditCardNumber;

    @ApiModelProperty(
        value = "The last four symbols of credit card's number are visible, the rest are replaced on asterisks",
        example = "**** **** **** 4237",
        required = true,
        readOnly = true
    )
    protected String creditCardLastFour;

    @ApiModelProperty(
        value = "The expired date of credit card.",
        example = "11/12",
        readOnly = true
    )
    protected String creditCardExpDate;

    @ApiModelProperty(
        value = "The month of credit card's expiration.",
        notes = "Should contains two digits. Zero-padded.",
        example = "06",
        required = true,
        readOnly = true
    )
    protected String creditCardExpMonth;

    @ApiModelProperty(
        value = "The year of credit card's expiration.",
        notes = "Should contains two digits. Zero-padded.",
        example = "17",
        required = true,
        readOnly = true
    )
    protected String creditCardExpYear;

    @ApiModelProperty(
        value = "The CVV of credit card. Required only, while credit card is being saved",
        notes = "Never stored in DB",
        required = true,
        readOnly = true
    )
    protected String creditCardCvv;

    @ApiModelProperty(value = "The DB identifier of saved credit card", readOnly = true, position = -1)
    protected Long creditCardId;

    public CreditCardDTO() {
        this.additionalFields = new HashMap<>();
    }

    public CreditCardDTO(T parent) {
        this.additionalFields = new HashMap<>();
        this.parent = parent;
    }

    public T done() {
        return parent;
    }

    public CreditCardDTO<T> additionalFields(String key, Object value) {
        additionalFields.put(key, value);
        return this;
    }

    public CreditCardDTO<T> creditCardHolderName(String creditCardHolderName) {
        this.creditCardHolderName = creditCardHolderName;
        return this;
    }

    public CreditCardDTO<T> creditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
        return this;
    }

    public CreditCardDTO<T> creditCardNum(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
        return this;
    }

    public CreditCardDTO<T> creditCardLastFour(String creditCardLastFour) {
        this.creditCardLastFour = creditCardLastFour;
        return this;
    }

    public CreditCardDTO<T> creditCardExpDate(String creditCardExpDate) {
        this.creditCardExpDate = creditCardExpDate;
        return this;
    }

    public CreditCardDTO<T> creditCardExpMonth(String creditCardExpMonth) {
        this.creditCardExpMonth = creditCardExpMonth;
        return this;
    }

    public CreditCardDTO<T> creditCardExpYear(String creditCardExpYear) {
        this.creditCardExpYear = creditCardExpYear;
        return this;
    }

    public CreditCardDTO<T> creditCardCvv(String creditCardCvv) {
        this.creditCardCvv = creditCardCvv;
        return this;
    }


    public Map<String, Object> getAdditionalFields() {
        return additionalFields;
    }

    public String getCreditCardHolderName() {
        return creditCardHolderName;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getCreditCardLastFour() {
        return creditCardLastFour;
    }

    public String getCreditCardExpDate() {
        return creditCardExpDate;
    }

    public String getCreditCardExpMonth() {
        return creditCardExpMonth;
    }

    public String getCreditCardExpYear() {
        return creditCardExpYear;
    }

    public String getCreditCardCvv() {
        return creditCardCvv;
    }

    public Long getCreditCardId() {
        return creditCardId;
    }

    public static final class Builder<P> {
        protected P parent;
        protected Map<String, Object> additionalFields;
        protected String creditCardHolderName;
        protected String creditCardType;
        protected String creditCardNumber;
        protected String creditCardLastFour;
        protected String creditCardExpDate;
        protected String creditCardExpMonth;
        protected String creditCardExpYear;
        protected String creditCardCvv;
        protected Long creditCardId;

        private Builder() {
        }

        public static <U> Builder<U> builder() {
            return new Builder<>();
        }

        public Builder parent(P parent) {
            this.parent = parent;
            return this;
        }

        public Builder additionalFields(Map<String, Object> additionalFields) {
            this.additionalFields = additionalFields;
            return this;
        }

        public Builder creditCardHolderName(String creditCardHolderName) {
            this.creditCardHolderName = creditCardHolderName;
            return this;
        }

        public Builder creditCardType(String creditCardType) {
            this.creditCardType = creditCardType;
            return this;
        }

        public Builder creditCardNumber(String creditCardNumber) {
            this.creditCardNumber = creditCardNumber;
            return this;
        }

        public Builder creditCardLastFour(String creditCardLastFour) {
            this.creditCardLastFour = creditCardLastFour;
            return this;
        }

        public Builder creditCardExpDate(String creditCardExpDate) {
            this.creditCardExpDate = creditCardExpDate;
            return this;
        }

        public Builder creditCardExpMonth(String creditCardExpMonth) {
            this.creditCardExpMonth = creditCardExpMonth;
            return this;
        }

        public Builder creditCardExpYear(String creditCardExpYear) {
            this.creditCardExpYear = creditCardExpYear;
            return this;
        }

        public Builder creditCardCvv(String creditCardCvv) {
            this.creditCardCvv = creditCardCvv;
            return this;
        }

        public Builder creditCardId(Long creditCardId) {
            this.creditCardId = creditCardId;
            return this;
        }

        public CreditCardDTO build() {
            CreditCardDTO creditCardDTO = new CreditCardDTO(parent);
            creditCardDTO.creditCardExpDate = this.creditCardExpDate;
            creditCardDTO.creditCardType = this.creditCardType;
            creditCardDTO.creditCardNumber = this.creditCardNumber;
            creditCardDTO.creditCardCvv = this.creditCardCvv;
            creditCardDTO.additionalFields = this.additionalFields;
            creditCardDTO.creditCardExpYear = this.creditCardExpYear;
            creditCardDTO.creditCardLastFour = this.creditCardLastFour;
            creditCardDTO.creditCardExpMonth = this.creditCardExpMonth;
            creditCardDTO.creditCardHolderName = this.creditCardHolderName;
            creditCardDTO.creditCardId = this.creditCardId;
            return creditCardDTO;
        }
    }
}
