package com.gdibernardo.emailservice.email;

import com.gdibernardo.emailservice.TestUtils;
import com.gdibernardo.emailservice.email.repository.model.DatastoreEmail;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class DatastoreEmailTest {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void GivenEmail_WhenDatastoreEntityIsCreated_ThenIsCorrect() {
        Email email = TestUtils.email(0,
                "joey@gmail.com",
                "Joey Tribbiani",
                "mary@gmail.com",
                "Mary Johnson",
                "How you doin?",
                "How you doin?");

        Entity entity = new DatastoreEmail(email).getEntity();


        Assert.assertEquals(entity.getKey().getId(), 0);

        Assert.assertEquals(entity.getProperty("fromEmail"), email.getFrom().getAddress());
        Assert.assertEquals(entity.getProperty("fromName"), email.getFrom().getName());
        Assert.assertEquals(entity.getProperty("toEmail"), email.getTo().getAddress());
        Assert.assertEquals(entity.getProperty("toName"), email.getTo().getName());
        Assert.assertEquals(entity.getProperty("content"), email.getContent());
        Assert.assertEquals(entity.getProperty("status"), email.getStatus().name());
    }

    @Test
    public void GivenEntity_WhenDatastoreIsCreated_ThenIsCorrect() {
        Entity entity = new Entity("EMAIL_TYPE");

        entity.setProperty("fromName", "Joey");
        entity.setProperty("fromEmail", "joey@gmail.com");
        entity.setProperty("toName", "Mary");
        entity.setProperty("toEmail", "mary@gmail.com");
        entity.setProperty("subject", "How you doin?");
        entity.setProperty("content", "Hello, how you doin?");
        entity.setProperty("status", "SENT");

        Date date = new Date();
        entity.setUnindexedProperty("modifiedAt", date);

        DatastoreEmail datastoreEmail = DatastoreEmail.fromEntity(entity);

        Email email = datastoreEmail.getEmail();

        Assert.assertEquals(entity.getProperty("fromEmail"), email.getFrom().getAddress());
        Assert.assertEquals(entity.getProperty("fromName"), email.getFrom().getName());
        Assert.assertEquals(entity.getProperty("toEmail"), email.getTo().getAddress());
        Assert.assertEquals(entity.getProperty("toName"), email.getTo().getName());
        Assert.assertEquals(entity.getProperty("content"), email.getContent());
        Assert.assertEquals(entity.getProperty("status"), email.getStatus().name());

        Assert.assertEquals(datastoreEmail.getModifiedAt().toString(), date.toString());
    }
}
