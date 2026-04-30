package com.khait_academy.backend.security;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.SignatureAlgorithm;

public class GenKey {
    public static void main(String[] args) {
        String key = Encoders.BASE64.encode(
            Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded()
        );
        System.out.println(key);
    }
}