package com.gdibernardo.emailservice.email.model;

import org.junit.Assert;
import org.junit.Test;

public class EmailAddressTest {

    @Test
    public void GivenEmailAddressWithName_WhenIsSetNameCalled_ReturnTrue() {
        EmailAddress emailAddress = new EmailAddress("jeff@amazon.com", "Jeff");

        Assert.assertEquals(emailAddress.hasName(), true);
    }

    @Test
    public void GivenEmailAddressWithEmptyName_WhenIsSetNameCalled_ReturnFalse() {
        EmailAddress emailAddress = new EmailAddress("empty@emoty.com", "      ");

        Assert.assertEquals(emailAddress.hasName(), false);
    }

    @Test
    public void GivenEmailAddressWithNullName_WhenIsSetNameCalled_ReturnFalse() {
        EmailAddress emailAddress = new EmailAddress("null@null.com",null);

        Assert.assertEquals(emailAddress.hasName(), false);
    }
}
