package com.warehouse.e2e.mcp;

import static com.warehouse.e2e.mcp.McpE2EFixture.MANAGER_ID;
import static com.warehouse.e2e.mcp.McpE2EFixture.MANAGER_USERNAME;
import static com.warehouse.e2e.mcp.McpE2EFixture.MCP_USERS_SEED;
import static com.warehouse.e2e.mcp.McpE2EFixture.PASSWORD;
import static com.warehouse.e2e.mcp.McpE2EFixture.REVOKE_USERNAME;
import static com.warehouse.e2e.mcp.McpE2EFixture.REVOKE_USER_ID;
import static com.warehouse.e2e.mcp.McpE2EFixture.USER_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.e2e.AbstractE2ETest;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import tools.jackson.databind.JsonNode;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class McpE2ETest extends AbstractE2ETest {

    private static final String MCP_KEYS = "/auth/me/mcp-keys";

    private final String restUrl;

    private final McpE2EClient mcpClient;

    private ManagerSessionClient managerSession;

    private JsonNode managerKey;

    private String managerSecret;

    private String userSecret;

    McpE2ETest(@LocalServerPort final int serverPort, final DataSource dataSource) {
        super(serverPort, dataSource);
        this.restUrl = "http://localhost:" + serverPort + "/v1/api";
        this.mcpClient = new McpE2EClient(serverPort);
    }

    @BeforeAll
    void generateMcpKeys() {
        executeSqlScript(MCP_USERS_SEED);

        managerSession = new ManagerSessionClient(restUrl).login(MANAGER_USERNAME, PASSWORD);
        final HttpResponse<String> managerResponse = managerSession.post(MCP_KEYS, "{\"name\":\"Claude Code\"}");
        assertThat(managerResponse.statusCode()).as("MCP key response: %s", managerResponse.body()).isEqualTo(201);
        managerKey = managerSession.json(managerResponse);
        managerSecret = managerKey.get("key").asString();

        final ManagerSessionClient userSession = new ManagerSessionClient(restUrl).login(USER_USERNAME, PASSWORD);
        userSecret = generateSecret(userSession, "User assistant");
    }

    @Test
    void shouldReturnMcpKeyOnlyOnceAndStoreOnlyItsHash() {
        final String mcpKeyId = managerKey.get("mcpKey").get("mcpKeyId").get("value").asString();

        assertThat(managerSecret).startsWith("mcp_");
        assertThat(managerKey.get("mcpKey").get("keyPrefix").asString()).isEqualTo(managerSecret.substring(0, 10));
        assertThat(managerKey.get("mcpKey").get("status").asString()).isEqualTo("ACTIVE");
        assertThat(storedKey(mcpKeyId).get("key_hash")).isNotEqualTo(managerSecret);
        assertThat(userMcpKeyId(MANAGER_ID)).isEqualTo(UUID.fromString(mcpKeyId));
    }

    @Test
    void shouldListMcpKeysWithoutSecret() {
        final HttpResponse<String> response = managerSession.get(MCP_KEYS);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).doesNotContain(managerSecret);
        assertThat(managerSession.json(response).get(0).get("name").asString()).isEqualTo("Claude Code");
    }

    @Test
    void shouldExposeToolsOverMcpWithMcpKey() {
        try (McpSyncClient client = mcpClient.connect(managerSecret)) {
            final List<String> tools = client.listTools().tools().stream().map(McpSchema.Tool::name).toList();

            assertThat(tools).contains("whoami", "find_shipment", "list_devices", "cancel_delivery",
                    "change_department_status");
        }
    }

    @Test
    void shouldRunToolInContextOfMcpKeyOwner() {
        final McpSchema.CallToolResult result = mcpClient.callTool(managerSecret, "whoami", Map.of());

        assertThat(result.isError()).as(McpE2EClient.text(result)).isNotEqualTo(Boolean.TRUE);
        assertThat(McpE2EClient.text(result)).contains(MANAGER_USERNAME).contains("MANAGER");
    }

    @Test
    void shouldRecordLastUsageOfMcpKey() {
        mcpClient.callTool(managerSecret, "whoami", Map.of());

        final String mcpKeyId = managerKey.get("mcpKey").get("mcpKeyId").get("value").asString();
        assertThat(storedKey(mcpKeyId).get("last_used_at")).isNotNull();
    }

    @Test
    void shouldRejectToolRequiringHigherRole() {
        final McpSchema.CallToolResult result = mcpClient.callTool(userSecret, "list_devices", Map.of());

        assertThat(result.isError()).isTrue();
        assertThat(McpE2EClient.text(result)).contains("requires the MANAGER role or higher");
    }

    @Test
    void shouldRejectMcpRequestWithoutCredentials() {
        final HttpResponse<String> response = mcpClient.initialize(Map.of());

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat(response.headers().firstValue("WWW-Authenticate")).contains("Bearer realm=\"manager-mcp\"");
    }

    @Test
    void shouldRejectUnknownMcpKey() {
        final HttpResponse<String> response = mcpClient.initialize(
                Map.of("Authorization", "Bearer mcp_unknown-key-that-was-never-issued"));

        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    void shouldRejectApiKeyOnMcpEndpoint() {
        final HttpResponse<String> apiKeyResponse = managerSession.post("/auth/me/api-key", "");
        final String apiKey = managerSession.json(apiKeyResponse).get("apiKey").asString();

        final HttpResponse<String> response = mcpClient.initialize(Map.of("Authorization", "Bearer " + apiKey));

        assertThat(apiKeyResponse.statusCode()).isEqualTo(200);
        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    void shouldRejectMcpKeyOnRestEndpoint() {
        final HttpResponse<String> response = new ManagerSessionClient(restUrl)
                .withBearer(managerSecret)
                .get("/auth/me");

        assertThat(response.statusCode()).isIn(401, 403);
    }

    @Test
    void shouldRevokeMcpKeyImmediately() {
        final ManagerSessionClient session = new ManagerSessionClient(restUrl).login(REVOKE_USERNAME, PASSWORD);
        final JsonNode generated = session.json(session.post(MCP_KEYS, "{\"name\":\"To revoke\"}"));
        final String secret = generated.get("key").asString();
        final String mcpKeyId = generated.get("mcpKey").get("mcpKeyId").get("value").asString();
        assertThat(mcpClient.initialize(Map.of("Authorization", "Bearer " + secret)).statusCode()).isEqualTo(200);

        final HttpResponse<String> revokeResponse = session.delete(MCP_KEYS + "/" + mcpKeyId);

        assertThat(revokeResponse.statusCode()).isEqualTo(204);
        assertThat(mcpClient.initialize(Map.of("Authorization", "Bearer " + secret)).statusCode()).isEqualTo(401);
        assertThat(storedKey(mcpKeyId).get("status")).isEqualTo("REVOKED");
        assertThat(userMcpKeyId(REVOKE_USER_ID)).isNull();
    }

    @Test
    void shouldRevokePreviousMcpKeyWhenNewOneIsGenerated() {
        final ManagerSessionClient session = new ManagerSessionClient(restUrl).login(REVOKE_USERNAME, PASSWORD);
        final String previousSecret = generateSecret(session, "Previous");

        final String currentSecret = generateSecret(session, "Current");

        assertThat(mcpClient.initialize(Map.of("Authorization", "Bearer " + previousSecret)).statusCode())
                .isEqualTo(401);
        assertThat(mcpClient.initialize(Map.of("Authorization", "Bearer " + currentSecret)).statusCode())
                .isEqualTo(200);
    }

    private String generateSecret(final ManagerSessionClient session, final String name) {
        final HttpResponse<String> response = session.post(MCP_KEYS, "{\"name\":\"" + name + "\"}");
        assertThat(response.statusCode()).as("MCP key response: %s", response.body()).isEqualTo(201);
        return session.json(response).get("key").asString();
    }

    private Map<String, Object> storedKey(final String mcpKeyId) {
        return jdbcClient.sql("SELECT key_hash, status, last_used_at FROM mcp_keys WHERE mcp_key_id = :mcpKeyId")
                .param("mcpKeyId", UUID.fromString(mcpKeyId))
                .query()
                .singleRow();
    }

    private UUID userMcpKeyId(final UserId userId) {
        return jdbcClient.sql("SELECT mcp_key_id FROM users WHERE user_id = :userId")
                .param("userId", userId.value())
                .query(UUID.class)
                .optional()
                .orElse(null);
    }
}
