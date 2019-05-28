package com.gdibernardo.emailservice.email;

import org.junit.Assert;
import org.junit.Test;

public class EmailValidatorTest {

    @Test
    public void GivenValidEmail_WhenValidated_ThenIsValid() {
        String email = "correct@correctaddress.com";

        Assert.assertEquals(EmailValidator.isValid(email), true);
    }

    @Test
    public void GivenEmptyEmail_WhenValidated_ThenIsInvalid() {
        String emptyEmail = new String();
        Assert.assertEquals(EmailValidator.isValid(emptyEmail), false);


        emptyEmail =  "    ";
        Assert.assertEquals(EmailValidator.isValid(emptyEmail), false);
    }

    @Test
    public void GivenNullEmail_WhenValidated_ThenIsInvalid() {
        String nullEmail = null;

        Assert.assertEquals(EmailValidator.isValid(nullEmail), false);
    }

    @Test
    public void GivenInvalidEmails_WhenValidated_ThenAreInvalid() {
        String invalidEmail =  "I am and invalid email";
        Assert.assertEquals(EmailValidator.isValid(invalidEmail), false);

        invalidEmail =  "@";
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
