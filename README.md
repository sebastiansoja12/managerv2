# Manager 2.0 Backend

**Development Version 2026.3 - 6th July, 2026**

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
- Spring Data JPA/JDBC, Liquibase and MySQL
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
| `ShipmentManagement` | Shipments, dangerous goods, shipment status, tracking numbers and external tracking integrations. |
| `DeliveryOperation` | Delivery, return and rejection process handling. |
| `LogisticsOrchestrator` | Coordination layer for logistics flows. |
| `ProcessHub` | Process logs and process details used by the GUI. |
| `Device` | Device pairing, device verification and terminal/device access validation. |
| `SupplierServiceManagement` | Suppliers, package types, delivery areas, vehicle data and supplier updates. |
| `GeocodingService` | Geocoding provider configuration and geocoding API. |
| `PathFinder` and `DestinationDetermination` | Routing, destination and area calculation support. |
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
- Database: `jdbc:mysql://localhost:3306/dev`
- Database user/password: `root` / `rootpassword`
- Liquibase changelog: `classpath:/changelog/db.changelog-master.xml`
- Eureka URL: `http://localhost:8761/eureka/`
- Kafka bootstrap servers: `localhost:9092`
- GUI CORS origin: `http://localhost:3000`

The MySQL helper script in `docker/mysql/init/01-create-databases.sql` creates
the `dev`, `rm` and `rt` databases.

Common environment variables:

- `SPRING_PROFILES_ACTIVE`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`
- `JWT_SECRET_KEY`
- `AUTH_CORS_ALLOWED_ORIGINS`
- `CREDENTIALS_ENCRYPTION_KEY` or `GEOCODING_ENCRYPTION_KEY`

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
