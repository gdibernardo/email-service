package com.gdibernardo.emailservice.email.service.client.base;

import com.gdibernardo.emailservice.email.Email;

public interface EmailClient {
    boolean sendEmail(Email email);
}
