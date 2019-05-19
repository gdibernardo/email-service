package com.gdibernardo.emailservice.pubsub.subscriber;

import com.gdibernardo.emailservice.email.service.EmailSenderService;
import com.gdibernardo.emailservice.pubsub.EmailMessage;
import com.gdibernardo.emailservice.util.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class EmailMessagePushReceiver {

    @Value("${gcp.pub-sub.verification-token}")
    private String pubSubVerificationToken;

    @Autowired
    private EmailSenderService emailSenderService;

    private static final Logger log = Logger.getLogger(EmailMessagePushReceiver.class.getName());

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
            emailSenderService.send(receivedMessage);
        } catch (Exception exception) {
            log.warning("EmailMessagePushReceiver: Cannot parse received message.");
            log.warning("" + exception.getLocalizedMessage());
        }
    }
}
