# Manager 2.0 Backend

**Development Version 2026.4 - 10th September, 2026**

Manager 2.0 is a Java 21 / Spring Boot backend for warehouse and logistics
operations. The repository is a multi-module Maven project. The main runtime is
the `Application` module, which assembles the domain modules into one API served
under `/v1/api`.

## Technology

- Java 21
- Maven multi-module build
- Spring Boot 3.5.5
- Spring Cloud 2025.0.3
- Spring Web, Security, Validation, Actuator and OpenFeign
- Spring Data JPA/JDBC, Liquibase and PostgreSQL
- Kafka for domain events and shipment read-model synchronization
- Eureka service discovery and a lightweight Gateway service
- Lombok, MapStruct and springdoc OpenAPI

## Main Modules

| Module | Purpose |
| --- | --- |
| `Application` | Main Spring Boot application on port `8080`, API context `/v1/api`. |
| `Authorization` | Login, JWT/cookie authentication, refresh tokens, current user/operator context and permissions. |
| `OrganisationStructure` | Operator management, operator configuration, provisioning of initial operator data and departments (identifiers, coordinates and read-model synchronization). |
| `ShipmentManagement` | Shipments, dangerous goods, pickup and delivery methods, pickup-point assignment, return summaries, shipment status, notes, document links and tracking integrations. |
| `PickupPointManagement` | Pickup-point aggregate, lifecycle, address geocoding, operator-scoped persistence, Kafka-projected search model and REST API. |
| `OrganizationChat` | Persistent direct conversations, organization-user discovery, WebSocket presence and message notifications. |
| `DeliveryOperation` | Delivery, return and rejection process handling. |
| `LogisticsOrchestrator` | Coordination layer for logistics flows. |
| `ProcessHub` | Process logs and process details used by the GUI. |
| `DepartmentMap` | Department floor plans (`department_maps`, elements), storage-location occupancy with optimistic locking, pallet placement, reservations and location history (`/department-maps`). |
| `Device` | Device pairing, device verification and terminal/device access validation. |
| `McpServer` | Read-only MCP server (`/v1/api/mcp`) exposing shipment, process and device lookups to AI assistants. |
| `SupplierServiceManagement` | Suppliers, package types, delivery areas, vehicle data and supplier updates. |
| `GeocodingService` | Geocoding provider configuration and geocoding API. |
| `PathFinder` and `DestinationDetermination` | Routing, delivery-department determination, address coordinates and area calculation support. |
| `DocumentStorage` | Barcode and CSV generation utilities, plus shared operator-scoped document storage (`DocumentStorageApi` and `DocumentStorageImpl`). |
| `MessageProvider` and `MailService` | Message infrastructure; Kafka-driven e-mail notifications rendered from multilingual database templates. |
| `Common` | Shared value objects, enums, exceptions, repository helpers, operator context and security utilities. |
| `Gateway` | Gateway service on port `8088`; routes requests to manager and related services. |
| `EurekaServer` | Eureka server on port `8761`. |
| `ArchitectureTest` and `E2E` | Architecture and end-to-end test modules. |

Some older or service-specific directories are still present in the repository
but are not part of the active root Maven reactor, for example `Terminal`,
`PaymentManagement`, `Properties` and selected `target` directories.

## Local Configuration

The default profile is `dev`.

Important defaults from `Application/src/main/resources`:

- API port: `8080`
- API context path: `/v1/api`
- Database: `jdbc:postgresql://localhost:5432/dev`
- Database user/password: `postgres` / `postgres`
- Liquibase changelog: `classpath:/changelog/db.changelog-master.xml`
- Eureka URL: `http://localhost:8761/eureka/`
- Kafka bootstrap servers: `localhost:9092`
- GUI CORS origin: `http://localhost:3000`

The PostgreSQL helper script in `docker/postgresql/init/01-create-databases.sql` creates
the `dev`, `rm` and `rt` databases.

Common environment variables:

- `SPRING_PROFILES_ACTIVE`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`
- `MANAGER_KAFKA_PICKUP_POINT_READ_MODEL_SYNC_ENABLED`
- `PICKUP_POINT_READ_MODEL_SYNC_TOPIC`
- `PICKUP_POINT_READ_MODEL_SYNC_GROUP_ID`
- `RETURNING_SERVICE_URL`
- `RETURNING_SERVICE_ENDPOINT`
- `JWT_SECRET_KEY`
- `AUTH_CORS_ALLOWED_ORIGINS`
- `CREDENTIALS_ENCRYPTION_KEY`

### E-mail notifications

`MailService` consumes shipment integration events from Kafka in `MailKafkaListener`
(`shipment.created`, `shipment.events` status changes, `shipment.out-for-delivery`, `shipment.delivered`,
`shipment.reroute.requested`), maps each event to generic `MailRequest`s (template key, recipient, locale,
variables) and sends them over SMTP. Mail texts are not in code: they are stored in the `mail_templates`
table (`template_key`, `locale`, `subject`, `body`) seeded by Liquibase in `pl`, `en` and `de`.
Templates use `{{variable}}` placeholders and optional `{{#variable}}...{{/variable}}` sections.
A request without a template is skipped, so adding a template row enables a new notification
(for example `shipment.status.prepared.recipient`). The recipient language is resolved from the
party country code (`MAIL_COUNTRY_LOCALES`) with fallback to the language and then to `MAIL_DEFAULT_LOCALE`.

Local development: `docker compose -f docker.compose.yml up -d mailhog` starts MailHog (SMTP `localhost:1025`,
web UI http://localhost:8025) and the `dev` profile sends every e-mail there, so nothing leaves your machine.
To use a real SMTP server instead, copy `application-local.properties.example` to `application-local.properties`
(ignored by git, imported by the `dev` profile from the working directory or its parent) and fill in the mailbox
credentials. Environment variables override both files.

- `MAIL_ENABLED` (`false` by default, `true` in the `dev` profile; when `false` e-mails are only logged)
- `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_SMTP_AUTH`, `MAIL_SMTP_STARTTLS`, `MAIL_SMTP_SSL`
- `MAIL_FROM_ADDRESS`, `MAIL_FROM_NAME`
- `MAIL_DEFAULT_LOCALE`, `MAIL_UNMAPPED_COUNTRY_LOCALE`, `MAIL_COUNTRY_LOCALES`
- `MAIL_TRACKING_URL`, `MAIL_SHIPMENT_EDIT_URL`

## Build and Run

Build all active Maven modules:

```bash
mvn clean package
```

Run the main backend API:

```bash
mvn -pl Application -am spring-boot:run
```

Run Eureka:

```bash
mvn -pl EurekaServer -am spring-boot:run
```

Run the Gateway:

```bash
mvn -pl Gateway -am spring-boot:run
```

Run tests:

```bash
mvn test
```

Run tests for a single module:

```bash
mvn -pl ShipmentManagement/ShipmentManagementImpl -am test
```

## Docker

Run the Compose stack from the repository root:

```bash
docker compose -f docker.compose.yml up -d
```

Stop all services together, keeping their containers and database volumes:

```bash
docker compose -f docker.compose.yml stop
```

Start the existing containers again with `docker compose -f docker.compose.yml start`. To stop and
remove the stack's containers and network, use `docker compose -f docker.compose.yml down`; named
database volumes are retained unless `--volumes` is explicitly supplied.

The stack is defined in `docker.compose.yml`. The project name remains
`managerv2`, matching the existing containers and Docker Desktop configuration.

The stack contains PostgreSQL, MongoDB, ZooKeeper, Kafka, Kafka UI, MailHog (http://localhost:8025), Eureka,
Gateway, Software Configuration, Returning Manager and Route Tracker. Run the
main `Application` separately using the instructions above.

Older Elasticsearch and Kibana containers may still appear in Docker Desktop
under this project, but they are no longer defined in this Compose configuration.

The root `Dockerfile` and `Application/Dockerfile` both build the main
`Application` jar and expose port `8080`.

```bash
docker build -t manager-v2-backend .
```

Separate Dockerfiles are available for:

- `Gateway` on port `8088`
- `EurekaServer` on port `8761`

## MCP Server

`McpServer` exposes a stateless MCP endpoint (Spring AI MCP Server, streamable HTTP) at `/v1/api/mcp`.
Requests are authenticated with a user API key sent as `Authorization: Bearer <key>`; the key is generated
with `POST /v1/api/auth/me/api-key` (revoked with `DELETE`) and stored only as an HMAC hash
with its issue date. Keys created before the issue date was introduced, and the keys assigned automatically at
sign-up, are not accepted. Operator blocking and session revocation also invalidate API keys issued earlier.

| Tool | Parameters | Notes |
| --- | --- | --- |
| `whoami` | none | Current user, role, department and operator. |
| `find_shipment` | `trackingNumber` | Status, dates, masked sender/recipient (initials, city, postal code) and the 10 newest events. |
| `get_process` | `processId` (UUID) | Process status, fault, device and communication steps without request payloads. |
| `list_devices` | optional `status`, `departmentCode` | Up to 50 devices with assignment mode, heartbeat and signed-in operator; requires MANAGER or higher. |

All tools are read-only and run in the operator context of the API key owner. Disable the endpoint with
`MCP_SERVER_ENABLED=false`. Example for Claude Code:

```bash
claude mcp add --transport http manager https://manager.example.com/v1/api/mcp --header "Authorization: Bearer <api-key>"
```

## API and Documentation

Return operations are exposed by `ReturnPackageController` under `/v1/api/returns/packages`.
Creation accepts a shipment ID; processing (`PUT /{returnId}/process`), completion
(`PUT /{returnId}/complete`) and cancellation (`DELETE /{returnId}`) use the return package ID.
The same controller provides the return details and paginated department list. The returning
context calls `ShipmentApiService` for shipment changes; shipment summaries read return details
through `ReturningApiService`.

RTM publishes `return.cancelled` through its transactional outbox. Manager handles it in the
returning context, restores the original shipment to `DELIVERY` and cancels its linked return
shipment. The Manager `return_processing_state` changeset stores terminal cancellation markers
so duplicate or delayed processing events cannot undo cancellation. No RTM schema change is required.

Shipment search uses `ShipmentQueryPort` through the existing `/v1/api/shipments/read-model/search` endpoint.

`ReturnConsumedEventTransactionTest` requires an isolated PostgreSQL database. Enable it with
`RETURN_TEST_POSTGRES=true` and set `RETURN_TEST_JDBC_URL`, `RETURN_TEST_JDBC_USER` and
`RETURN_TEST_JDBC_PASSWORD`. It creates and removes its own test schema.

- Runtime API prefix: `/v1/api`
- Actuator health endpoint: `/v1/api/actuator/health`
- OpenAPI is configured by `Application/src/main/java/com/warehouse/configuration/OpenApiConfiguration.java`
- Release notes are maintained in `CHANGELOG.md`
- InPost tracking notes are in `docs/inpost-global-tracking.md`
- Writerside documentation sources are in `Writerside`
