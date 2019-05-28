package com.gdibernardo.emailservice.email.rest;

import com.gdibernardo.emailservice.email.model.EmailAddress;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Email {

    @NotNull(message = "To email address is a mandatory field.")
    private EmailAddress to;
    private EmailAddress from;
    @NotBlank(message = "Subject is a mandatory field.")
    private String subject;
    @NotBlank(message = "Content is a mandatory field.")
    private String content;

    public EmailAddress getTo() {
        return to;
    }

    public EmailAddress getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }
}
