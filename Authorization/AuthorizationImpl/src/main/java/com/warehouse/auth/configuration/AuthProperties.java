package com.warehouse.auth.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("auth")
public class AuthProperties {

    private Cookie cookie = new Cookie();

    private Cors cors = new Cors();

    @Data
    public static class Cookie {
        private String accessName = "AUTH-TOKEN";
        private String refreshName = "REFRESH-TOKEN";
        private String accessPath = "/";
        private String refreshPath = "/v2/api/auth";
        private String domain;
        private boolean secure;
        private String sameSite = "Lax";
    }

    @Data
    public static class Cors {
        private List<String> allowedOrigins = List.of("http://localhost:3000");
        private List<String> allowedMethods = List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
        private List<String> allowedHeaders = List.of("Content-Type", "X-XSRF-TOKEN", "X-Correlation-ID", "X-Requested-With");
        private List<String> exposedHeaders = List.of("X-Correlation-ID");
        private long maxAgeSeconds = 3600;
    }
}
