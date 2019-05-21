package com.gdibernardo.emailservice.pubsub.publisher;

import com.gdibernardo.emailservice.email.model.EmailAddress;
import com.google.api.core.ApiFuture;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gdibernardo.emailservice.email.model.EmailMessage;
import org.threeten.bp.Duration;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class EmailMessagePublisherService {

    private static final Logger log = Logger.getLogger(EmailMessagePublisherService.class.getName());

    @Value("${email-system-sender.address}")
    private String emailAddressSystemSender;

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.pub-sub.topic-name}")
    private String topicName;

    private Publisher emailMessagePublisher;


    public boolean publish(EmailMessage message) {

        setSystemSender(message);

        try {
            PubsubMessage pubsubMessage = message.toPubSubMessage();
            ApiFuture<String> future = emailMessagePublisher.publish(pubsubMessage);

            String messageId = future.get();

            log.info(String.format("EmailMessagePublisherService: published message with id %s to Pub/Sub.", messageId));

            return true;

        } catch (Exception exception) {
            log.warning(String.format("EmailMessagePublisherService: Failed while trying to publish a message. Exception: %s", exception.getMessage()));
            return false;
        }
    }

    @PostConstruct
    private void initPublisher() throws Exception {
        ProjectTopicName projectTopicName = ProjectTopicName.of(projectId, topicName);

        int executorThreadCount = 4;

        emailMessagePublisher = Publisher.newBuilder(projectTopicName)
                .setExecutorProvider(InstantiatingExecutorProvider.newBuilder()
                        .setExecutorThreadCount(executorThreadCount)
                        .build())
                .setRetrySettings(defaultRetrySettings())
                .build();
    }

    private RetrySettings defaultRetrySettings() {
        Duration retryDelay = Duration.ofMillis(5);             // default: 5 ms

        double retryDelayMultiplier = 2.0;                      // back off for repeated failures, default: 2.0
        Duration maxRetryDelay = Duration.ofSeconds(60);       // default : Long.MAX_VALUE
        Duration totalTimeout = Duration.ofSeconds(10);         // default: 10 seconds
        Duration initialRpcTimeout = Duration.ofSeconds(10);    // default: 10 seconds
        Duration maxRpcTimeout = Duration.ofSeconds(10);        // default: 10 seconds

        return RetrySettings.newBuilder()
                .setInitialRetryDelay(retryDelay)
                .setRetryDelayMultiplier(retryDelayMultiplier)
                .setMaxRetryDelay(maxRetryDelay)
                .setTotalTimeout(totalTimeout)
                .setInitialRpcTimeout(initialRpcTimeout)
                .setMaxRpcTimeout(maxRpcTimeout)
                .build();
    }

    private void setSystemSender(EmailMessage clientEmailMessage) {
        EmailAddress systemSender = new EmailAddress(emailAddressSystemSender);
        if(clientEmailMessage.getFrom() != null && clientEmailMessage.getFrom().hasName()) {
            systemSender.setName(clientEmailMessage.getFrom().getName());
        }
        clientEmailMessage.setFrom(systemSender);
    }
}
