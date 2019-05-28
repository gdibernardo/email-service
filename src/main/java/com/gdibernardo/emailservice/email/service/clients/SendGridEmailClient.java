package com.gdibernardo.emailservice.email.service.clients;

import com.gdibernardo.emailservice.email.model.EmailMessage;
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

    public void sendEmail(EmailMessage emailMessage) throws EmailClientNotAvailableException {
        log.info(String.format("Sending email %s from SendGrid email client.", emailMessage.toString()));

        Email to = new Email(emailMessage.getTo().getAddress());
        Email from = new Email(emailMessage.getFrom().getAddress(), emailMessage.getFrom().getName());

        //  Our service will support only text/plain content for now.
        Content content = new Content(SENDGRID_MAIL_CONTENT_TYPE, emailMessage.getContent());
        Mail mail = new Mail(from, emailMessage.getSubject(), to, content);

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
    }
}

