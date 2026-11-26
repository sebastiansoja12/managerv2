package com.warehouse.e2e.mcp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

public class ManagerSessionClient {

    private static final String CSRF_HEADER = "X-XSRF-TOKEN";
    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();

    private final String baseUrl;
    private final CookieManager cookieManager;
    private final HttpClient httpClient;
    private String bearerToken;

    public ManagerSessionClient(final String baseUrl) {
        this.baseUrl = baseUrl;
        this.cookieManager = new CookieManager();
        this.httpClient = HttpClient.newBuilder().cookieHandler(cookieManager).build();
    }

    public ManagerSessionClient login(final String username, final String password) {
        final HttpResponse<String> response = send(jsonRequest("/auth/login",
                "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}", "POST"));
        if (response.statusCode() != 204) {
            throw new IllegalStateException("Login of " + username + " failed with " + response.statusCode()
                    + ": " + response.body());
        }
        return this;
    }

    public ManagerSessionClient withBearer(final String token) {
        this.bearerToken = token;
        return this;
    }

    public HttpResponse<String> get(final String path) {
        return send(request(path).GET());
    }

    public HttpResponse<String> post(final String path, final String body) {
        return send(jsonRequest(path, body, "POST"));
    }

    public HttpResponse<String> delete(final String path) {
        return send(request(path).header(CSRF_HEADER, csrfToken()).DELETE());
    }

    public JsonNode json(final HttpResponse<String> response) {
        return JSON_MAPPER.readTree(response.body());
    }

    private HttpRequest.Builder jsonRequest(final String path, final String body, final String method) {
        return request(path)
                .header("Content-Type", "application/json")
                .header(CSRF_HEADER, csrfToken())
                .method(method, HttpRequest.BodyPublishers.ofString(body));
    }

    private HttpRequest.Builder request(final String path) {
        final HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(baseUrl + path));
        if (bearerToken != null) {
            builder.header("Authorization", "Bearer " + bearerToken);
        }
        return builder;
    }

    private String csrfToken() {
        return json(send(request("/auth/csrf").GET())).get("token").asString();
    }

    private HttpResponse<String> send(final HttpRequest.Builder requestBuilder) {
        try {
            return httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(exception);
        }
    }
}
