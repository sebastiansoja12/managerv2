package com.warehouse.gateway.infrastructure.adapter.primary;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import com.warehouse.gateway.configuration.GatewayProperties;
import com.warehouse.gateway.infrastructure.filter.CorrelationIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class GatewayProxyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayProxyController.class);
    private static final Set<String> HOP_BY_HOP_HEADERS = Set.of(
            "connection",
            "content-length",
            "host",
            "keep-alive",
            "proxy-authenticate",
            "proxy-authorization",
            "te",
            "trailer",
            "transfer-encoding",
            "upgrade"
    );
    private static final Set<String> GATEWAY_CONTROLLED_HEADERS = Set.of(
            "x-forwarded-for",
            "x-forwarded-host",
            "x-forwarded-prefix",
            "x-forwarded-proto"
    );

    private final GatewayProperties gatewayProperties;
    private final HttpClient httpClient;

    public GatewayProxyController(final GatewayProperties gatewayProperties,
                                  final HttpClient httpClient) {
        this.gatewayProperties = gatewayProperties;
        this.httpClient = httpClient;
    }

    @GetMapping("/health")
    public GatewayHealthResponse gatewayHealth() {
        return new GatewayHealthResponse("UP", this.gatewayProperties.getRoutes().size());
    }

    @GetMapping("/routes")
    public List<GatewayRouteResponse> routes() {
        return this.gatewayProperties.getRoutes().stream()
                .map(route -> new GatewayRouteResponse(route.getId(), route.getPathPrefix(), route.getUri().toString(), route.isHealthCheckEnabled()))
                .toList();
    }

    @GetMapping("/services/health")
    public GatewayServicesHealthResponse servicesHealth(@RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) final String authorizationHeader,
                                                        @RequestHeader(name = CorrelationIdFilter.CORRELATION_ID_HEADER, required = false) final String correlationId) {
        String requestCorrelationId = StringUtils.hasText(correlationId) ? correlationId : MDC.get(CorrelationIdFilter.MDC_KEY);
        List<GatewayServiceHealthResponse> services = this.gatewayProperties.getRoutes().stream()
                .map(route -> checkServiceHealth(route, authorizationHeader, requestCorrelationId))
                .toList();

        String status = services.stream().allMatch(service -> "UP".equals(service.status())) ? "UP" : "DEGRADED";
        return new GatewayServicesHealthResponse(status, services);
    }

    @RequestMapping(
            value = "/{service}/**",
            method = {
                    RequestMethod.GET,
                    RequestMethod.POST,
                    RequestMethod.PUT,
                    RequestMethod.PATCH,
                    RequestMethod.DELETE,
                    RequestMethod.OPTIONS,
                    RequestMethod.HEAD
            }
    )
    public ResponseEntity<byte[]> proxy(final HttpServletRequest request,
                                        @PathVariable final String service,
                                        @RequestBody(required = false) final byte[] body) {
        Optional<GatewayProperties.Route> route = this.gatewayProperties.findRouteByPathPrefix(service);
        if (route.isEmpty()) {
            return textResponse(HttpStatus.NOT_FOUND, "Gateway route not found: " + service);
        }

        URI targetUri = buildTargetUri(route.get(), request, service);
        HttpRequest proxyRequest = buildProxyRequest(request, targetUri, service, body);

        try {
            HttpResponse<byte[]> proxyResponse = this.httpClient.send(proxyRequest, HttpResponse.BodyHandlers.ofByteArray());
            return buildProxyResponse(proxyResponse);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Gateway request interrupted for route {}", route.get().getId());
            return textResponse(HttpStatus.BAD_GATEWAY, "Gateway request was interrupted");
        } catch (IOException exception) {
            LOGGER.warn("Gateway could not reach route {}: {}", route.get().getId(), exception.getMessage());
            return textResponse(HttpStatus.BAD_GATEWAY, "Gateway target is not reachable: " + route.get().getId());
        }
    }

    private GatewayServiceHealthResponse checkServiceHealth(final GatewayProperties.Route route,
                                                            final String authorizationHeader,
                                                            final String correlationId) {
        if (!route.isHealthCheckEnabled()) {
            return new GatewayServiceHealthResponse(route.getId(), route.getPathPrefix(), "DISABLED", null, 0);
        }

        URI healthUri = buildTargetUri(route.getUri(), route.getHealthPath(), null);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(healthUri)
                .timeout(Duration.ofSeconds(route.getHealthTimeoutSeconds()))
                .GET();

        if (StringUtils.hasText(authorizationHeader)) {
            requestBuilder.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
        }
        if (StringUtils.hasText(correlationId)) {
            requestBuilder.header(CorrelationIdFilter.CORRELATION_ID_HEADER, correlationId);
        }

        Instant startedAt = Instant.now();
        try {
            HttpResponse<byte[]> response = this.httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());
            long responseTimeMillis = Duration.between(startedAt, Instant.now()).toMillis();
            String status = response.statusCode() >= 200 && response.statusCode() < 400 ? "UP" : "DOWN";
            return new GatewayServiceHealthResponse(route.getId(), route.getPathPrefix(), status, response.statusCode(), responseTimeMillis);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            long responseTimeMillis = Duration.between(startedAt, Instant.now()).toMillis();
            LOGGER.warn("Gateway service health check interrupted for route {}", route.getId());
            return new GatewayServiceHealthResponse(route.getId(), route.getPathPrefix(), "DOWN", null, responseTimeMillis);
        } catch (IOException exception) {
            long responseTimeMillis = Duration.between(startedAt, Instant.now()).toMillis();
            LOGGER.warn("Gateway service health check failed for route {}: {}", route.getId(), exception.getMessage());
            return new GatewayServiceHealthResponse(route.getId(), route.getPathPrefix(), "DOWN", null, responseTimeMillis);
        }
    }

    private HttpRequest buildProxyRequest(final HttpServletRequest request,
                                          final URI targetUri,
                                          final String service,
                                          final byte[] body) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(targetUri)
                .method(request.getMethod(), bodyPublisher(body));

        Collections.list(request.getHeaderNames()).stream()
                .filter(headerName -> !isHopByHopHeader(headerName))
                .filter(headerName -> !isGatewayControlledHeader(headerName))
                .forEach(headerName -> Collections.list(request.getHeaders(headerName))
                        .forEach(headerValue -> requestBuilder.header(headerName, headerValue)));

        addForwardedHeaders(requestBuilder, request, service);
        return requestBuilder.build();
    }

    private void addForwardedHeaders(final HttpRequest.Builder requestBuilder,
                                     final HttpServletRequest request,
                                     final String service) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        String remoteAddress = request.getRemoteAddr();
        String forwardedForValue = StringUtils.hasText(forwardedFor) ? forwardedFor + ", " + remoteAddress : remoteAddress;
        String forwardedHost = StringUtils.hasText(request.getHeader(HttpHeaders.HOST))
                ? request.getHeader(HttpHeaders.HOST)
                : request.getServerName();

        requestBuilder.header("X-Forwarded-For", forwardedForValue);
        requestBuilder.header("X-Forwarded-Host", forwardedHost);
        requestBuilder.header("X-Forwarded-Proto", request.getScheme());
        requestBuilder.header("X-Forwarded-Prefix", "/gateway/" + service);
    }

    private HttpRequest.BodyPublisher bodyPublisher(final byte[] body) {
        if (body == null || body.length == 0) {
            return HttpRequest.BodyPublishers.noBody();
        }
        return HttpRequest.BodyPublishers.ofByteArray(body);
    }

    private ResponseEntity<byte[]> buildProxyResponse(final HttpResponse<byte[]> proxyResponse) {
        HttpHeaders responseHeaders = new HttpHeaders();
        proxyResponse.headers().map().forEach((headerName, headerValues) -> {
            if (!isHopByHopHeader(headerName)) {
                headerValues.forEach(headerValue -> responseHeaders.add(headerName, headerValue));
            }
        });

        return ResponseEntity.status(proxyResponse.statusCode())
                .headers(responseHeaders)
                .body(proxyResponse.body());
    }

    private URI buildTargetUri(final GatewayProperties.Route route,
                               final HttpServletRequest request,
                               final String service) {
        String requestUri = request.getRequestURI();
        String gatewayRoutePrefix = request.getContextPath() + "/gateway/" + service;
        String downstreamPath = requestUri.substring(gatewayRoutePrefix.length());
        return buildTargetUri(route.getUri(), downstreamPath, request.getQueryString());
    }

    private URI buildTargetUri(final URI baseUri,
                               final String downstreamPath,
                               final String queryString) {
        String base = trimTrailingSlash(baseUri.toString());
        String normalizedPath = normalizePath(downstreamPath);
        String query = StringUtils.hasText(queryString) ? "?" + queryString : "";
        return URI.create(base + normalizedPath + query);
    }

    private String normalizePath(final String downstreamPath) {
        if (!StringUtils.hasText(downstreamPath)) {
            return "";
        }
        if (downstreamPath.startsWith("/")) {
            return downstreamPath;
        }
        return "/" + downstreamPath;
    }

    private String trimTrailingSlash(final String value) {
        if (value.endsWith("/")) {
            return value.substring(0, value.length() - 1);
        }
        return value;
    }

    private boolean isHopByHopHeader(final String headerName) {
        return HOP_BY_HOP_HEADERS.contains(headerName.toLowerCase(Locale.ROOT));
    }

    private boolean isGatewayControlledHeader(final String headerName) {
        return GATEWAY_CONTROLLED_HEADERS.contains(headerName.toLowerCase(Locale.ROOT));
    }

    private ResponseEntity<byte[]> textResponse(final HttpStatus status,
                                                final String message) {
        return ResponseEntity.status(status)
                .contentType(MediaType.TEXT_PLAIN)
                .body(message.getBytes(StandardCharsets.UTF_8));
    }

    public record GatewayHealthResponse(String status, int routes) {
    }

    public record GatewayRouteResponse(String id, String pathPrefix, String uri, boolean healthCheckEnabled) {
    }

    public record GatewayServicesHealthResponse(String status, List<GatewayServiceHealthResponse> services) {
    }

    public record GatewayServiceHealthResponse(String id, String pathPrefix, String status, Integer httpStatus, long responseTimeMillis) {
    }
}
