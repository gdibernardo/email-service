package com.gdibernardo.emailservice.pubsub;

import com.gdibernardo.emailservice.TestUtils;
import com.gdibernardo.emailservice.email.model.EmailMessage;
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
    public void ShouldEmailMessage_Published_Correctly() throws Exception {
        Publisher publisher = mock(Publisher.class);

        EmailMessagePublisherService emailMessagePublisherService = new EmailMessagePublisherService(publisher,
                "systemsender@me.io");

        ApiFuture<String> apiFuture = mock(ApiFuture.class);
        when(apiFuture.get()).thenReturn("message-id-00");

        when(publisher.publish(any(PubsubMessage.class))).thenReturn(apiFuture);

        EmailMessage emailMessage = TestUtils.dummyEmailMessage();

        Assert.assertEquals(emailMessagePublisherService.publish(emailMessage), true);
        verify(publisher, times(1)).publish(eq(emailMessage.toPubSubMessage()));
    }

    @Test
    public void Given_FailingPublisher_WhenPublishing_ReturnFalse() throws Exception {
        Publisher publisher = mock(Publisher.class);

        EmailMessagePublisherService emailMessagePublisherService = new EmailMessagePublisherService(publisher,
                "systemsender@me.io");

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
                "systemsender@me.io");

        ApiFuture<String> apiFuture = mock(ApiFuture.class);
        when(apiFuture.get()).thenReturn("message-id-00");

        when(publisher.publish(any(PubsubMessage.class))).thenReturn(apiFuture);

        EmailMessage emailMessage = TestUtils.dummyEmailMessage();

        Assert.assertEquals(emailMessagePublisherService.publish(emailMessage), true);

        ArgumentCaptor<PubsubMessage> argumentCaptor = ArgumentCaptor.forClass(PubsubMessage.class);
        verify(publisher, times(1)).publish(argumentCaptor.capture());

        EmailMessage emailMessage1 = EmailMessage.fromJSONString(argumentCaptor.getValue().getData().toStringUtf8());

        Assert.assertEquals(emailMessage1.getFrom().getAddress(), "systemsender@me.io");
    }

    @Test
    public void EmailMessagePublisherService_ShouldSetFromFieldName() throws Exception {
        Publisher publisher = mock(Publisher.class);

        EmailMessagePublisherService emailMessagePublisherService = new EmailMessagePublisherService(publisher,
                "systemsender@me.io");

        ApiFuture<String> apiFuture = mock(ApiFuture.class);
        when(apiFuture.get()).thenReturn("message-id-00");

        when(publisher.publish(any(PubsubMessage.class))).thenReturn(apiFuture);

        EmailMessage emailMessage = TestUtils.dummyEmailMessage();

        Assert.assertEquals(emailMessagePublisherService.publish(emailMessage), true);

        ArgumentCaptor<PubsubMessage> argumentCaptor = ArgumentCaptor.forClass(PubsubMessage.class);
        verify(publisher, times(1)).publish(argumentCaptor.capture());

        EmailMessage emailMessage1 = EmailMessage.fromJSONString(argumentCaptor.getValue().getData().toStringUtf8());

        Assert.assertEquals(emailMessage1.getFrom().getName(), emailMessage.getFrom().getName());
    }

    @Test
    public void EmailMessagePublisherService_ShouldNotSetFromFieldName() throws Exception {
        Publisher publisher = mock(Publisher.class);

        EmailMessagePublisherService emailMessagePublisherService = new EmailMessagePublisherService(publisher,
                "systemsender@me.io");

        ApiFuture<String> apiFuture = mock(ApiFuture.class);
        when(apiFuture.get()).thenReturn("message-id-00");

        when(publisher.publish(any(PubsubMessage.class))).thenReturn(apiFuture);

        EmailMessage emailMessage = TestUtils.emailMessage("its@me.com",
                null,
                "you@you.com",
                null,
                "Your flight to Geneva",
                "Time for check-in.");

        Assert.assertEquals(emailMessagePublisherService.publish(emailMessage), true);

        ArgumentCaptor<PubsubMessage> argumentCaptor = ArgumentCaptor.forClass(PubsubMessage.class);
        verify(publisher, times(1)).publish(argumentCaptor.capture());

        EmailMessage emailMessage1 = EmailMessage.fromJSONString(argumentCaptor.getValue().getData().toStringUtf8());

        Assert.assertEquals(emailMessage1.getFrom().getName(), "");
    }
}
