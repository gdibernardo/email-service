package com.gdibernardo.emailservice.email.service;

import com.gdibernardo.emailservice.email.service.clients.EmailClient;
import com.gdibernardo.emailservice.email.service.clients.EmailClientNotAvailableException;
import com.gdibernardo.emailservice.email.service.clients.MailjetEmailClient;
import com.gdibernardo.emailservice.email.service.clients.SendGridEmailClient;
import com.gdibernardo.emailservice.email.model.EmailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class EmailSenderService {

    private static final Logger log = Logger.getLogger(EmailSenderService.class.getName());

    private List<EmailClient> emailClients = new LinkedList<>();

    @PostConstruct
    private void initEmailClients() {
        emailClients.add(new SendGridEmailClient());
        emailClients.add(new MailjetEmailClient());
    }

    public boolean send(EmailMessage emailMessage) {

        for(EmailClient emailClient : emailClients) {
            log.info(String.format("EmailSenderService: trying sending email from %s.", emailClient.getClass().getSimpleName()));
            try {
                emailClient.sendEmail(emailMessage);
                log.info(String.format("Email %s sent correctly.", emailMessage.toString()));
                return true;
            } catch (EmailClientNotAvailableException exception) {
                log.warning(exception.getMessage());
            }
        }

        log.info(String.format("Email %s has not been sent.", emailMessage.toString()));
        return false;
    }
}
