package com.gdibernardo.emailservice.pubsub;

import com.gdibernardo.emailservice.TestUtils;
import com.gdibernardo.emailservice.email.EmailMessage;
import com.gdibernardo.emailservice.pubsub.publisher.EmailMessagePublisherService;
import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.PubsubMessage;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;

public class EmailMessagePublisherServiceTest {

    @Test
    public void EmailMessage_ShouldBePublished_Correctly() throws Exception {
        Publisher publisher = mock(Publisher.class);

        EmailMessagePublisherService emailMessagePublisherService = new EmailMessagePublisherService(publisher,
                "systemsender@sender.io");

        ApiFuture<String> apiFuture = mock(ApiFuture.class);
        when(apiFuture.get()).thenReturn("message-id-00");

        when(publisher.publish(any(PubsubMessage.class))).thenReturn(apiFuture);

        EmailMessage emailMessage = TestUtils.dummyEmailMessage();

        Assert.assertEquals(emailMessagePublisherService.publish(emailMessage), true);
        verify(publisher, times(1)).publish(eq(emailMessage.toPubSubMessage()));
    }

    @Test
    public void GivenFailingPublisher_WhenPublishing_ReturnFalse() throws Exception {
        Publisher publisher = mock(Publisher.class);

        EmailMessagePublisherService emailMessagePublisherService = new EmailMessagePublisherService(publisher,
                "systemsender@sender.io");

        ApiFuture<String> apiFuture = mock(ApiFuture.class);
        doThrow(ExecutionException.class)
                .when(apiFuture)
                .get();
        when(publisher.publish(any(PubsubMessage.class))).thenReturn(apiFuture);

        EmailMessage emailMessage = TestUtils.dummyEmailMessage();

        Assert.assertEquals(emailMessagePublisherService.publish(emailMessage), false);
        verify(publisher, times(1)).publish(eq(emailMessage.toPubSubMessage()));
    }

    @Test
    public void EmailMessagePublisherService_ShouldSetFromFieldAddress_ToSystemSenderEmail() throws Exception {
        Publisher publisher = mock(Publisher.class);

        EmailMessagePublisherService emailMessagePublisherService = new EmailMessagePublisherService(publisher,
                "systemsender@sender.io");

        ApiFuture<String> apiFuture = mock(ApiFuture.class);
        when(apiFuture.get()).thenReturn("message-id-00");

        when(publisher.publish(any(PubsubMessage.class))).thenReturn(apiFuture);

        EmailMessage emailMessage = TestUtils.dummyEmailMessage();

        Assert.assertEquals(emailMessagePublisherService.publish(emailMessage), true);

        ArgumentCaptor<PubsubMessage> argumentCaptor = ArgumentCaptor.forClass(PubsubMessage.class);
        verify(publisher, times(1)).publish(argumentCaptor.capture());

        EmailMessage emailMessageFromArg = EmailMessage.fromJSONString(argumentCaptor.getValue().getData().toStringUtf8());

        Assert.assertEquals(emailMessageFromArg.getFrom().getAddress(), "systemsender@sender.io");
    }

    @Test
    public void EmailMessagePublisherService_ShouldSetFromFieldName_WhenSet() throws Exception {
        Publisher publisher = mock(Publisher.class);

        EmailMessagePublisherService emailMessagePublisherService = new EmailMessagePublisherService(publisher,
                "systemsender@sender.io");

        ApiFuture<String> apiFuture = mock(ApiFuture.class);
        when(apiFuture.get()).thenReturn("message-id-00");

        when(publisher.publish(any(PubsubMessage.class))).thenReturn(apiFuture);

        EmailMessage emailMessage = TestUtils.dummyEmailMessage();

        Assert.assertEquals(emailMessagePublisherService.publish(emailMessage), true);

        ArgumentCaptor<PubsubMessage> argumentCaptor = ArgumentCaptor.forClass(PubsubMessage.class);
        verify(publisher, times(1)).publish(argumentCaptor.capture());

        EmailMessage emailMessageFromArg = EmailMessage.fromJSONString(argumentCaptor.getValue().getData().toStringUtf8());

        Assert.assertEquals(emailMessageFromArg.getFrom().getName(), emailMessage.getFrom().getName());
    }

    @Test
    public void EmailMessagePublisherService_ShouldNotSetFromFieldName_WhenNotSet() throws Exception {
        Publisher publisher = mock(Publisher.class);

        EmailMessagePublisherService emailMessagePublisherService = new EmailMessagePublisherService(publisher,
                "systemsender@sender.io");

        ApiFuture<String> apiFuture = mock(ApiFuture.class);
        when(apiFuture.get()).thenReturn("message-id-00");

        when(publisher.publish(any(PubsubMessage.class))).thenReturn(apiFuture);

        EmailMessage emailMessage = TestUtils.emailMessage("its@me.com",
                null,
                "you@you.com",
                null,
                "Your flight to Geneva!",
                "Time for check-in.");

        Assert.assertEquals(emailMessagePublisherService.publish(emailMessage), true);

        ArgumentCaptor<PubsubMessage> argumentCaptor = ArgumentCaptor.forClass(PubsubMessage.class);
        verify(publisher, times(1)).publish(argumentCaptor.capture());

        EmailMessage emailMessageFromArg = EmailMessage.fromJSONString(argumentCaptor.getValue().getData().toStringUtf8());

        Assert.assertEquals(emailMessageFromArg.getFrom().getName(), "");
    }
}
