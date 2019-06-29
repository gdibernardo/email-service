package com.gdibernardo.emailservice.email.rest.model.response;

public class EmailSubmitResponseMessage extends ResponseMessage {

    private long emailId;

    public EmailSubmitResponseMessage(String message, int httpStatusCode, long emailId) {
        super(message, httpStatusCode);

        this.emailId = emailId;
    }

    public long getEmailId() {
        return emailId;
    }
}
