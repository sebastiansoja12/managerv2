package com.warehouse.e2e.mcp;

import com.warehouse.commonassets.identificator.UserId;

public final class McpE2EFixture {

    public static final String MCP_USERS_SEED = "sql/mcp/mcp-users-seed.sql";

    public static final String PASSWORD = "E2e-Mcp-Password-1";

    public static final UserId MANAGER_ID = new UserId(1101L);

    public static final String MANAGER_USERNAME = "e2e.mcp.manager@manager.local";

    public static final UserId USER_ID = new UserId(1102L);

    public static final String USER_USERNAME = "e2e.mcp.user@manager.local";

    public static final UserId REVOKE_USER_ID = new UserId(1103L);

    public static final String REVOKE_USERNAME = "e2e.mcp.revoke@manager.local";

    private McpE2EFixture() {
    }
}
