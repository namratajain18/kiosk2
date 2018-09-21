package com.cusbee.kiosk.controller.api.customer.payment;

import com.cusbee.kiosk.bean.dto.CreditCardDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class CreditCardValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return clazz.equals(CreditCardDTO.class);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardType", "Credit Card Name Is Required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardNumber", "Credit Card Number Is Required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardExpMonth", "Credit Card Exp Month Is Required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardExpYear", "Credit Card Exp Year Is Required");
        rejectIfTypeIsNotMatch(errors, "creditCardType", "Credit Card Type Do Not Match Credit Card Number");
    }

    private static void rejectIfTypeIsNotMatch(
        Errors errors, String field, String errorCode) {

        Assert.notNull(errors, "Errors object must not be null");
        Object value = errors.getFieldValue(field);
        if (value == null || !StringUtils.hasText(value.toString())) {
            errors.rejectValue(field, errorCode);
        } else {
            CreditCardType creditCardType = CreditCardType.valueOf(value.toString());
            if (CreditCardTypeCheck.getCreditCardType(errors.getFieldValue("creditCardNumber").toString()) != creditCardType) {
                errors.rejectValue(field, errorCode);
            }
        }
    }
}
