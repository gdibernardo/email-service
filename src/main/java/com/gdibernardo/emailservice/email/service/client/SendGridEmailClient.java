package com.gdibernardo.emailservice.email.service.client;

import com.gdibernardo.emailservice.email.Email;
import com.gdibernardo.emailservice.email.service.client.base.EmailClient;
import com.gdibernardo.emailservice.email.service.client.base.EmailClientNotAvailableException;
import com.sendgrid.*;

import java.io.IOException;
import java.util.logging.Logger;

public class SendGridEmailClient implements EmailClient {

    private static final Logger log = Logger.getLogger(SendGridEmailClient.class.getName());

    private static final String SENDGRID_API_KEY = "SENDGRID_API_KEY";
    private static final String SENDGRID_SEND_ENDPOINT = "mail/send";

    private static final String SENDGRID_MAIL_CONTENT_TYPE = "text/plain";

    private SendGrid sendGrid;

    public SendGridEmailClient() {
        sendGrid = new SendGrid(System.getenv(SENDGRID_API_KEY));
    }

    public boolean sendEmail(Email email) {
        log.info(String.format("Sending email %s from SendGrid email client.", email.toString()));

        com.sendgrid.Email to = new com.sendgrid.Email(email.getTo().getAddress());
        com.sendgrid.Email from = new com.sendgrid.Email(email.getFrom().getAddress(), email.getFrom().getName());

        //  Our service will support only text/plain content for now.
        Content content = new Content(SENDGRID_MAIL_CONTENT_TYPE, email.getContent());
        Mail mail = new Mail(from, email.getSubject(), to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(SENDGRID_SEND_ENDPOINT);

            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            if (response.getStatusCode() != 202) {
                throw new EmailClientNotAvailableException(String.format("SendGridEmailClient raised an exception: Client response status %d", response.getStatusCode()));
            }

        } catch (IOException exception) {
            throw new EmailClientNotAvailableException(String.format("SendGridEmailClient raised an exception: %s", exception.getMessage()));
        }

        return true;
    }
}

