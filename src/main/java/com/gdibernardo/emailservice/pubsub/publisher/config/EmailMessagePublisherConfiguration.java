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

    @Value("${gcp.pub-sub.publisher.executor-thread-count}")
    private int executorThreadCount;

    @Value("${gcp.pub-sub.publisher.retry-settings.retry-delay}")
    private long retryDelay;

    @Value("${gcp.pub-sub.publisher.retry-settings.retry-delay-multiplier}")
    private double retryDelayMultiplier;

    @Value("${gcp.pub-sub.publisher.retry-settings.max-retry-delay}")
    private long maxRetryDelay;

    @Value("${gcp.pub-sub.publisher.retry-settings.total-timeout}")
    private long totalTimeout;

    @Value("${gcp.pub-sub.publisher.retry-settings.initial-rpc-timeout}")
    private long initialRpcTimeout;

    @Value("${gcp.pub-sub.publisher.retry-settings.max-rpc-timeout}")
    private long maxRpcTimeout;


    @Bean
    public Publisher emailMessagePublisher() throws Exception {
        ProjectTopicName projectTopicName = ProjectTopicName.of(projectId, topicName);

        return Publisher.newBuilder(projectTopicName)
                .setExecutorProvider(InstantiatingExecutorProvider.newBuilder()
                        .setExecutorThreadCount(executorThreadCount)
                        .build())
                .setRetrySettings(defaultRetrySettings())
                .build();
    }


    private RetrySettings defaultRetrySettings() {
        return RetrySettings.newBuilder()
                .setInitialRetryDelay(Duration.ofMillis(retryDelay))
                .setRetryDelayMultiplier(retryDelayMultiplier)
                .setMaxRetryDelay(Duration.ofSeconds(maxRetryDelay))
                .setTotalTimeout(Duration.ofSeconds(totalTimeout))
                .setInitialRpcTimeout(Duration.ofSeconds(initialRpcTimeout))
                .setMaxRpcTimeout(Duration.ofSeconds(maxRpcTimeout))
                .build();
    }
}
