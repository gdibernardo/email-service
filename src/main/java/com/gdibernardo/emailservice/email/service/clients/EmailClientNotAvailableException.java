package com.gdibernardo.emailservice.email.service.clients;

public class EmailClientNotAvailableException extends Exception {
    public EmailClientNotAvailableException(String message) {
        super(message);
    }
}
