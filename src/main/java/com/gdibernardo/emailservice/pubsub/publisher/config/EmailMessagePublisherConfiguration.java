package com.gdibernardo.emailservice.pubsub.publisher.config;

import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.ProjectTopicName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.threeten.bp.Duration;

@Configuration
public class EmailMessagePublisherConfiguration {

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.pub-sub.topic-name}")
    private String topicName;

    @Bean
    public Publisher emailMessagePublisher() throws Exception {
        ProjectTopicName projectTopicName = ProjectTopicName.of(projectId, topicName);

        int executorThreadCount = 4;

        return Publisher.newBuilder(projectTopicName)
                .setExecutorProvider(InstantiatingExecutorProvider.newBuilder()
                        .setExecutorThreadCount(executorThreadCount)
                        .build())
                .setRetrySettings(defaultRetrySettings())
                .build();
    }


    private static RetrySettings defaultRetrySettings() {
        Duration retryDelay = Duration.ofMillis(5);

        double retryDelayMultiplier = 2.0;
        Duration maxRetryDelay = Duration.ofSeconds(60);
        Duration totalTimeout = Duration.ofSeconds(10);
        Duration initialRpcTimeout = Duration.ofSeconds(10);
        Duration maxRpcTimeout = Duration.ofSeconds(10);

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
