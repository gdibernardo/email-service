package com.gdibernardo.emailservice.email.api;

import com.gdibernardo.emailservice.email.EmailValidator;
import com.gdibernardo.emailservice.email.EmailMessage;
import com.gdibernardo.emailservice.pubsub.publisher.EmailMessagePublisherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class EmailRestController {

    @Autowired
    private EmailMessagePublisherService emailMessagePublisherService;

    @PostMapping("/send")
    public ResponseEntity<String> send(@Valid @RequestBody Email email) {

        if(EmailValidator.isValid(email.getTo())) {
            emailMessagePublisherService.publish(EmailMessage.fromEmail(email));
            return ResponseEntity.ok("Email received.");
        }

        return ResponseEntity.badRequest().body("Email addresses have to be formatted correctly.");
    }
}
