package com.gdibernardo.emailservice.email.service.client.base;

import com.gdibernardo.emailservice.email.EmailMessage;

public interface EmailClient {
    boolean sendEmail(EmailMessage emailMessage);
}
