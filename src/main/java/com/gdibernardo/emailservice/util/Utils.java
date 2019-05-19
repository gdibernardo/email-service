package com.gdibernardo.emailservice.util;

import java.util.Base64;

public final class Utils {
    private Utils() {}

    public static String decodeBase64(String encoded) {
        return new String(Base64.getDecoder().decode(encoded));
    }
}
