package com.gdibernardo.emailservice.email.rest.model.response;

import java.util.Date;

public class EmailStatusResponseMessage extends EmailSubmitResponseMessage {

    private String emailStatus;

    private Date lastUpdate;

    public EmailStatusResponseMessage(String message,
                                      int httpStatusCode,
                                      long emailId,
                                      String emailStatus,
                                      Date lastUpdate) {
        super(message, httpStatusCode, emailId);

        this.emailStatus = emailStatus;
        this.lastUpdate = lastUpdate;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }
}
