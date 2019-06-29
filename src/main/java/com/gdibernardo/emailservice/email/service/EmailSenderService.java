package com.gdibernardo.emailservice.email.service;

import com.gdibernardo.emailservice.email.Email;
import com.gdibernardo.emailservice.email.service.client.base.EmailClient;
import com.gdibernardo.emailservice.email.service.client.MailjetEmailClient;
import com.gdibernardo.emailservice.email.service.client.SendGridEmailClient;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

@Service
public class EmailSenderService {

    private static final Logger log = Logger.getLogger(EmailSenderService.class.getName());

    private List<EmailClient> emailClients = new LinkedList<>();

    private CircuitBreakerRegistry circuitBreakerRegistry;

    private RetryRegistry retryRegistry;

    public EmailSenderService(CircuitBreakerRegistry circuitBreakerRegistry,
                              RetryRegistry retryRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.retryRegistry = retryRegistry;
    }

    public void addEmailClient(EmailClient emailClient) {
        emailClients.add(emailClient);
    }

    public boolean send(Email email) {

        for(EmailClient emailClient : emailClients) {

            String emailClientIdentifier = emailClient.getClass().getSimpleName();
            log.info(String.format("EmailSenderService: trying sending email from %s.", emailClientIdentifier));

            CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(emailClientIdentifier);
            Retry retry = retryRegistry.retry(emailClientIdentifier);

            Supplier<Boolean> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker,
                    () -> emailClient.sendEmail(email));

            decoratedSupplier = Retry.decorateSupplier(retry, decoratedSupplier);

            Boolean emailClientResponse = Try.ofSupplier(decoratedSupplier)
                    .recover(throwable -> false)
                    .get();

            if(emailClientResponse == true) {
                log.info(String.format("EmailSenderService: email %s sent correctly.", email.toString()));
                return true;
            }
        }

        log.info(String.format("EmailSenderService: email %s has not been sent.", email.toString()));
        return false;
    }

    @PostConstruct
    private void initEmailClients() {
        emailClients.add(new SendGridEmailClient());
        emailClients.add(new MailjetEmailClient());
    }
}
