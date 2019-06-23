package com.gdibernardo.emailservice.pubsub.publisher;

import com.gdibernardo.emailservice.email.EmailAddress;
import com.gdibernardo.emailservice.email.EmailMessage;
import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class EmailMessagePublisherService {

    private static final Logger log = Logger.getLogger(EmailMessagePublisherService.class.getName());

    private String emailAddressSystemSender;
    private Publisher emailMessagePublisher;

    public EmailMessagePublisherService(Publisher emailMessagePublisher,
                                        @Value("${email-system-sender.address}") String emailAddressSystemSender) {
        this.emailMessagePublisher = emailMessagePublisher;
        this.emailAddressSystemSender = emailAddressSystemSender;
    }

    public boolean publish(EmailMessage message) {

        setSystemSender(message);

        try {
            PubsubMessage pubsubMessage = message.toPubSubMessage();
            ApiFuture<String> future = emailMessagePublisher.publish(pubsubMessage);

            String messageId = future.get();

            log.info(String.format("EmailMessagePublisherService: published message %s with id %s to Pub/Sub.", message.toString(), messageId));
            return true;
        } catch (Exception exception) {
            log.warning(String.format("EmailMessagePublisherService: Failed while trying to publish a message. Exception: %s", exception.getMessage()));
            return false;
        }
    }


    private void setSystemSender(EmailMessage clientEmailMessage) {
        EmailAddress systemSender;

        if(clientEmailMessage.getFrom() != null && clientEmailMessage.getFrom().hasName()) {
           systemSender = new EmailAddress(emailAddressSystemSender, clientEmailMessage.getFrom().getName());
        } else {
            systemSender = new EmailAddress(emailAddressSystemSender);
        }

        clientEmailMessage.setFrom(systemSender);
    }
}
