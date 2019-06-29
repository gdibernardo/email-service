package com.gdibernardo.emailservice.email;

import com.gdibernardo.emailservice.email.rest.model.RestEmail;
import com.gdibernardo.emailservice.pubsub.EmailMessage;

public class Email {
    private long id;

    private EmailAddress from;
    private EmailAddress to;
    private String subject;
    private String content;

    private EmailStatus status;

    public Email(EmailAddress from,
                 EmailAddress to,
                 String subject,
                 String content,
                 EmailStatus status) {

        this.id = 0;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;

        this.status = status;
    }

    public Email(long id,
                 EmailAddress from,
                 EmailAddress to,
                 String subject,
                 String content,
                 EmailStatus status) {
        this(from, to, subject, content, status);

        this.id = id;
    }

    public long getId() {
        return id;
    }

    public EmailAddress getFrom() {
        return from;
    }

    public void setFrom(EmailAddress from) {
        this.from = from;
    }

    public EmailAddress getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public void setStatus(EmailStatus status) {
        this.status = status;
    }

    public EmailMessage emailMessage() {
        return new EmailMessage(id);
    }

    @Override
    public String toString() {
        return "DatastoreEmail {" +
                "id='"          + id                    + '\'' +
                ", from='"      + from.getAddress()     + " - " + from.getName() + '\'' +
                ", to='"        + to.getAddress()       + " - " + to.getName()   + '\'' +
                ", subject='"   + subject               + '\'' +
                ", content='"   + content               + '\'' +
                ", status='"    + status.name()         + '\'' +
                '}';
    }

    public static Email fromRest(RestEmail restEmail) {
        return new Email(restEmail.getFrom(),
                restEmail.getTo(),
                restEmail.getSubject(),
                restEmail.getContent(),
                EmailStatus.ENQUEUED);
    }
}
