package com.warehouse.commonassets.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CredentialSecurityConfiguration {

    @Bean
    public CredentialCipher credentialCipher(
            @Value("${credentials.encryption-key:}") final String encryptionKey) {
        return new CredentialCipher(encryptionKey);
    }
}
