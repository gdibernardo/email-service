package com.gdibernardo.emailservice.email.service;

import com.gdibernardo.emailservice.email.Email;
import com.gdibernardo.emailservice.email.repository.DatastoreEmailRepository;
import com.gdibernardo.emailservice.email.repository.model.DatastoreEmail;
import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.EntityNotFoundException;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
public class DatastoreEmailService {

    private static final String PERSIST_EMAIL_RETRY = "persistEmailRetry";

    private DatastoreEmailRepository datastoreEmailRepository;

    private RetryRegistry retryRegistry;

    public DatastoreEmailService(DatastoreEmailRepository datastoreEmailRepository,
                                 @Value("${resilience4j.retry.max-attempts}") int retryMaxAttempts,
                                 @Value("${resilience4j.retry.wait-duration-ms}") long retryWaitDuration) {
        this.datastoreEmailRepository = datastoreEmailRepository;

        retryRegistry = RetryRegistry.of(
                RetryConfig.custom()
                        .maxAttempts(retryMaxAttempts)
                        .waitDuration(Duration.ofMillis(retryWaitDuration))
                        .retryExceptions(DatastoreFailureException.class)
                        .build());
    }

    public Email fetchEmail(long emailId) throws EntityNotFoundException {
        return fetch(emailId).getEmail();
    }

    public DatastoreEmail fetch(long emailId) throws EntityNotFoundException {
        return datastoreEmailRepository.fetch(emailId);
    }

    public long persistEmail(Email email) {
        Retry retry = retryRegistry.retry(PERSIST_EMAIL_RETRY);

        Supplier<Long> persistEmailSupplier = Retry.decorateSupplier(retry,
                () -> datastoreEmailRepository.persist(new DatastoreEmail(email)));

        return Try.ofSupplier(persistEmailSupplier)
                .recover(throwable -> 0L)
                .get();
    }
}
