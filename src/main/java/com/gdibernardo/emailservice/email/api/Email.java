package com.gdibernardo.emailservice.email.api;

import com.gdibernardo.emailservice.email.model.EmailAddress;

import javax.validation.constraints.NotNull;

public class Email {

    private EmailAddress to;
    private EmailAddress from;
    @NotNull(message = "Subject is a mandatory field.")
    private String subject;
    @NotNull(message = "Content is a mandatory field.")
    private String content;

    public void setTo(EmailAddress to) {
        this.to = to;
    }
    public EmailAddress getTo() {
        return to;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setFrom(EmailAddress from) {
        this.from = from;
    }
    public EmailAddress getFrom() {
        return from;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getSubject() {
        return subject;
    }
}
