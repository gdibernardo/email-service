package com.gdibernardo.emailservice;

import com.gdibernardo.emailservice.email.Email;
import com.gdibernardo.emailservice.email.EmailAddress;
import com.gdibernardo.emailservice.email.EmailStatus;
import com.gdibernardo.emailservice.email.service.client.base.EmailClientNotAvailableException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

import java.time.Duration;

public final class TestUtils {
    private TestUtils() {}

    public static Email dummyEmailMessage() {
        EmailAddress from = new EmailAddress("mary@provider.com", "Mary");
        EmailAddress to = new EmailAddress("jack@provider.com", "Jack");

        return new Email(23,
                from,
                to,
                "It's not you, it is me.",
                "It does not work anymore.",
                EmailStatus.ENQUEUED);
    }

    public static Email email(long id,
                              String fromAddress,
                              String fromName,
                              String toAddress,
                              String toName,
                              String subject,
                              String content) {

        return new Email(id,
                new EmailAddress(fromAddress, fromName),
                new EmailAddress(toAddress, toName),
                subject,
                content,
                EmailStatus.ENQUEUED);

    }

    public static CircuitBreakerRegistry defaultCircuitBreakerRegistry() {
        return CircuitBreakerRegistry.of(
                CircuitBreakerConfig.custom()
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofMillis(2000))
                        .ringBufferSizeInHalfOpenState(10)
                        .ringBufferSizeInClosedState(10)
                        .build());
    }

    public static RetryRegistry defaultRetryRegistry() {
        return RetryRegistry.of(
                RetryConfig.custom()
                        .maxAttempts(2)
                        .waitDuration(Duration.ofMillis(500))
                        .retryExceptions(EmailClientNotAvailableException.class)
                        .build());
    }
}
