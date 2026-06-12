package com.warehouse.auth.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.servlet.http.HttpServletRequest;

@ConfigurationProperties(prefix = "api.exposure")
public class ApiExposureProperties {

    private String internalPrefix = "/internal";

    private List<String> internalControllerPaths = new ArrayList<>(List.of(
            "/device-pairings",
            "/device-pairing-verifications",
            "/shipments/read-sync"
    ));

    private List<String> pairKeyControllerPaths = new ArrayList<>(List.of(
            "/device-pairing-verifications"
    ));

    private List<PublicEndpoint> publicEndpoints = new ArrayList<>(List.of(
            new PublicEndpoint("POST", "/device-pairings")
    ));

    public String getInternalPrefix() {
        return internalPrefix;
    }

    public void setInternalPrefix(final String internalPrefix) {
        this.internalPrefix = internalPrefix;
    }

    public List<String> getInternalControllerPaths() {
        return internalControllerPaths;
    }

    public void setInternalControllerPaths(final List<String> internalControllerPaths) {
        this.internalControllerPaths = internalControllerPaths;
    }

    public List<String> getPairKeyControllerPaths() {
        return pairKeyControllerPaths;
    }

    public void setPairKeyControllerPaths(final List<String> pairKeyControllerPaths) {
        this.pairKeyControllerPaths = pairKeyControllerPaths;
    }

    public List<PublicEndpoint> getPublicEndpoints() {
        return publicEndpoints;
    }

    public void setPublicEndpoints(final List<PublicEndpoint> publicEndpoints) {
        this.publicEndpoints = publicEndpoints;
    }

    public boolean isInternalControllerPath(final String path) {
        final String normalizedPath = normalizePath(path);
        return internalControllerPaths.stream()
                .map(this::internalPath)
                .anyMatch(normalizedPath::startsWith);
    }

    public boolean isPairKeyController(final HttpServletRequest request) {
        final String normalizedPath = requestPathWithoutContext(request);
        return pairKeyControllerPaths.stream()
                .map(this::internalPath)
                .anyMatch(normalizedPath::startsWith);
    }

    public boolean isPublicEndpoint(final HttpServletRequest request) {
        final String normalizedPath = requestPathWithoutContext(request);
        final String method = request.getMethod();
        return publicEndpoints.stream()
                .anyMatch(endpoint -> endpoint.matches(method, normalizedPath, internalPath(endpoint.getPath())));
    }

    public String[] pairKeySecurityMatchers(final String contextPath) {
        return pairKeyControllerPaths.stream()
                .map(this::internalPath)
                .map(path -> pathWithContext(contextPath, path) + "/**")
                .toArray(String[]::new);
    }

    private String requestPathWithoutContext(final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isBlank() && uri.startsWith(contextPath)) {
            return normalizePath(uri.substring(contextPath.length()));
        }
        return normalizePath(uri);
    }

    private String internalPath(final String path) {
        final String normalizedPath = normalizePath(path);
        final String normalizedInternalPrefix = normalizePath(internalPrefix);
        if (normalizedPath.equals(normalizedInternalPrefix)
                || normalizedPath.startsWith(normalizedInternalPrefix + "/")) {
            return normalizedPath;
        }
        if ("/".equals(normalizedInternalPrefix)) {
            return normalizedPath;
        }
        return normalizedInternalPrefix + normalizedPath;
    }

    private String pathWithContext(final String contextPath, final String path) {
        final String normalizedPath = normalizePath(path);
        final String normalizedContextPath = normalizePath(contextPath);
        if ("/".equals(normalizedContextPath)) {
            return normalizedPath;
        }
        if (normalizedPath.equals(normalizedContextPath)
                || normalizedPath.startsWith(normalizedContextPath + "/")) {
            return normalizedPath;
        }
        return normalizedContextPath + normalizedPath;
    }

    private String normalizePath(final String path) {
        if (path == null || path.isBlank()) {
            return "/";
        }
        final String normalizedPath = path.startsWith("/") ? path : "/" + path;
        if (normalizedPath.length() > 1 && normalizedPath.endsWith("/")) {
            return normalizedPath.substring(0, normalizedPath.length() - 1);
        }
        return normalizedPath;
    }

    public static class PublicEndpoint {

        private String method;

        private String path;

        public PublicEndpoint() {
        }

        public PublicEndpoint(final String method, final String path) {
            this.method = method;
            this.path = path;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(final String method) {
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public void setPath(final String path) {
            this.path = path;
        }

        public boolean matches(final String requestMethod, final String requestPath, final String expectedPath) {
            return method != null
                    && expectedPath != null
                    && method.equalsIgnoreCase(requestMethod)
                    && expectedPath.equals(requestPath);
        }
    }
}
