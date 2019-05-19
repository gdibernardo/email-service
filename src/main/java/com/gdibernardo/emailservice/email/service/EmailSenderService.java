package com.gdibernardo.emailservice.email.service;

import com.gdibernardo.emailservice.pubsub.EmailMessage;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class EmailSenderService {

    private static final Logger log = Logger.getLogger(EmailSenderService.class.getName());

    public void send(EmailMessage emailMessage) {
        log.info("Sending email " + emailMessage.toString());
    }
}
