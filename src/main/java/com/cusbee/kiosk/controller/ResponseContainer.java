package com.cusbee.kiosk.controller;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.text.MessageFormat;

/**
 * Created by Alex Horbatiuk on 01.02.2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseContainer<T> {

    private T data = null;
    private int code = 0;
    private String message = null;
    private PageContainer pageContainer;

    public T getData() {
        return data;
    }

    public PageContainer getPageContainer() {
        return pageContainer;
    }

    public void setPageContainer(PageContainer pageContainer) {
        this.pageContainer = pageContainer;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(RestError error) {
        code = error.getAppErrorCode();
        message = error.getAppErrorMessage();
    }

    public static final class Builder<T> {

        private T data = null;
        private int code = 0;
        private String message = null;
        private PageContainer pageContainer;

        private Builder() {
        }

        public static <U> Builder<U> builder() {
            return new Builder<>();
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> code(int code) {
            this.code = code;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> message(String message, Object ...varargs) {
            this.message = MessageFormat.format(message, varargs);
            return this;
        }

        public Builder<T> pageContainer(PageContainer pageContainer) {
            this.pageContainer = pageContainer;
            return this;
        }

        public ResponseContainer<T> build() {
            ResponseContainer<T> responseContainer = new ResponseContainer<>();
            responseContainer.setData(data);
            responseContainer.setCode(code);
            responseContainer.setMessage(message);
            responseContainer.setPageContainer(pageContainer);

            return responseContainer;
        }
    }
}

