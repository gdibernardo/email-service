package com.gdibernardo.emailservice.email.service.client;

import com.gdibernardo.emailservice.email.Email;
import com.gdibernardo.emailservice.email.service.client.base.EmailClient;
import com.gdibernardo.emailservice.email.service.client.base.EmailClientNotAvailableException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;

import java.util.logging.Logger;

public class MailjetEmailClient implements EmailClient {

    private static final Logger log = Logger.getLogger(MailjetEmailClient.class.getName());

    private static final String MAILJET_API_KEY = "MAILJET_API_KEY";
    private static final String MAILJET_SECRET_KEY = "MAILJET_SECRET_KEY";

    private MailjetClient mailjetClient;

    public MailjetEmailClient() {
        mailjetClient = new MailjetClient(System.getenv(MAILJET_API_KEY), System.getenv(MAILJET_SECRET_KEY));
    }

    public boolean sendEmail(Email email) {
        log.info(String.format("Sending email %s from Mailjet email client.", email.toString()));

        MailjetRequest request = new MailjetRequest(com.mailjet.client.resource.Email.resource)
                .property(com.mailjet.client.resource.Email.FROMEMAIL, email.getFrom().getAddress())
                .property(com.mailjet.client.resource.Email.FROMNAME, email.getFrom().getName())
                .property(com.mailjet.client.resource.Email.TO, email.getTo().getAddress())
                .property(com.mailjet.client.resource.Email.SUBJECT, email.getSubject())
                .property(com.mailjet.client.resource.Email.TEXTPART, email.getContent());
        try {
            MailjetResponse response = mailjetClient.post(request);
            if(response.getStatus() != 200 && response.getStatus() != 201) {
                throw new EmailClientNotAvailableException(String.format("MailjetEmailClient raised an exception: Client response status %d", response.getStatus()));
            }
        } catch (Exception exception) {
            throw new EmailClientNotAvailableException(String.format("MailjetEmailClient raised an exception: %s", exception.getMessage()));
        }

        return true;
    }
}
