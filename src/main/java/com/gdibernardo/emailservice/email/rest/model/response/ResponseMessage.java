package com.gdibernardo.emailservice.email.rest.model.response;

public class ResponseMessage {

    private String message;
    private int httpStatusCode;

    public ResponseMessage(String message, int httpStatusCode) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getMessage() {
        return message;
    }
}
