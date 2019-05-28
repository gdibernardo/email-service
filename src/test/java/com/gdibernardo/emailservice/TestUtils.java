package com.gdibernardo.emailservice;

import com.gdibernardo.emailservice.email.model.EmailAddress;
import com.gdibernardo.emailservice.email.model.EmailMessage;

public final class TestUtils {
    private TestUtils() {}

    public static EmailMessage dummyEmailMessage() {
        EmailAddress from = new EmailAddress("mary@provider.com", "Mary");
        EmailAddress to = new EmailAddress("jack@provider.com", "Jack");

        return new EmailMessage(from, to, "It's not you, it is me.", "It does not work anymore.");
    }

    public static EmailMessage emailMessage(String fromAddress,
                                            String fromName,
                                            String toAddress,
                                            String toName,
                                            String subject,
                                            String content) {

        return new EmailMessage(new EmailAddress(fromAddress, fromName),
                new EmailAddress(toAddress, toName),
                subject,
                content);

    }
}
