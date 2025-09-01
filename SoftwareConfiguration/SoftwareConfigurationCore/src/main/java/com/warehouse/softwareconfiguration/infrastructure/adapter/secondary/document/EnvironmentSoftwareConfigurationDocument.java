package com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document.config.Config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "environmentSoftwareConfiguration")
public class EnvironmentSoftwareConfigurationDocument {

    private String environmentId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String apiBaseUrl;
    private String authEndpoint;
    private String shipmentEndpoint;
    private String supplierEndpoint;
    private String departmentEndpoint;
    private String userEndpoint;
    private String reportEndpoint;
    private String notificationEndpoint;

    private Map<String, String> apiKeys;
    private List<Config.OAuthClientConfig> oauthClients;
    private List<Config.ExternalServiceConfig> externalServices;

    private String defaultLanguage;
    private String defaultTimezone;
    private String defaultDateFormat;
    private String defaultTimeFormat;
    private String loggingLevel;
    private int maxConcurrentRequests;
    private Config.CacheSettings cacheSettings;
    private Map<String, Boolean> featureFlags;
    private boolean maintenanceMode;

    private String notificationServiceApiKey;
    private Config.EmailServerConfig emailServerConfig;
    private Config.SmsProviderConfig smsProviderConfig;
    private List<String> alertEmails;

    private Set<String> allowedIpRanges;
    private String jwtSecret;
    private Map<String, String> encryptionKeys;
    private boolean twoFactorAuthRequired;
    private Config.PasswordPolicy passwordPolicy;

    private int maxUploadSize;
    private String storageEndpoint;
    private String storageApiKey;
    private Config.BackupSettings backupSettings;
    private String monitoringEndpoint;
    private String monitoringApiKey;
}
