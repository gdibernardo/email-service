package com.gdibernardo.emailservice.pubsub.publisher;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gdibernardo.emailservice.pubsub.EmailMessage;
import org.threeten.bp.Duration;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class EmailMessagePublisherService {

    private static final Logger log = Logger.getLogger(EmailMessagePublisherService.class.getName());

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.pub-sub.topic-name}")
    private String topicName;

    private Publisher emailMessagePublisher;

    public void publish(EmailMessage message) {
        try {

            PubsubMessage pubsubMessage = message.toPubSubMessage();
            ApiFuture<String> future = emailMessagePublisher.publish(pubsubMessage);

            ApiFutures.addCallback(future,
                    new ApiFutureCallback<String>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            log.warning("Failed sending message to PubSub.");
                        }

                        @Override
                        public void onSuccess(String messageId) {
                            log.info(String.format("Successfully sent message with id %s", messageId));
                        }
                    },
                    MoreExecutors.directExecutor());

        } catch (Exception exception) {
            log.warning("EmailMessagePublisherService: Failed while trying to publish a message.");
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
        Duration maxRetryDelay = Duration.ofSeconds(600);       // default : Long.MAX_VALUE
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
}
