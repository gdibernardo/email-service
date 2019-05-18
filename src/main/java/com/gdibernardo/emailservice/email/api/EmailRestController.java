package com.gdibernardo.emailservice.email.api;

import com.gdibernardo.emailservice.email.EmailValidator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class EmailRestController {

    @PostMapping("/send")
    public ResponseEntity<String> send(@Valid @RequestBody Email email) {

        if(EmailValidator.isValid(email.getFrom()) && EmailValidator.isValid(email.getTo())) {
            return ResponseEntity.ok("Email received.");
        }

        return ResponseEntity.badRequest().body("Email addresses have to be formatted correctly.");
    }
}
