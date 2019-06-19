package com.gdibernardo.emailservice.email.rest.model;

public class ResponseMessage {

    private String message;
    private int status;

    public ResponseMessage(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
