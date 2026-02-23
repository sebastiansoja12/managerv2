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
- The `Api` module calls ports/adapters exposed by the corresponding `Core` module.
- Controllers belong to the `Core` module.
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

Use the following package responsibilities:

| Package pattern | Responsibility |
|---|---|
| `*.domain` | Bounded context, domain model, domain services, business rules. |
| `*.configuration` | Configuration for the given domain/module. |
| `*.infrastructure.adapter` | Adapters, persistence entities, integration implementations. |

Rules:

- Keep bounded-context code in `*.domain`.
- Keep domain configuration in `*.configuration`.
- Keep adapters and entities in `*.infrastructure.adapter`.
- Do not break hexagonal boundaries by calling infrastructure directly from another domain.
- Do not introduce reverse dependencies from domain code to infrastructure, controllers, or framework code.

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

- Create domain event classes in the `Core` module, inside `*.domain.event`.
- Use one marker interface per event family when the domain already has one, for example `DeviceEvent`, `SupplierEvent`, or `ShipmentEvent`.
- Prefer the existing local pattern: a base changed event with a snapshot and timestamp, plus specific events such as `DeviceCreated`, `DeviceUpdated`, or `SupplierUpdated`.
- Event constructors must use `final` parameters.
- Event payload should contain a domain snapshot/value object and an `Instant` timestamp, not mutable entities.

Publishing rules:

- Publish domain events from the domain service method that performs the state change.
- For `create(...)`, publish the created event immediately after `repository.create(...)`.
- For `update(...)` or a specific change method, mutate the aggregate, call `repository.update(...)`, then publish the matching event.
- Use the `DomainContext` from the same bounded context/domain, never another module's `DomainContext`.
- Prefer `DomainContext.publishAfterCommit(...)` when it exists in the module, especially when listeners trigger side effects or cross-module actions.
- If the module does not provide `publishAfterCommit(...)`, follow the existing local pattern with `DomainContext.publish(...)` or `DomainContext.eventPublisher().publishEvent(...)`.
- Do not publish events before persistence succeeds.
- Do not publish an event when the method did not create or change domain state.

Example:

```java
public void updateDevice(final DeviceUpdateCommand command) {
    this.deviceRepository.findById(command.deviceId()).ifPresent(device -> {
        device.update(command);
        this.deviceRepository.update(device);
        DomainContext.publishAfterCommit(new DeviceUpdated(device.toSnapshot(), Instant.now()));
    });
}
```

Listeners:

- Put domain event listeners in `*.domain.listener` when they react to domain events.
- Listener dependencies must be ports or domain services, injected through a constructor.
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
