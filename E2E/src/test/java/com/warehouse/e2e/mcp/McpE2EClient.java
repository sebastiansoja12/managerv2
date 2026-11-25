package com.warehouse.e2e.mcp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.spec.McpSchema;

public class McpE2EClient {

    private static final String MCP_ENDPOINT = "/v1/api/mcp";
    private static final String INITIALIZE_REQUEST = """
            {"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2025-06-18",\
            "capabilities":{},"clientInfo":{"name":"manager-e2e","version":"1.0.0"}}}""";

    private final String serverUrl;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public McpE2EClient(final int serverPort) {
        this.serverUrl = "http://localhost:" + serverPort;
    }

    public McpSyncClient connect(final String mcpKey) {
        final HttpClientStreamableHttpTransport transport = HttpClientStreamableHttpTransport.builder(serverUrl)
                .endpoint(MCP_ENDPOINT)
                .customizeRequest(request -> request.header("Authorization", "Bearer " + mcpKey))
                .build();
        final McpSyncClient client = McpClient.sync(transport)
                .requestTimeout(Duration.ofSeconds(30))
                .build();
        client.initialize();
        return client;
    }

    public McpSchema.CallToolResult callTool(final String mcpKey, final String toolName,
                                             final Map<String, Object> arguments) {
        try (McpSyncClient client = connect(mcpKey)) {
            return client.callTool(new McpSchema.CallToolRequest(toolName, arguments));
        }
    }

    public HttpResponse<String> initialize(final Map<String, String> headers) {
        final HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(serverUrl + MCP_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json, text/event-stream")
                .POST(HttpRequest.BodyPublishers.ofString(INITIALIZE_REQUEST));
        headers.forEach(request::header);
        try {
            return httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString());
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(exception);
        }
    }

    public static String text(final McpSchema.CallToolResult result) {
        return result.content().stream()
                .filter(McpSchema.TextContent.class::isInstance)
                .map(McpSchema.TextContent.class::cast)
                .map(McpSchema.TextContent::text)
                .collect(Collectors.joining("\n"));
    }
}
