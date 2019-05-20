package com.gdibernardo.emailservice.email.service.clients;

import com.gdibernardo.emailservice.pubsub.EmailMessage;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Email;

import java.util.logging.Logger;

public class MailjetEmailClient implements EmailClient {

    private static final Logger log = Logger.getLogger(MailjetEmailClient.class.getName());

    private static final String MAILJET_API_KEY = "MAILJET_API_KEY";
    private static final String MAILJET_SECRET_KEY = "MAILJET_SECRET_KEY";

    private MailjetClient mailjetClient;

    public MailjetEmailClient() {
        mailjetClient = new MailjetClient(System.getenv(MAILJET_API_KEY), System.getenv(MAILJET_SECRET_KEY));
    }

    public void sendEmail(EmailMessage emailMessage) throws EmailClientNotAvailableException {
        log.info(String.format("Sending email %s from Mailjet email client.", emailMessage.toString()));

        MailjetRequest request = new MailjetRequest(Email.resource)
                .property(Email.FROMEMAIL, emailMessage.getFrom())
                .property(Email.TO, emailMessage.getTo())
                .property(Email.SUBJECT, emailMessage.getSubject())
                .property(Email.TEXTPART, emailMessage.getContent());
        try {
            MailjetResponse response = mailjetClient.post(request);
            if(response.getStatus() != 200 && response.getStatus() != 201) {
                throw new EmailClientNotAvailableException(String.format("MailjetEmailClient raised an exception: Client response status %d", response.getStatus()));
            }
        } catch (Exception exception) {
            throw new EmailClientNotAvailableException(String.format("MailjetEmailClient raised an exception: %s", exception.getMessage()));
        }
    }
}
