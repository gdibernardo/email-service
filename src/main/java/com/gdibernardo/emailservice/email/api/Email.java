package com.gdibernardo.emailservice.email.api;

import javax.validation.constraints.NotNull;

public class Email {

    private String to;
    private String from;
    @NotNull(message = "Subject is a mandatory field.")
    private String subject;
    @NotNull(message = "Content is a mandatory field.")
    private String content;

    public void setTo(String to) {
        this.to = to;
    }
    public String getTo() {
        return to;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }


    public void setFrom(String from) {
        this.from = from;
    }
    public String getFrom() {
        return from;
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getSubject() {
        return subject;
    }
}
