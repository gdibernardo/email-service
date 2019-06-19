package com.gdibernardo.emailservice.email.service.clients.base;

import com.gdibernardo.emailservice.email.EmailMessage;

public interface EmailClient {
    void sendEmail(EmailMessage emailMessage) throws EmailClientNotAvailableException;
}
