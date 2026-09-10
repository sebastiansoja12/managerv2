# Manager 2.0 Backend

**Development Version 2026.3 - 10th September, 2026**

Manager 2.0 is a Java 21 / Spring Boot backend for warehouse and logistics
operations. The repository is a multi-module Maven project. The main runtime is
the `Application` module, which assembles the domain modules into one API served
under `/v2/api`.

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
| `Application` | Main Spring Boot application on port `8080`, API context `/v2/api`. |
| `Authorization` | Login, JWT/cookie authentication, refresh tokens, current user/operator context and permissions. |
| `OrganisationStructure` | Operator management, operator configuration and provisioning of initial operator data. |
| `DepartmentService` | Departments, department identifiers, coordinates and read-model synchronization. |
| `ShipmentManagement` | Shipments, dangerous goods, pickup and delivery methods, pickup-point assignment, return summaries, shipment status and tracking integrations. |
| `PickupPointManagement` | Pickup-point aggregate, lifecycle, address geocoding, operator-scoped persistence, Kafka-projected search model and REST API. |
| `OrganizationChat` | Persistent direct conversations, organization-user discovery, WebSocket presence and message notifications. |
| `DeliveryOperation` | Delivery, return and rejection process handling. |
| `LogisticsOrchestrator` | Coordination layer for logistics flows. |
| `ProcessHub` | Process logs and process details used by the GUI. |
| `Device` | Device pairing, device verification and terminal/device access validation. |
| `SupplierServiceManagement` | Suppliers, package types, delivery areas, vehicle data and supplier updates. |
| `GeocodingService` | Geocoding provider configuration and geocoding API. |
| `PathFinder` and `DestinationDetermination` | Routing, delivery-department determination, address coordinates and area calculation support. |
| `DocumentManager` | Barcode and CSV generation utilities. |
| `MessageProvider` and `MailService` | Message and mail infrastructure. |
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
- API context path: `/v2/api`
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

The root `Dockerfile` and `Application/Dockerfile` both build the main
`Application` jar and expose port `8080`.

```bash
docker build -t manager-v2-backend .
```

Separate Dockerfiles are available for:

- `Gateway` on port `8088`
- `EurekaServer` on port `8761`

## API and Documentation

- Runtime API prefix: `/v2/api`
- Actuator health endpoint: `/v2/api/actuator/health`
- OpenAPI is configured by `Application/src/main/java/com/warehouse/configuration/OpenApiConfiguration.java`
- Release notes are maintained in `CHANGELOG.md`
- InPost tracking notes are in `docs/inpost-global-tracking.md`
- Writerside documentation sources are in `Writerside`

## Pickup Points

Pickup points are managed under `/v2/api/pickup-points`. The API supports
creation, editing, lifecycle changes, detail lookup and operator-scoped search.
Search can be filtered by text, city, country, type, capability, department,
network and map bounding box. `/pickup-points/eligible` additionally filters by
shipment size and dangerous-goods requirements for shipment forms.

Clients provide an address when creating or editing a point. Coordinates are
resolved by the configured coordinates service, and the assigned department is
validated before the point is saved. The write model publishes a domain event
after persistence; the application layer converts it to the
`pickup-point.read-model.changed` integration event, Kafka transports it, and a
primary listener updates the separate `pickup_point_read_model` table used by
search and map queries. Both write and read persistence use the shared
operator-filtered repository infrastructure.

## Shipment Pickup and Return Data

Shipments store pickup and delivery methods together with separate sender and
recipient pickup-point identifiers. Their target delivery department is required
from creation and is determined through the existing routing services.
Application ports return `ShipmentResult` and control-center results instead of
exposing the mutable shipment model to REST controllers.

Return details are fetched only for shipments in `RETURN` status. Optional
return details degrade to an empty result when ReturningTrackManager is
unavailable, so ordinary shipment details still load. Return list and status
operations exposed by Manager use the generic external Feign client and proxy
processing and completion to ReturningTrackManager.
