package com.gdibernardo.emailservice.email.rest;

import com.gdibernardo.emailservice.email.EmailValidator;
import com.gdibernardo.emailservice.email.EmailMessage;
import com.gdibernardo.emailservice.email.rest.model.Email;
import com.gdibernardo.emailservice.email.rest.model.ResponseMessage;
import com.gdibernardo.emailservice.pubsub.publisher.EmailMessagePublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class EmailRestController {

    @Autowired
    private EmailMessagePublisherService emailMessagePublisherService;

    @PostMapping(value="/emails/submit", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> submit(@Valid @RequestBody Email email, Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(errors.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        if(!EmailValidator.isValid(email.getTo().getAddress())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Email address should be in the correct format.", HttpStatus.BAD_REQUEST.value()));
        }

        if(!emailMessagePublisherService.publish(EmailMessage.fromEmail(email))) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Email Service was not able to enqueue your request.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Email correctly enqueued.", HttpStatus.OK.value()));
    }
}
