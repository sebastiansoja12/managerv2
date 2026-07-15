# Delivery Network Backend Design

## Decision

`DeliveryNetwork` is a separate Maven module and bounded context. It does not belong to `OrganisationStructure` or
`DepartmentService`.

- `DepartmentService` owns departments, their type, status, code, and coordinates.
- `DeliveryNetwork` owns direct delivery connections and path finding over those connections.
- `OrganisationStructure` owns operators and their configuration, but it does not own the delivery graph.
- Shipment and logistics modules consume the published `DeliveryNetworkApi` contract. They must not read delivery
  network persistence directly.

The first backend implementation increment contains only the Maven module skeleton and dependency boundaries. It does
not include a controller or persistence implementation.

## Maven modules

```text
DeliveryNetwork/
|-- pom.xml
|-- DeliveryNetworkApi/
|   `-- pom.xml
`-- DeliveryNetworkImpl/
    `-- pom.xml
```

The root `pom.xml` includes `DeliveryNetwork`, and `Application` depends on `DeliveryNetworkImpl`.

Dependencies:

| Module | Dependencies |
|---|---|
| `DeliveryNetworkApi` | `Common`, `DepartmentServiceApi`, `AuthorizationApi` |
| `DeliveryNetworkImpl` | `DeliveryNetworkApi`, `Common`, Spring/JPA infrastructure |

`DeliveryNetworkImpl` must not declare a direct dependency on another bounded context's API or implementation. Its
cross-context API dependencies are declared by `DeliveryNetworkApi`.

## Package structure

The implementation follows the convention established in `ShipmentManagement`:

```text
com.warehouse.deliverynetwork
|-- domain
|   |-- model
|   |-- vo
|   |-- service
|   |-- event
|   `-- exception
|-- application
|   |-- port
|   |   |-- primary
|   |   |   |-- command
|   |   |   `-- result
|   |   `-- secondary
|   |-- service
|   `-- listener
|-- infrastructure
|   `-- adapter
|       |-- primary
|       `-- secondary
`-- configuration
```

## Domain language

| Term | Meaning |
|---|---|
| `DeliveryNetwork` | The complete delivery graph configured for one operator. It is the aggregate root. |
| `DepartmentConnection` | One direct, bidirectional delivery connection between two departments. |
| `DepartmentNode` | A read-only view of a department needed to validate the graph: ID, code, type, and status. It is not owned or persisted by this context. |
| `DeliveryPath` | An ordered list of departments through which a shipment must travel. |

`DepartmentConnection` is an undirected edge. The pair is canonicalized by department ID, so these inputs describe
the same connection:

```text
KT1 -> NCS
NCS -> KT1
```

Only one connection is held in the domain and stored in the database. Read models and API responses may present it
as `KT1 <-> NCS`; they must not create a second reverse row.

## Aggregate and invariants

There is one `DeliveryNetwork` aggregate per `OperatorId`. The aggregate contains a set of canonical
`DepartmentConnection` values.

Hard invariants:

1. A connection joins two different departments.
2. Both departments exist and belong to the current operator.
3. A canonical department pair can occur only once.
4. Archived and deleted departments cannot be added to new connections.
5. Every non-sorting department participating in the operator's network has a direct connection to at least one
   non-archived, non-deleted department of type `SORTING_FACILITY`.
6. Sorting facilities are exempt from rule 5. This matches the current GUI validation.
7. The aggregate is replaced only when the complete proposed graph satisfies all invariants.

The current business rule does not guarantee that the entire graph is connected. Two regional groups may each meet
the sorting-facility rule and still have no route between them. Full graph connectivity should remain a separate
business decision. Until it is required, path finding can return `no path` for disconnected departments.

## Application use cases

The primary application port should expose operations at the graph level:

| Use case | Purpose |
|---|---|
| `getCurrentNetwork` | Return the current operator's canonical connections. |
| `replaceCurrentNetwork` | Validate and atomically replace the complete set submitted by the editor. |
| `areDirectlyConnected` | Check whether a shipment can move directly between two departments. |
| `findDeliveryPath` | Return the shortest path by number of department hops. |

The GUI should eventually save the complete relation set with one request. Separate `add` and `delete` writes are not
the preferred persistence contract because an intermediate request could temporarily violate the sorting-facility
invariant.

`replaceCurrentNetwork` application flow:

1. Resolve the current `OperatorId` from `OperatorContextProvider`.
2. Load the operator's department directory through `DepartmentDirectoryServicePort`.
3. Build canonical connections from the submitted department ID pairs.
4. Ask the `DeliveryNetwork` aggregate to validate and replace its connections.
5. Persist the aggregate in one transaction.
6. Publish `DeliveryNetworkChanged` after persistence succeeds.

The initial route finder uses breadth-first search because all direct connections have the same cost. The result
contains both endpoints and every intermediate department. A future version may introduce weighted connections for
transit time, distance, capacity, or service availability without changing ownership of the graph.

## Ports and module integration

Planned secondary ports in `DeliveryNetworkImpl`:

| Port | Responsibility |
|---|---|
| `DeliveryNetworkRepository` | Load and persist the current operator's network aggregate. The repository adapter uses `BaseRepository` tenant filtering, so application code does not pass `OperatorId` to repository queries. |
| `DepartmentDirectoryServicePort` | Supply the department data required for validation. |

`DepartmentDirectoryServiceAdapter` implements `DepartmentDirectoryServicePort` and calls `DepartmentServiceApi`.
It maps the external module DTO into the internal `DepartmentNode` value object. No Department JPA entity crosses the
module boundary.

`DepartmentServiceApi` exposes `DepartmentDirectoryEntryDto` as a strongly typed, general department-directory
projection containing:

- `DepartmentIdDto`
- `DepartmentCodeDto`
- `DepartmentTypeDto`
- `DepartmentStatusDto`
- optional coordinates for the network map

The API query remains implicitly scoped to the current operator. `DeliveryNetwork` must still verify tenant ownership
and must never accept an `OperatorId` supplied by the GUI as the authority for a write.

`DeliveryNetworkApiService` exposes stable, read-only cross-module contracts:

- `getCurrentNetwork` returning canonical `DepartmentConnectionDto` values;
- `areDirectlyConnected` using typed `DepartmentIdDto` values;
- `findDeliveryPath` returning an ordered `DeliveryPathDto`.

The editor HTTP controller belongs to `DeliveryNetworkImpl` as a primary adapter. It is not part of
`DeliveryNetworkApi`.

The editor uses `GET /delivery-networks/current` and `PUT /delivery-networks/current`. The write request contains the
complete set of canonical department pairs with typed department IDs. HTTP `DepartmentIdApi.value` is serialized as
a decimal string so JavaScript clients cannot lose `Long` precision; the primary mapper converts it to `DepartmentId`.
Neither endpoint accepts `OperatorId`; tenant scope comes exclusively from the authenticated operator context.

## Persistence design

Recommended table names are `delivery_network` and `delivery_network_connection`, not
`department_connection_relations`. The data represents edges owned by the delivery network rather than relations
owned by the Department aggregate.

### `delivery_network`

| Column | Purpose |
|---|---|
| `delivery_network_id` | Technical primary key represented in persistence by `DeliveryNetworkId`. |
| `operator_id` | Unique tenant owner inherited by the persistence entity from `BelongsToOperator`. |
| `version` | Optimistic-lock version for concurrent editor sessions. |
| `updated_at` | Last successful replacement time. |

### `delivery_network_connection`

| Column | Purpose |
|---|---|
| `connection_id` | Technical primary key represented in persistence by `DeliveryNetworkConnectionId`. |
| `delivery_network_id` | Parent delivery network represented by `DeliveryNetworkId`. Tenant isolation is inherited through the parent. |
| `first_department_id` | Lower canonical `DepartmentId`. |
| `second_department_id` | Higher canonical `DepartmentId`. |

Database constraints:

- unique `operator_id` in `delivery_network`;
- unique `(delivery_network_id, first_department_id, second_department_id)`;
- check `first_department_id < second_department_id`;
- foreign key from `operator_id` to the operator table;
- foreign key from `delivery_network_id` to the parent network;
- foreign keys from both department columns to `department.department_id`;
- indexes on `(delivery_network_id, first_department_id)` and `(delivery_network_id, second_department_id)`;
- no cascade delete from departments; department lifecycle is handled explicitly and old connections remain visible
  until the network is reconciled.

Persistence entities store typed identifiers and must not define JPA relationships to Department entities from
another module. `DeliveryNetworkEntity` extends `BelongsToOperator`, and `DeliveryNetworkRepositoryImpl` queries it
through the tenant filtering already provided by `BaseRepository`.

During replacement, persistence reconciles the child collection by its canonical department pair. Unchanged
connections retain their existing `DeliveryNetworkConnectionId`; only removed pairs are deleted and only new pairs
are inserted. This avoids inserting a replacement row before Hibernate deletes the old row protected by the unique
pair constraint.

## Department lifecycle

The network must be revalidated when a department is archived, deleted, or changes type. The target design is an
application listener in `DeliveryNetworkImpl` consuming a stable integration event published by `DepartmentService`.

The listener should not silently invent a replacement sorting connection. It may remove connections that can no
longer be used and mark the network as requiring configuration. The operator must explicitly choose the new graph.
Until Department integration events exist, `getCurrentNetwork` and `replaceCurrentNetwork` should validate against the
current department directory so stale connections are never treated as usable routes.

## Events

The domain event is `DeliveryNetworkChanged`, containing an immutable network snapshot and timestamp. The application
use case publishes it only after a successful transaction.

An integration event is added only when another bounded context needs an asynchronous network snapshot. Synchronous
shipment routing should initially use `DeliveryNetworkApi`; it must not subscribe to internal domain events or query
the delivery-network tables.

## Error model

The design needs explicit failures for:

- self-connection;
- duplicate connection;
- unknown department;
- department owned by another operator;
- archived or deleted department;
- missing direct sorting-facility connection, including all offending department codes;
- missing delivery path;
- concurrent network modification.

Validation should report all departments missing a sorting-facility connection in one result so the GUI can preserve
its current consolidated warning.

## Implementation sequence

1. **Completed:** create the Maven module skeleton and dependency boundaries.
2. **Completed:** add the department-directory projection to `DepartmentServiceApi`.
3. **Completed:** implement the pure domain model and domain tests.
4. **Completed:** implement application ports, the full-network replacement use case, and route finding.
5. **Completed:** add persistence adapters and Liquibase changesets.
6. **Completed:** add `DeliveryNetworkApi` adapters for internal consumers.
7. **Completed:** add the editor controller and connect the GUI with an atomic full-network replacement.
8. Add department lifecycle integration and architecture tests.

## Required tests

- reversed pairs are equal and stored once;
- self-connections and duplicates are rejected;
- every non-sorting department is checked against an eligible sorting facility;
- sorting facilities themselves are exempt from that check;
- archived and deleted departments cannot satisfy validation;
- networks and queries are isolated by operator;
- replacement is atomic and rejects stale optimistic-lock versions;
- direct paths contain two departments;
- indirect paths contain the expected intermediate departments;
- disconnected departments return `no path`;
- domain and application packages have no infrastructure dependencies.
