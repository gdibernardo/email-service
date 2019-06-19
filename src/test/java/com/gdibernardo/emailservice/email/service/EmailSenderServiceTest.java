package com.gdibernardo.emailservice.email.service;

import com.gdibernardo.emailservice.TestUtils;
import com.gdibernardo.emailservice.email.EmailMessage;
import com.gdibernardo.emailservice.email.service.clients.base.EmailClientNotAvailableException;
import com.gdibernardo.emailservice.email.service.clients.MailjetEmailClient;
import com.gdibernardo.emailservice.email.service.clients.SendGridEmailClient;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EmailSenderServiceTest {

    @Test
    public void GivenEmailSenderServiceWithNoClients_WhenSendEmail_ThenReturnsFalse() {
        EmailSenderService emailSenderService = new EmailSenderService();

        Assert.assertEquals(emailSenderService.send(TestUtils.dummyEmailMessage()), false);
    }

    @Test
    public void GivenEmailSenderServiceWithANotAvailableClient_WhenSendEmail_ThenReturnsFalse() throws Exception {
        SendGridEmailClient sendGridEmailClient = mock(SendGridEmailClient.class);

        doThrow(EmailClientNotAvailableException.class)
                .when(sendGridEmailClient)
                .sendEmail(any(EmailMessage.class));

        EmailSenderService emailSenderService = new EmailSenderService();
        emailSenderService.addEmailClient(sendGridEmailClient);

        Assert.assertEquals(emailSenderService.send(TestUtils.dummyEmailMessage()), false);
    }

    @Test
    public void GivenEmailSenderServiceWithANotAvailableClientAndAnAvailableClient_WhenSendEmail_ThenReturnsTrue() throws Exception {
        SendGridEmailClient sendGridEmailClient = mock(SendGridEmailClient.class);
        doThrow(EmailClientNotAvailableException.class)
                .when(sendGridEmailClient)
                .sendEmail(any(EmailMessage.class));

        MailjetEmailClient mailjetEmailClient = mock(MailjetEmailClient.class);

        EmailSenderService emailSenderService = new EmailSenderService();

        emailSenderService.addEmailClient(sendGridEmailClient);
        emailSenderService.addEmailClient(mailjetEmailClient);

        Assert.assertEquals(emailSenderService.send(TestUtils.dummyEmailMessage()), true);
    }

    @Test
    public void GivenEmailSenderServiceWithTwoNotAvailableClients_WhenSendEmail_ThenReturnsFalse() throws Exception {
        SendGridEmailClient sendGridEmailClient = mock(SendGridEmailClient.class);
        doThrow(EmailClientNotAvailableException.class)
                .when(sendGridEmailClient)
                .sendEmail(any(EmailMessage.class));

        MailjetEmailClient mailjetEmailClient = mock(MailjetEmailClient.class);
        doThrow(EmailClientNotAvailableException.class)
                .when(mailjetEmailClient)
                .sendEmail(any(EmailMessage.class));

        EmailSenderService emailSenderService = new EmailSenderService();

        emailSenderService.addEmailClient(sendGridEmailClient);
        emailSenderService.addEmailClient(mailjetEmailClient);

        Assert.assertEquals(emailSenderService.send(TestUtils.dummyEmailMessage()), false);
    }


    @Test
    public void GivenEmailSenderServiceWithAnAvailableClientAndANotAvailableClient_WhenSendEmail_ThenReturnsTrue() throws Exception {
        SendGridEmailClient sendGridEmailClient = mock(SendGridEmailClient.class);

        MailjetEmailClient mailjetEmailClient = mock(MailjetEmailClient.class);
        doThrow(EmailClientNotAvailableException.class)
                .when(mailjetEmailClient)
                .sendEmail(any(EmailMessage.class));

        EmailSenderService emailSenderService = new EmailSenderService();

        emailSenderService.addEmailClient(sendGridEmailClient);
        emailSenderService.addEmailClient(mailjetEmailClient);

        Assert.assertEquals(emailSenderService.send(TestUtils.dummyEmailMessage()), true);
    }
}
