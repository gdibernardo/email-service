package com.gdibernardo.emailservice.email;

import java.util.regex.Pattern;

public final class EmailValidator {

    private EmailValidator() {}

    public static boolean isValid(String email) {
        if(email == null) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}
