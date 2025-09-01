package com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document.config;

import java.util.Set;

public class Config {

    public static class OAuthClientConfig {
        private String clientId;
        private String clientSecret;
        private Set<String> scopes;
    }

    public static class ExternalServiceConfig {
        private String serviceName;
        private String endpoint;
    }

    public static class CacheSettings {
        private int ttlMinutes;
        private boolean enabled;
    }

    public static class EmailServerConfig {
        private String host;
        private int port;
        private String username;
        private String password;
    }

    public static class SmsProviderConfig {
        private String endpoint;
        private String apiKey;
    }

    public static class PasswordPolicy {
        private int minLength;
        private boolean requireUppercase;
        private boolean requireSpecialChar;
        private int expirationDays;
    }

    public static class BackupSettings {
        private String cronSchedule;
        private String backupLocation;
    }
}
