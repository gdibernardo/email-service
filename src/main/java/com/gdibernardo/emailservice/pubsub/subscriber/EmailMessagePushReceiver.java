package com.gdibernardo.emailservice.pubsub.subscriber;

import com.gdibernardo.emailservice.email.Email;
import com.gdibernardo.emailservice.email.EmailStatus;
import com.gdibernardo.emailservice.email.service.DatastoreEmailService;
import com.gdibernardo.emailservice.email.service.EmailSenderService;
import com.gdibernardo.emailservice.pubsub.EmailMessage;
import com.gdibernardo.emailservice.util.Utils;
import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class EmailMessagePushReceiver {

    private static final Logger log = Logger.getLogger(EmailMessagePushReceiver.class.getName());

    private static final String PUBSUB_VERIFICATION_TOKEN = "PUBSUB_VERIFICATION_TOKEN";

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private DatastoreEmailService datastoreEmailService;

    private String pubSubVerificationToken;

    @PostMapping("/pubsub/push")
    public void pubSubPushReceive(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if(request.getParameter("token").compareTo(pubSubVerificationToken) != 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String requestBody = request.getReader().lines().collect(Collectors.joining("\n"));
        JsonElement jsonRoot = new JsonParser().parse(requestBody);

        String messageData = jsonRoot.getAsJsonObject()
                .get("message")
                .getAsJsonObject()
                .get("data")
                .getAsString();

        try {
            EmailMessage receivedMessage = EmailMessage.fromJSONString(Utils.decodeBase64(messageData));
            log.info(String.format("EmailMessagePushReceiver: Received message %s", receivedMessage.toString()));

            Email email = datastoreEmailService.fetchEmail(receivedMessage.getId());

            if(email.getStatus() == EmailStatus.SENT) {
                return;
            }

            if(emailSenderService.send(email)) {
                email.setStatus(EmailStatus.SENT);
            } else {
                email.setStatus(EmailStatus.PENDING);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            if(datastoreEmailService.persistEmail(email) == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        catch (EntityNotFoundException exception) {
            log.warning(String.format("EmailMessagePushReceiver: Cannot fetch email: %s.", exception.getMessage()));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        catch (DatastoreFailureException exception) {
            log.warning(String.format("EmailMessagePushReceiver: Datastore failure %s.", exception.getMessage()));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        catch (Exception exception) {
            log.warning(String.format("EmailMessagePushReceiver: Cannot parse received message: %s.", exception.getMessage()));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @PostConstruct
    private void loadPubSubVerificationToken() {
        pubSubVerificationToken = System.getenv(PUBSUB_VERIFICATION_TOKEN);
    }
}
