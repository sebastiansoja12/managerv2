# E2E

End-to-end tests start the whole `Application` with PostgreSQL and Kafka in Testcontainers (Docker is required) and
call it from outside over its real protocols. They are skipped in the normal build and run only with the `e2e` profile
(`*E2ETest` classes, Maven Failsafe).

| Folder | Protocol | Tests |
| --- | --- | --- |
| `com.warehouse.e2e` | Terminal SOAP | `CollectE2ETest`, `DeliveryRejectE2ETest` |
| `com.warehouse.e2e.mcp` | MCP (streamable HTTP) | `McpE2ETest` |

## MCP

`McpE2ETest` logs seeded users in through `/auth/login` (cookies and CSRF, `ManagerSessionClient`), generates MCP keys
through `/auth/me/mcp-keys` and connects with the MCP Java SDK client (`McpE2EClient`) to `/v1/api/mcp`. It covers key
generation, listing without secrets, tool listing and calls in the key owner's context, `@Mcp` role checks, rejection
of missing, unknown, API and revoked keys on MCP, rejection of MCP keys on REST, last-use tracking and revocation of the
previous key when a new one is generated. Seed data: `src/test/resources/sql/mcp`.

Run all E2E tests:

```bash
mvn -s ~/.m2/manager_settings.xml -pl E2E -am -Pe2e verify -Dtest=NONE -Dsurefire.failIfNoSpecifiedTests=false
```

Run only the MCP tests:

```bash
mvn -s ~/.m2/manager_settings.xml -pl E2E -am -Pe2e verify -Dtest=NONE -Dsurefire.failIfNoSpecifiedTests=false -Dit.test=McpE2ETest
```
