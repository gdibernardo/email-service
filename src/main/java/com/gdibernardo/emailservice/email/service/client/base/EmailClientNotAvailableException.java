package com.gdibernardo.emailservice.email.service.client.base;

public class EmailClientNotAvailableException extends RuntimeException {
    public EmailClientNotAvailableException(String message) {
        super(message);
    }
}