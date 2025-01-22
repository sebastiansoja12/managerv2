package com.warehouse.returntoken.domain.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class RandomSaltGenerator {

    static String generateSecureRandomSalt() {
        final SecureRandom random = new SecureRandom();
        final byte[] saltBytes = new byte[8];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes).replaceAll("[^a-zA-Z0-9]", "");
    }

}
