package com.gdibernardo.emailservice.pubsub;

import com.gdibernardo.emailservice.TestUtils;
import com.gdibernardo.emailservice.email.Email;
import com.gdibernardo.emailservice.pubsub.publisher.EmailMessagePublisherService;
import com.google.cloud.pubsub.v1.Publisher;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EmailPublisherServiceTest {

    @Test
    public void EmailMessage_ShouldBePublished_Correctly() throws Exception {
        Publisher publisher = mock(Publisher.class);

        EmailMessagePublisherService emailMessagePublisherService = new EmailMessagePublisherService(publisher);

        Email email = TestUtils.dummyEmailMessage();

        emailMessagePublisherService.publish(email.emailMessage());
        verify(publisher, times(1)).publish(eq(email.emailMessage().toPubSubMessage()));
    }
}
