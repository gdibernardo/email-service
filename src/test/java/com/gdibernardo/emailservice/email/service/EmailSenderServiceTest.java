package com.gdibernardo.emailservice.email.service;

import com.gdibernardo.emailservice.TestUtils;
import com.gdibernardo.emailservice.email.Email;
import com.gdibernardo.emailservice.email.service.client.base.EmailClientNotAvailableException;
import com.gdibernardo.emailservice.email.service.client.MailjetEmailClient;
import com.gdibernardo.emailservice.email.service.client.SendGridEmailClient;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EmailSenderServiceTest {

    @Test
    public void GivenEmailSenderServiceWithNoClients_WhenSendEmail_ThenReturnsFalse() {
        EmailSenderService emailSenderService = new EmailSenderService(TestUtils.defaultCircuitBreakerRegistry(), TestUtils.defaultRetryRegistry());

        Assert.assertEquals(emailSenderService.send(TestUtils.dummyEmailMessage()), false);
    }

    @Test
    public void GivenEmailSenderServiceWithANotAvailableClient_WhenSendEmail_ThenReturnsFalse() {
        SendGridEmailClient sendGridEmailClient = mock(SendGridEmailClient.class);

        doThrow(EmailClientNotAvailableException.class)
                .when(sendGridEmailClient)
                .sendEmail(any(Email.class));

        EmailSenderService emailSenderService = new EmailSenderService(TestUtils.defaultCircuitBreakerRegistry(), TestUtils.defaultRetryRegistry());
        emailSenderService.addEmailClient(sendGridEmailClient);

        Assert.assertEquals(emailSenderService.send(TestUtils.dummyEmailMessage()), false);
    }

    @Test
    public void GivenEmailSenderServiceWithANotAvailableClientAndAnAvailableClient_WhenSendEmail_ThenReturnsTrue() {
        SendGridEmailClient sendGridEmailClient = mock(SendGridEmailClient.class);
        doThrow(EmailClientNotAvailableException.class)
                .when(sendGridEmailClient)
                .sendEmail(any(Email.class));

        MailjetEmailClient mailjetEmailClient = mock(MailjetEmailClient.class);

        doReturn(true)
                .when(mailjetEmailClient)
                .sendEmail(any(Email.class));

        EmailSenderService emailSenderService = new EmailSenderService(TestUtils.defaultCircuitBreakerRegistry(), TestUtils.defaultRetryRegistry());

        emailSenderService.addEmailClient(sendGridEmailClient);
        emailSenderService.addEmailClient(mailjetEmailClient);

        Assert.assertEquals(emailSenderService.send(TestUtils.dummyEmailMessage()), true);
    }

    @Test
    public void GivenEmailSenderServiceWithTwoNotAvailableClients_WhenSendEmail_ThenReturnsFalse() {
        SendGridEmailClient sendGridEmailClient = mock(SendGridEmailClient.class);
        doThrow(EmailClientNotAvailableException.class)
                .when(sendGridEmailClient)
                .sendEmail(any(Email.class));

        MailjetEmailClient mailjetEmailClient = mock(MailjetEmailClient.class);
        doThrow(EmailClientNotAvailableException.class)
                .when(mailjetEmailClient)
                .sendEmail(any(Email.class));

        EmailSenderService emailSenderService = new EmailSenderService(TestUtils.defaultCircuitBreakerRegistry(), TestUtils.defaultRetryRegistry());

        emailSenderService.addEmailClient(sendGridEmailClient);
        emailSenderService.addEmailClient(mailjetEmailClient);

        Assert.assertEquals(emailSenderService.send(TestUtils.dummyEmailMessage()), false);
    }


    @Test
    public void GivenEmailSenderServiceWithAnAvailableClientAndANotAvailableClient_WhenSendEmail_ThenReturnsTrue() {
        SendGridEmailClient sendGridEmailClient = mock(SendGridEmailClient.class);

        doReturn(true)
                .when(sendGridEmailClient)
                .sendEmail(any(Email.class));

        MailjetEmailClient mailjetEmailClient = mock(MailjetEmailClient.class);
        doThrow(EmailClientNotAvailableException.class)
                .when(mailjetEmailClient)
                .sendEmail(any(Email.class));

        EmailSenderService emailSenderService = new EmailSenderService(TestUtils.defaultCircuitBreakerRegistry(), TestUtils.defaultRetryRegistry());

        emailSenderService.addEmailClient(sendGridEmailClient);
        emailSenderService.addEmailClient(mailjetEmailClient);

        Assert.assertEquals(emailSenderService.send(TestUtils.dummyEmailMessage()), true);
    }
}
