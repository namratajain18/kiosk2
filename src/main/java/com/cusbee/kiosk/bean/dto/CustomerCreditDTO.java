package com.cusbee.kiosk.bean.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
public class CustomerCreditDTO<T> {
    protected T parent;

    protected Map<String, Object> additionalFields;
    protected String customerCreditAccountNum;
    protected String customerCreditAccountMasked;

    public CustomerCreditDTO() {
        this.additionalFields = new HashMap<>();
    }

    public CustomerCreditDTO(T parent) {
        this.additionalFields = new HashMap<>();
        this.parent = parent;
    }

    public T done() {
        return parent;
    }

    public CustomerCreditDTO<T> additionalFields(String key, Object value) {
        additionalFields.put(key, value);
        return this;
    }

    public CustomerCreditDTO<T> customerCreditAccountNum(String customerCreditAccountNum) {
        this.customerCreditAccountNum = customerCreditAccountNum;
        return this;
    }

    public CustomerCreditDTO<T> customerCreditAccountMasked(String customerCreditAccountMasked) {
        this.customerCreditAccountMasked = customerCreditAccountMasked;
        return this;
    }
}
