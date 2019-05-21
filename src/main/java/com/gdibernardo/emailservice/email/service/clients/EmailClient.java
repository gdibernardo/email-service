package com.gdibernardo.emailservice.email.service.clients;

import com.gdibernardo.emailservice.email.model.EmailMessage;

public interface EmailClient {
    void sendEmail(EmailMessage emailMessage) throws EmailClientNotAvailableException;
}
