# Manager v2 - AI Agent Instructions

## 1. General Rules

- Write backend code in **Java 21**.
- Do not use `var`; always declare explicit types.
- All production code must be in English: class names, method names, variables, inline comments, and messages intended for developers.
- Do not use generic parameter names like `dto`; always use context-specific names that describe the actual meaning.
- Do not add unnecessary comments, Javadoc, or annotations that are not present in the surrounding code.
- Do not create new files or abstractions unless strictly necessary. Prefer editing existing files.
- Do not refactor code outside the scope of the current task.
- Do not commit changes unless explicitly asked by the user.
- Do not run build or test commands after changes unless the user explicitly asks for it.
- Before implementing anything, read an existing similar file and follow the established local pattern.
- Department is called Department (DepartmentCode) not Depot (DepotCode).
- Secondary Adapter for communication with external system has in its name Client
- Mappers are in primary and secondary adapters ONLY
- Primary adapters are connected with primary ports
- Secondary adapters are implementation of secondary ports
- Maven config is manager_settings.xml
- Liquibase changesets author as 's-soja'
- Add finals in interface method args

---

## 2. Project Architecture

The project follows **hexagonal architecture**.

Each business module should be split into:

| Part | Responsibility |
|---|---|
| `Api` | Internal module API used by other domains or modules when they need to call or retrieve something. |
| `Core` | Domain implementation, ports, adapters, entities, controllers, and application logic. |

Rules:

- The `Api` module is used only for internal communication between modules/domains.
- The `Api` module calls ports/adapters exposed by the corresponding `Impl` module.
- An `Impl` module may depend directly on its own `Api` module, but it must not declare a direct Maven dependency on
  another bounded context's `Api` or implementation module. Declare cross-context API dependencies in the owning
  module's `Api` module.
- Controllers belong to the `Impl` module.
- Controllers are outside the `Api` module and are used for communication from outside the system.
- `Application` is the main monolithic application module.
- `ArchitectureTest` is the module used for architecture tests.

External services / microservices:

- `RouteTrackerFlow`
- `SoftwareConfiguration`
- `PalletHandlingManager`
- `ReturningTrackManager`
- `DeliveryProtection`

---

## 3. Package Structure

Use the layered package structure established in `ShipmentManagement`. Each bounded context inside an implementation module must use the following top-level packages:

| Package pattern | Responsibility |
|---|---|
| `*.domain` | Pure business model and rules: aggregates, entities, value objects, enumerations, domain services, domain events, and domain exceptions. |
| `*.application` | Use-case orchestration: primary and secondary ports, commands, results, application services, event listeners, and integration-event contracts. |
| `*.infrastructure` | Technology-specific adapters: HTTP/Kafka entry points, persistence, external clients, entities, framework repositories, and adapter mappers. |
| `*.configuration` | Dependency composition and framework configuration for the bounded context. |

Use these subpackages when the corresponding responsibility exists:

| Package pattern | Responsibility |
|---|---|
| `*.domain.model` | Aggregates and domain entities. |
| `*.domain.vo` | Domain value objects and snapshots used by domain events. |
| `*.domain.enumeration` | Domain enumerations. |
| `*.domain.service` | Business rules that operate only on domain concepts. |
| `*.domain.event` | Domain events describing business changes that already happened. |
| `*.domain.exception` | Domain failures and business exceptions. |
| `*.application.port.primary` | Inbound use-case contracts and their application implementations. |
| `*.application.port.primary.command` | Commands accepted by primary ports. |
| `*.application.port.primary.result` | Results returned by primary ports. |
| `*.application.port.secondary` | Outbound contracts required by application use cases, including persistence and integrations. |
| `*.application.service` | Use-case orchestration shared by primary-port implementations. |
| `*.application.listener` | Domain-event listeners and listeners that translate events owned by the same bounded context into outgoing integration events. |
| `*.application.event` | Integration-event contracts when the bounded context has no separate `Api` module. Do not place domain events here. |
| `*.application.event.snapshot` | Stable serializable payloads used by integration events. |
| `*.infrastructure.adapter.primary` | Controllers, inbound messaging adapters, listeners for integration events received from other bounded contexts, request validation, and inbound mapping. |
| `*.infrastructure.adapter.secondary` | Persistence adapters, external service adapters, JPA entities, technical repositories, and outbound mapping. |

Rules:

- The dependency direction is `infrastructure -> application -> domain`. Configuration may compose all three layers.
- Domain code must not depend on application, infrastructure, Spring, JPA, Kafka, controllers, repositories from frameworks, or configuration classes.
- Keep state changes and business invariants in domain models or domain services.
- Keep transactions, persistence sequencing, event publication, calls to other modules, and use-case coordination in the application layer.
- Put primary and secondary port interfaces in `*.application.port`, not in `*.domain.port`.
- Put persistence contracts such as `ShipmentRepository` in `*.application.port.secondary`; framework repositories stay in `*.infrastructure.adapter.secondary`.
- A primary adapter must call a primary port. It must not call repositories or mutate domain objects directly.
- A secondary adapter must implement a secondary port owned by the application layer.
- Mapping between transport/persistence models and domain or application models belongs in primary or secondary adapters only.
- Domain events belong in `*.domain.event`. A cross-context integration-event contract belongs to the owning bounded
  context's `Api` module under `*.api.event`; use `*.application.event` only when that context has no separate `Api`
  module. Serializable event payloads follow the same ownership rule.
- Domain-event listeners belong in `*.application.listener`.
- A listener that translates a bounded context's own domain event into an outgoing integration event belongs in
  `*.application.listener` and publishes through `IntegrationEventPublisher` so the event is stored in the Kafka outbox.
- A listener for an integration event received from another bounded context is an inbound adapter and belongs in
  `*.infrastructure.adapter.primary`. It must consume the event through `@KafkaEventListener` and invoke a primary port;
  it must not use Spring's in-process `@EventListener` as a cross-context transport.
- The separate `Api` Maven module contains cross-module contracts only. Implementations, controllers, entities, and application orchestration remain in the corresponding implementation module.
- Apply this structure to all new bounded contexts and new features. Do not refactor a legacy context only to move packages unless the task explicitly includes that migration.
- Do not break hexagonal boundaries by calling infrastructure directly from another domain.
- Another bounded context must use the owning context's `Api` module or an application secondary port with an adapter; it must never import the owning context's implementation or infrastructure packages.

---

## 4. Ports and Adapters

Use explicit naming for outgoing integrations:

- An outgoing port to an outgoing adapter must include `ServicePort` in its name.
- An outgoing adapter must include `ServiceAdapter` in its name.

Example:

```java
public interface RouteTrackerServicePort {
    TrackingDetails getTrackingDetails(final TrackingNumber trackingNumber);
}

public class RouteTrackerServiceAdapter implements RouteTrackerServicePort {
    ...
}
```

Rules:

- Define the port on the side that owns the business need.
- Keep adapter implementation details outside the domain.
- Keep mapping between external service contracts and internal domain objects inside the adapter layer.

---

## 5. Domain Events

Domain events describe business changes that already happened in a bounded context.

Location and structure:

- Create domain event classes in the implementation module, inside `*.domain.event`.
- Create integration event classes and their serializable payloads inside `*.application.event` and `*.application.event.snapshot`.
- Use one marker interface per event family when the domain already has one, for example `DeviceEvent`, `SupplierEvent`, or `ShipmentEvent`.
- Prefer the existing local pattern: a base changed event with a snapshot and timestamp, plus specific events such as `DeviceCreated`, `DeviceUpdated`, or `SupplierUpdated`.
- Event constructors must use `final` parameters.
- Event payload should contain a domain snapshot/value object and an `Instant` timestamp, not mutable entities.
- Integration-event listeners map domain snapshots to stable integration-event snapshots. Domain models must not depend on integration-event contracts or serialization annotations.

Publishing rules:

- Publish domain events from the application use case that coordinates the state change and persistence.
- Inject the shared `DomainEventPublisher` through the application-layer constructor. Do not access a static `DomainContext` from new code.
- For `create(...)`, persist the aggregate and then publish the created domain event.
- For `update(...)`, invoke the domain behavior, persist the changed aggregate, and then publish the matching domain event.
- A domain method may return a domain event when the event depends on the result of a business transition; the application layer remains responsible for publishing it.
- Do not publish events before persistence succeeds.
- Do not publish an event when the method did not create or change domain state.

Example:

```java
public ShipmentId create(final ShipmentCreateCommand command) {
    final Shipment shipment = shipmentFactory.create(command);
    this.shipmentRepository.createOrUpdate(shipment);
    this.domainEventPublisher.publish(new ShipmentCreated(shipment.snapshot(), Instant.now()));
    return shipment.getShipmentId();
}
```

Listeners:

- Put domain-event listeners and producers of outgoing integration events in `*.application.listener`.
- Put consumers of integration events owned by another bounded context in `*.infrastructure.adapter.primary`.
- Outgoing integration events must implement `IntegrationEvent`, declare `@IntegrationEventType` with a stable type and
  version, and be published through `IntegrationEventPublisher`. Configure their
  `manager.kafka.integration-events.routes.<event-type>` route to a Kafka topic.
- Kafka producers must put the integration event's fully qualified class name in the `__TypeId__` header. Producers
  and consumers must use the same event class from the owning bounded context's `Api` module so Spring Kafka can
  serialize and deserialize it automatically. Do not add per-event `manager.kafka.type-mappings` entries.
- Incoming cross-context listeners must use `@KafkaEventListener`, configure an explicit topic and consumer group, and
  call a primary port. They must not call repositories or infrastructure adapters directly.
- Listener dependencies must be primary ports, secondary ports, application services, or event publishers, injected through a constructor.
- Listeners must not call infrastructure adapters directly.

---

## 6. Dependency Injection

- Do not use `@Autowired`.
- Prefer constructor injection for all services and collaborators.
- Always use `final` for injected services, dependencies, and fields when applicable.
- Always use `final` for parameters in interfaces, methods, and constructors.
- Do not use field injection.
- Instantiate classes under test directly through constructors.

Example:

```java
public class ShipmentService {

    private final RouteTrackerServicePort routeTrackerServicePort;
    private final ShipmentRepository shipmentRepository;

    public ShipmentService(final RouteTrackerServicePort routeTrackerServicePort,
                           final ShipmentRepository shipmentRepository) {
        this.routeTrackerServicePort = routeTrackerServicePort;
        this.shipmentRepository = shipmentRepository;
    }
}
```

---

## 7. Null Safety and Defensive Code

Do not create defensive-code hell.

- Do not add defensive `null` checks preemptively.
- Before adding a null check, NPE safeguard, fallback, retry, or broad exception handling, verify that the problematic value or failure is realistic.
- Confirm the contract by reading call sites, constructors, framework binding code, serialization/deserialization paths, or existing tests.
- Prefer making invalid states impossible through constructors, value objects, validation at boundaries, and clear method contracts.
- Use `Optional` to express the absence of a value when returning from methods.

When consuming an `Optional`, prefer functional style:

```java
shipmentRepository.findById(shipmentId)
        .ifPresent(shipment -> shipment.confirmDelivery(deliveryDate));

String status = shipmentRepository.findById(shipmentId)
        .map(Shipment::getStatus)
        .orElse("UNKNOWN");
```

Avoid:

```java
if (optionalShipment.isPresent()) {
    optionalShipment.get().confirmDelivery(deliveryDate);
}
```

Do not use `Optional.get()` without a prior `isPresent()` check. Prefer `orElse`, `orElseGet`, `orElseThrow`, `map`, `flatMap`, `ifPresent`, or `ifPresentOrElse`.

---

## 8. Authorization

Application authorization is based on:

- JWT token, or
- a dedicated API key generated for a specific device.

Device API keys may be generated for:

- terminals,
- scanners,
- mobile devices.

Rules:

- Do not invent a new authorization mechanism.
- Keep JWT and device API key handling consistent with existing security configuration.
- Treat device API keys as credentials; do not log them or expose them in plain text.

---

## 9. Logging

- Use the logging approach already present in the surrounding code.
- Use SLF4J log levels appropriately:
  - `info` for normal business operations,
  - `warn` for unexpected but recoverable situations,
  - `error` for failures that require attention.
- Use parameterized messages with `{}`.
- Do not concatenate strings in log messages.
- Do not log secrets, JWT tokens, API keys, personal data, or sensitive business data.

---

## 10. Database Changes

- All database schema or data changes must be added through Liquibase files.
- Do not add database changes as standalone SQL migration files outside the Liquibase changelog structure.
- Never modify an already applied Liquibase changeset; add a new changeset instead.
- Keep Liquibase changes idempotent when possible and consistent with the existing changelog style.

---

## 11. Tests

- Write new tests in JUnit 5 unless the touched area already uses a different established test style.
- Do not create new JUnit 4 tests.
- Put architecture rules and architecture-boundary tests in the `ArchitectureTest` module.
- Test domain logic and application services as pure unit tests whenever possible.
- Instantiate the class under test via its constructor directly.
- Do not use `@Autowired` or `@InjectMocks` in unit tests.
- For non-trivial test data, create a dedicated `*Fixture` class in the same test source tree.

Test method naming:

```java
@Test
void shouldReturnShipmentWhenShipmentExists() {
    ...
}

@Test
void shouldThrowExceptionWhenShipmentDoesNotExist() {
    ...
}
```

Structure test methods with blank lines between setup, action, and assertion. Do not add `given/when/then` comments when the test name already explains the scenario.

---

## 12. What NOT to Do

- Do not use `var`.
- Do not use `@Autowired`.
- Do not use field injection.
- Do not add defensive null checks without verifying that null is realistic.
- Do not create new abstractions or files unless they are strictly necessary.
- Do not bypass the Api/Core module split.
- Do not call infrastructure directly from another domain.
- Do not put external-system communication into the domain layer.
- Do not invent new naming conventions for ports and adapters.
- Do not publish domain events before `create(...)` or `update(...)` persistence succeeds.
- Do not publish domain events from controllers or infrastructure adapters when the state change belongs to a domain service.
- Do not add database changes outside Liquibase files.
- Do not introduce a new authorization mechanism.
- Do not run build or test commands unless explicitly asked by the user.
- Do not commit changes unless explicitly asked by the user.
