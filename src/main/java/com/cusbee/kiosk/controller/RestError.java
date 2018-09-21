package com.cusbee.kiosk.controller;

/**
 * Created by Alex Horbatiuk on 01.02.2017.
 */

public class RestError {
    private int appErrorCode;
    private String appErrorMessage;

    public RestError(int appErrorCode, String appErrorMessage) {
        super();
        this.appErrorCode = appErrorCode;
        this.appErrorMessage = appErrorMessage;
    }

    public int getAppErrorCode() {
        return appErrorCode;
    }

    public void setAppErrorCode(int appErrorCode) {
        this.appErrorCode = appErrorCode;
    }

    public String getAppErrorMessage() {
        return appErrorMessage;
    }

    public void setAppErrorMessage(String appErrorMessage) {
        this.appErrorMessage = appErrorMessage;
    }

}