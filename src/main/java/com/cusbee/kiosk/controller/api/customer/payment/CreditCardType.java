package com.cusbee.kiosk.controller.api.customer.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
@ApiModel(
    value = "CreditCardType",
    description = "The types of registered credit cards"
)
public enum CreditCardType implements Serializable {

    MASTERCARD("MASTERCARD", "Master Card"),
    VISA("VISA", "Visa"),
    AMEX("AMEX", "American Express"),
    DINERSCLUB_CARTEBLANCHE("DINERSCLUB_CARTEBLANCHE", "Diner's Club / Carte Blanche"),
    DISCOVER("DISCOVER", "Discover"),
    ENROUTE("ENROUTE", "En Route"),
    JCB("JCB", "JCB");

    @ApiModelProperty(value = "The official type of credit card", readOnly = true, position = 1)
    private final String type;

    @ApiModelProperty(value = "The human-readable name of credit card's type", readOnly = true, position = 2)
    private final String friendlyType;

    CreditCardType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getFriendlyType() {
        return friendlyType;
    }
}
