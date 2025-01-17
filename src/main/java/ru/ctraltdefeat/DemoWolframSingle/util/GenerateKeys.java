package ru.ctraltdefeat.DemoWolframSingle.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

public class GenerateKeys {

    private static String generateKey() {
        return Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
    }

}
