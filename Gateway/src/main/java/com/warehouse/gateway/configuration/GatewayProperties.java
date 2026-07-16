package com.warehouse.gateway.configuration;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "warehouse.gateway")
public class GatewayProperties {

    @Valid
    @NotEmpty
    private List<Route> routes = new ArrayList<>();

    @Valid
    @NotNull
    private Cors cors = new Cors();

    public Optional<Route> findRouteByPathPrefix(final String pathPrefix) {
        return this.routes.stream()
                .filter(route -> route.getPathPrefix().equals(pathPrefix))
                .findFirst();
    }

    public List<Route> getRoutes() {
        return this.routes;
    }

    public void setRoutes(final List<Route> routes) {
        this.routes = routes;
    }

    public Cors getCors() {
        return this.cors;
    }

    public void setCors(final Cors cors) {
        this.cors = cors;
    }

    public static class Route {

        @NotBlank
        private String id;

        @NotBlank
        private String pathPrefix;

        @NotNull
        private URI uri;

        @NotBlank
        private String healthPath = "/actuator/health";

        private boolean healthCheckEnabled = true;

        @Min(1)
        private int healthTimeoutSeconds = 3;

        public String getId() {
            return this.id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        public String getPathPrefix() {
            return this.pathPrefix;
        }

        public void setPathPrefix(final String pathPrefix) {
            this.pathPrefix = pathPrefix;
        }

        public URI getUri() {
            return this.uri;
        }

        public void setUri(final URI uri) {
            this.uri = uri;
        }

        public String getHealthPath() {
            return this.healthPath;
        }

        public void setHealthPath(final String healthPath) {
            this.healthPath = healthPath;
        }

        public boolean isHealthCheckEnabled() {
            return this.healthCheckEnabled;
        }

        public void setHealthCheckEnabled(final boolean healthCheckEnabled) {
            this.healthCheckEnabled = healthCheckEnabled;
        }

        public int getHealthTimeoutSeconds() {
            return this.healthTimeoutSeconds;
        }

        public void setHealthTimeoutSeconds(final int healthTimeoutSeconds) {
            this.healthTimeoutSeconds = healthTimeoutSeconds;
        }
    }

    public static class Cors {

        private List<String> allowedOrigins = List.of("http://localhost:3000", "http://localhost:3001");

        private List<String> allowedMethods = List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");

        private List<String> allowedHeaders = List.of("Content-Type", "X-XSRF-TOKEN", "X-Correlation-ID", "X-Requested-With");

        private List<String> exposedHeaders = List.of("X-Correlation-ID");

        @Min(0)
        private long maxAgeSeconds = 3600;

        public List<String> getAllowedOrigins() {
            return this.allowedOrigins;
        }

        public void setAllowedOrigins(final List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public List<String> getAllowedMethods() {
            return this.allowedMethods;
        }

        public void setAllowedMethods(final List<String> allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public List<String> getAllowedHeaders() {
            return this.allowedHeaders;
        }

        public void setAllowedHeaders(final List<String> allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public List<String> getExposedHeaders() {
            return this.exposedHeaders;
        }

        public void setExposedHeaders(final List<String> exposedHeaders) {
            this.exposedHeaders = exposedHeaders;
        }

        public long getMaxAgeSeconds() {
            return this.maxAgeSeconds;
        }

        public void setMaxAgeSeconds(final long maxAgeSeconds) {
            this.maxAgeSeconds = maxAgeSeconds;
        }
    }
}
