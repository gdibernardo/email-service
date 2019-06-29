package com.gdibernardo.emailservice.pubsub.publisher;

import com.gdibernardo.emailservice.pubsub.EmailMessage;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class EmailMessagePublisherService {

    private static final Logger log = Logger.getLogger(EmailMessagePublisherService.class.getName());

    private Publisher emailMessagePublisher;

    public EmailMessagePublisherService(Publisher emailMessagePublisher) {
        this.emailMessagePublisher = emailMessagePublisher;
    }

    public void publish(EmailMessage message) {

        try {
            PubsubMessage pubsubMessage = message.toPubSubMessage();
            ApiFuture<String> future = emailMessagePublisher.publish(pubsubMessage);

            ApiFutures.addCallback(future,
                    new ApiFutureCallback<String>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            log.warning(String.format("Failed sending message to PubSub: %s", throwable.getMessage()));
                        }

                        @Override
                        public void onSuccess(String messageId) {
                            log.info(String.format("Successfully sent message with id %s.", messageId));
                        }
                    },
                    MoreExecutors.directExecutor());

        } catch (Exception exception) {
            log.warning(String.format("EmailMessagePublisherService: Failed while trying to publish a message. Exception: %s", exception.getMessage()));
        }
    }
}
