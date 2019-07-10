package com.gdibernardo.emailservice.email.rest;

import com.gdibernardo.emailservice.email.Email;
import com.gdibernardo.emailservice.email.EmailValidator;
import com.gdibernardo.emailservice.email.repository.model.DatastoreEmail;
import com.gdibernardo.emailservice.email.rest.model.response.ResponseMessage;
import com.gdibernardo.emailservice.email.rest.model.RestEmail;
import com.gdibernardo.emailservice.email.rest.model.response.EmailStatusResponseMessage;
import com.gdibernardo.emailservice.email.rest.model.response.EmailSubmitResponseMessage;
import com.gdibernardo.emailservice.email.service.DatastoreEmailService;
import com.gdibernardo.emailservice.email.service.EmailIngestionService;
import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
public class EmailRestController {

    @Autowired
    private EmailIngestionService emailIngestionService;

    @Autowired
    private DatastoreEmailService datastoreEmailService;

    @PostMapping(value="/emails/submit", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> submit(@Valid @RequestBody RestEmail restEmail, Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(errors.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        if(!EmailValidator.isValid(restEmail.getTo().getAddress())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Email address should be in the correct format.", HttpStatus.BAD_REQUEST.value()));
        }

        long emailId = emailIngestionService.ingest(Email.fromRest(restEmail));
        if(emailId == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Email Service was not able to enqueue your request.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new EmailSubmitResponseMessage("Email correctly enqueued.", HttpStatus.OK.value(), emailId));
    }

    @GetMapping(value = "/emails/status/{emailId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> getStatus(@PathVariable("emailId") @NotBlank String emailId) {
        try {
            long parsedEmailId = Long.parseLong(emailId);
            DatastoreEmail datastoreEmail = datastoreEmailService.fetch(parsedEmailId);

            return ResponseEntity.status(HttpStatus.OK).body(new EmailStatusResponseMessage("Email status correctly fetched from Email Service.",
                    HttpStatus.OK.value(),
                    parsedEmailId,
                    datastoreEmail.getEmail().getStatus().name(),
                    datastoreEmail.getModifiedAt()));
        } catch (NumberFormatException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Email id does not have a valid format.", HttpStatus.BAD_REQUEST.value()));
        } catch (EntityNotFoundException exception) {
        //  Email identifier should be returned too.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Email Service cannot find such email id.", HttpStatus.NOT_FOUND.value()));
        } catch (DatastoreFailureException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Something went wrong while fetching the status.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
