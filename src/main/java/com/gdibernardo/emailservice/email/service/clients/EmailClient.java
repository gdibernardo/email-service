package com.gdibernardo.emailservice.email.service.clients;

import com.gdibernardo.emailservice.pubsub.EmailMessage;

public interface EmailClient {
    void sendEmail(EmailMessage emailMessage) throws EmailClientNotAvailableException;
}
