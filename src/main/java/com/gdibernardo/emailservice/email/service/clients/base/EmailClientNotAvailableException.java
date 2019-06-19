package com.gdibernardo.emailservice.email.service.clients.base;

public class EmailClientNotAvailableException extends Exception {
    public EmailClientNotAvailableException(String message) {
        super(message);
    }
}
