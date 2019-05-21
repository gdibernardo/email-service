package com.gdibernardo.emailservice.email;

import org.junit.Assert;
import org.junit.Test;

public class EmailValidatorTest {

    @Test
    public void Given_ValidEmail_WhenValidated_IsValid() {
        /*Given a correct email */
        String email = "correct@correctaddress.com";

        Assert.assertEquals(EmailValidator.isValid(email), true);
    }

    @Test
    public void Given_EmptyEmail_WhenValidated_IsInvalid() {
        String emptyEmail = new String();

        Assert.assertEquals(EmailValidator.isValid(emptyEmail), false);
    }

    @Test
    public void Given_NullEmail_WhenValidated_IsInvalid() {
        String nullEmail = null;

        Assert.assertEquals(EmailValidator.isValid(nullEmail), false);
    }

    @Test
    public void Given_InvalidEmails_WhenValidated_AreInvalid() {
        String invalidEmail =  "I am and invalid email";
        Assert.assertEquals(EmailValidator.isValid(invalidEmail), false);

        invalidEmail =  "invalid@";
        Assert.assertEquals(EmailValidator.isValid(invalidEmail), false);

        invalidEmail =  "invalid@invalid";
        Assert.assertEquals(EmailValidator.isValid(invalidEmail), false);

        invalidEmail =  "@stillinvalid";
        Assert.assertEquals(EmailValidator.isValid(invalidEmail), false);

        invalidEmail =  "@moreinvalid.com";
        Assert.assertEquals(EmailValidator.isValid(invalidEmail), false);
    }
}
