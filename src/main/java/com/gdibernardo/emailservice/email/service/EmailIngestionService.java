package com.gdibernardo.emailservice.email.service;

import com.gdibernardo.emailservice.email.Email;
import com.gdibernardo.emailservice.email.EmailAddress;
import com.gdibernardo.emailservice.pubsub.EmailMessage;
import com.gdibernardo.emailservice.pubsub.publisher.EmailMessagePublisherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class EmailIngestionService {

    private static final Logger log = Logger.getLogger(EmailIngestionService.class.getName());

    private String emailAddressSystemSender;

    private EmailMessagePublisherService emailMessagePublisherService;
    private DatastoreEmailService datastoreEmailService;

    public EmailIngestionService(
            EmailMessagePublisherService emailMessagePublisherService,
            DatastoreEmailService datastoreEmailService,
            @Value("${email-system-sender.address}") String emailAddressSystemSender) {

        this.emailMessagePublisherService = emailMessagePublisherService;
        this.datastoreEmailService = datastoreEmailService;
        this.emailAddressSystemSender = emailAddressSystemSender;
    }

    public long ingest(Email email) {

        setSystemSender(email);

        log.info(String.format("EmailIngestionService: Ingesting email %s.", email.toString()));

        long id = datastoreEmailService.persistEmail(email);

        if(id != 0) {
            emailMessagePublisherService.publish(new EmailMessage(id));
        }

        return id;
    }

    private void setSystemSender(Email email) {
        EmailAddress systemSender;

        if (email.getFrom() != null && email.getFrom().hasName()) {
            systemSender = new EmailAddress(emailAddressSystemSender, email.getFrom().getName());
        } else {
            systemSender = new EmailAddress(emailAddressSystemSender);
        }

        email.setFrom(systemSender);
    }
}
