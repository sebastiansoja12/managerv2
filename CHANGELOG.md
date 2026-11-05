## [2026.4] - Unreleased

### Added
- FleetTracking API and implementation modules with driver tracking sessions, batched GPS position ingestion from terminals, accuracy, sequence and plausible-speed filtering, last known vehicle positions, route history and `tracking_session`/`tracking_position` tables.
- `/fleet-tracking/sessions` REST endpoints for starting, reporting positions to, finishing and listing active tracking sessions and reading a session route.
- IncidentManagement API and implementation modules for reporting, assigning, commenting, resolving and reopening shipment incidents with statistics and operator-scoped persistence.
- AuditLog module consuming audit entries from Kafka and exposing them through a paginated REST endpoint, with audit recording for shipments, deliveries, pallets, pickup points and the delivery network.
- PalletManagement module with pallet lists, details, documents, carrier assignment, pallet transports and pallet unloading.
- DepartmentMap module with department layouts, storage locations, directions and pallet placement, movement and reservation.
- McpServer module exposing read-only and command tools for shipments, deliveries, departments, couriers, devices, processes and configuration with personal-data masking.
- PlatformAdministration module for platform owners: operator users, departments, couriers, devices, API keys, sessions, access blocking, read-model rebuilds, data export and erasure, announcements, system status, maintenance mode and platform audit log.
- Operator subscriptions, superadmin impersonation with operator consent and impersonation tokens.
- User onboarding with required consents, consent versions and tutorial status.
- Department lifecycle with status policies, daily capacity, configuration overrides, archiving of inactive departments and lifecycle participants in delivery network and logistics.
- Department load endpoint, shipment acceptance and capacity-based destination department balancing in LogisticsOrchestrator.
- Delivery model in LogisticsOrchestrator with routing data, delivery steps, step comments and comment codes, read model, change history, related deliveries and recipient signatures.
- Delivery areas, postal codes with coordinates and delivery-department resolution from delivery areas.
- Carriers, drivers and carrier vehicles in DriverHub, with courier vehicle and device assignment.
- Device activation with one-time activation codes, hashed pair keys and operator sessions for terminals and phones.
- Supplier configuration, supplier user accounts created through `SupplierCreatedEvent` and courier invitation e-mails.
- Template-based mail service over SMTP driven by shipment Kafka events, including collected, reroute and external shipment templates.
- Operator configuration for shipment policies, notifications, labels, tracking number prefixes, sorting, pickup points, fleet communication and regional settings.
- `COLLECTED` shipment status, shipment content, parcel details, lifecycle timestamps, shipment source and party country codes.
- Organization chat participants endpoint.
- Common identifiers `IncidentId`, `TrackingSessionId` and `TrackingPositionId`.

### Changed
- Extracted deliveries from shipments into the hexagonal `logisticsdelivery` context; shipments now read routing data from deliveries.
- Moved recipient signatures from shipments to deliveries.
- Rebuilt AsyncJob and the legacy logistics package as hexagonal DDD modules.
- Terminal requests use tracking numbers instead of shipment ids and are resolved in DeliveryOperation.
- Moved QR code and CSV generators into DocumentStorage and build labels from shipment and delivery contracts.
- Kafka outbox publishes on a dedicated executor with capped exponential backoff and validates operator headers in the record interceptor.
- Log lines resolve operator and user from the security context.
- Modeled organization users as a role-based account hierarchy with account levels and audiences.
- Department code is carried explicitly instead of through the deprecated department ownership base class.
- Restricted `/fleet-tracking/**` endpoints to authenticated users.

### Fixed
- Driver license and dangerous good certification invalidation job for suppliers.
- Bean cycle when reading the platform owner username.
- Unique current delivery target and missing delivery destination department resolution.
- Operator context leaking between Kafka records.

### Deprecated
- Removed the PathFinder lookup from shipment management in favor of delivery areas.
- Removed the standalone shipment dangerous good table and the deprecated software configuration service.

## [2026.3] - 2026-09-10

### Added
- PickupPointManagement API and implementation modules with pickup-point types, capabilities, shipment-size policies, opening schedules, lifecycle rules and operator-scoped persistence.
- Pickup-point REST endpoints for creation, editing, status changes, detail lookup, eligibility checks and paginated search by text, street, city, country, network and map bounds.
- Automatic pickup-point coordinate resolution and shipment delivery-department determination through the existing coordinates and routing modules.
- Dedicated `pickup_point_read_model` table synchronized through domain events, application-level integration-event translation, Kafka and a primary-adapter listener.
- Pickup-point identifiers and sender/recipient pickup-point assignments in shipment commands, snapshots, events, entities, read models and API responses.
- Shipment pickup and delivery methods, including pickup-point and locker flows.
- Required target delivery department assignment when a shipment is created.
- `PLANNED` shipment status across shared, delivery, message, organisation and shipment contracts.
- OrganizationChat API and implementation modules with persistent direct conversations, message notifications and WebSocket presence tracking.
- Shipment application results and a result factory for assembling department, route and optional return information before REST mapping.
- Return search, processing and completion operations exposed through the Manager shipment API.
- Operator-aware domain model across organisation, authorization, departments, shipments, suppliers, returns, processes and devices.
- OrganisationStructure module with operator aggregate, configuration, management endpoints and provisioning events.
- Initial operator provisioning for departments and users after operator creation.
- Operator identity in JWT claims, current-user/current-operator APIs and MDC logging context.
- Database changelogs for operators, operator configuration, audit tables, process logs, communication logs, shipment read models and supporting dictionaries.
- GeocodingService module with provider configuration, encrypted API keys, geocoding requests, result snapshots and REST API.
- User and role-permission management APIs with operator-scoped access control.
- Department read model synchronization, numeric department identifiers and map coordinates.
- Kafka domain-event infrastructure, outbox support and shipment lifecycle events.
- RouteTracker integration with JWT authentication, current user context, shipment event consumption and enriched route history data.
- Enriched shipment history service with readable department and user labels.
- Dangerous goods data embedded in shipment APIs, CSV export and tests.
- InPost tracking integration API.

### Changed
- Kept pickup-point state transitions in the domain model while making persistence entities immutable representations mapped from snapshots.
- Moved pickup-point search to its separate read-model entity and table, with operator filtering provided by `BelongsToOperator` and `OperatorFilteredRepository`.
- Reworked pickup-point projection flow to publish a domain event after persistence, translate it in the application layer and synchronize the read model through Kafka.
- Replaced client-provided pickup-point coordinates with automatic address-based resolution.
- Changed shipment loading ports to return application results instead of exposing `Shipment` to primary adapters.
- Centralized shipment result construction and delivery-department-code resolution in `ShipmentResultFactory`.
- Routed return list and status requests through Manager and the generic `ExternalFeignClient` integration.
- Limited return-detail lookup to shipments in `RETURN` status and made optional return enrichment tolerant of ReturningTrackManager outages.
- Added the `CREATED -> PROCESSING -> COMPLETED` return lifecycle while preserving cancellation rules.
- Renamed module implementations from `Core` to `Impl`.
- Scoped common repositories, process logs, suppliers, shipments, returns and departments by operator context.
- Reworked authentication responses, refresh-token handling and auth cookie configuration.
- Refactored supplier handling with delivery area, department code, vehicle and supported package type updates.
- Refactored geocoding and PathFinder integration around configurable providers.
- Extracted RouteTracker from embedded shipment processing and moved tracking communication to external services.
- Reworked RouteTracker request contracts to use identifiers instead of code-based lookup values.
- Removed the legacy RouteLogger module from application configuration and the Maven reactor.
- Updated Docker, Eureka, Gateway, CORS and local development configuration.

### Fixed
- Pickup-point read-model synchronization after restart and map searches by street, city and bounding box.
- Persistence of the recipient delivery pickup-point identifier on newly created shipments.
- Pickup-point lookup support used by clients to present point codes instead of raw UUIDs.
- Shipment response mapping failures caused by stale generated MapStruct implementations and constructor signature changes.
- Shipment detail loading when ReturningTrackManager is unavailable for shipments without returns.
- Manager return-processing endpoints required by clients that must not call ReturningTrackManager through the gateway directly.
- Shipment department country mapping.
- Route detail ownership persistence in RouteTracker.
- Kafka and Docker configuration problems.
- Authentication and architecture tests after operator, Kafka and RouteTracker refactors.
- Voronoi, supplier, process hub and database fixtures after the read-model changes.

### Deprecated
- Legacy RouteLogger API, DTOs, adapters, events, ports and module descriptors.
- Obsolete shipment assignment events and old route-history notifier.
- Legacy dangerous goods standalone module.

## [2026.2] - 2026-07-01

### Added
- Authorization API support required by the GUI.
- Tracking numbers and device-related methods for shipment and terminal flows.
- WSDL endpoint for external device communication.
- Device pairing from UI and device port implementations.
- Feign clients and generic Feign client infrastructure.
- Language support in API responses.
- Logistics availability, aspects, configuration and read model.
- Process logs for delivery operations.
- Finished delivery reject process with tests.

### Changed
- Device identifiers migrated to string values.
- Department code handling updated.
- Shipment and delivery reject APIs aligned with the new logistics flow.

### Fixed
- Liquibase configuration.
- XSD for redirect-domain removal.
- Shipment and device tests.

### Deprecated
- Redirect domain.

## [2026.1] - 2026-04-24

### Added
- ProcessHub implementation with JPA adapters, events, ports and test endpoints.
- Communication log methods and process-log read/write support.
- Finish-process handling.
- Delivery-shipment flow.
- Shipment tracking numbers and external identifiers.
- JWT/test profile settings for the 2026 branch.

### Changed
- Return token handling.
- Domain context naming.
- Application profiles and Liquibase setup.
- Device identifiers prepared for string-based handling.

### Fixed
- Application startup.
- Architecture tests.
- Communication log persistence.
- Tests after profile and device-id changes.

### Deprecated
- Microservice exclusions that are no longer part of the monolith runtime.

## [2025.3] - 2025-12-27

### Added
- Gateway module and Spring Boot 3.5.5 upgrade.
- Dangerous goods domain and API methods.
- SupplierServiceManagement, CrossLogistics, TransportAssets and DriverHub modules.
- ReturningTrackManager cancellation and completion flows with JWT context.
- Department administration features, including admin-user creation, identification-number change and activate/deactivate support.
- Refresh-token controller and authentication context refactor.
- Supplier domain with API DTOs, base repositories, common identifiers, device assignment and supported package types.
- Driver license, certification and supplier validation jobs.
- Returning shipment call and country-code validation for shipments.
- ProcessHub/process-log module and XSD support.
- External route and return identifiers for shipments.

### Changed
- Refactored department, authorization, pallet and user domains.
- Moved supplier domain to the new supplier module.
- Reworked returnings and shipment return integration.
- Updated PathFinder and shipment events.
- Changed country-code handling and Voronoi integration.

### Fixed
- Swagger configuration.
- Authorization and returning tests.
- QR code generation.
- Architecture tests.
- Voronoi processing and route tracking updates.

### Deprecated
- Older returning implementation inside the monolith.
- Obsolete supplier and department events/fields replaced by the new modules.

## [2025.2] - 2025-09-01

### Added
- Signature microservice separation and shipment signature support.
- Dangerous goods, prices, shipment priorities and country data for shipments.
- Person entity and person update flow.
- Shipment events.
- Country availability and signature availability services.
- Communication with RouteTracker from shipment flows.

### Changed
- Moved shipment API handling and creation flow.
- Updated route tracker database schema and configuration.
- Updated person and country models.
- Changed authentication integration for the release.

### Fixed
- Process flow and SQL scripts.
- Architecture tests.
- Shipment and validation issues around new country/signature data.

### Deprecated
- Old embedded Signature module location.

## [2025.1] - 2025-04-14

### Added
- DevicePair, Device and DeviceVersion repository migrations and tests.
- Delivery reject domain with validation, process type handling and device validation.
- Active flag for departments.
- Signature module and initial signature preparation.
- LogisticsOrchestrator module.
- CrossLogistics module.
- Device settings, device handler, update agent and update events.
- Empty pallet and seal number services.
- Currency and extended shipment data.

### Changed
- Renamed SupplyOperation to DeliveryOperation.
- Renamed Generator to DocumentManager.
- Moved delivery domain to LogisticsOrchestrator.
- Refactored return token, pallet handling, missed delivery and device pairing domains.
- Changed device user identifiers from `userId` to `username`.

### Fixed
- Device ID handling.
- Route tracking for device information.
- SQL scripts.
- Device pairing and terminal user handling.
- Tests after logistics and missed-domain refactors.

### Deprecated
- Redirect and reroute controllers from the old flow.
- Unused delivery reject requests/responses and adapters.

## [2024.7] - 2024-12-31

### Added
- New module Terminal
- Pairing devices
- Updating devices versions
- Delivery return
- Delivery reject
- Delivery missed
- New endpoint for deliveries
- New ShipmentManagement module
- New DepartmentService module
- New MessageProvider module
- Signatures for shipments

### Changed
- Update Spring Boot to version 3.4.1
- Software Configuration separated
- RouteTrackerFlow separated
- PalletHandlingManager separated
- Device request and response
- Reimplemented Shipment module
- Change Parcel id to Shipment id
- Rename depot to department

### Fixed
- Bugs with deliveries
- Problems with request and response from device

### Deprecated
- Reroute and redirect processes
- Old Device module

## [2024.6] - 2024-09-26

### Added
- Add PalletHandlingManager module

### Changed
- Zebra device properties
- Request and response of device
- Update Spring Boot to version 3.3.4
- Refactor tests

### Fixed
- Bugs

## [2024.5] - 2024-06-02

### Added
- Route logger module
- Route Tracker separate application
- Logging routes

### Changed
- Route process
- Delivery missed domain

## [2024.4] - 2024-05-04

### Added
- Extend delivery missed domain
- Route logger for logging informations via events
- Secondary adapters for route logging
- Module for Route logging

### Changed
- Route process
- Delivery missed domain

### Fixed
- Terminal requests not logging


## [2024.3] - 2024-04-06

### Added
- Delivery missed domain
- Route logger for logging informations via events
- Secondary adapters for route logging
- New process type
- Determination for parcel status
- Module for E2E tests

### Changed
- Route process
- Delivery domain
- Depot model extended
- Dummy data for depots

### Fixed
- -------

### Deprecated
- -------


## [2024.2] - 2024-02-05

### Added
- FFT-439 Add fields for depot
- New endpoints for saving information about route record

### Changed
- Route process
- Initialize in zebra module
- Delete old route controller

### Fixed
- Conflict between SoftwareConfig and main Application

### Deprecated
- -------

## [2024.1] - 2024-01-01

### Added
- FFT-385 isActive status for suppliers
- FFT-424 Adapter in ZebraIntegration module for initializing routes
- FFT-436 Delivery return conntection to return token domain in DeliveryReturnServiceAdapter
- FFT-443 Build SoftwareConfiguration module
- FFT-447 Create 'delivery-create' domain
- Finish epic for delivery protection

### Changed
- Update Spring Boot to version 3.2.2
- Update Java to version 21

### Fixed
- Problems with sending requests via RestClient between modules

### Deprecated
- Old route process - to be deleted in 2024.2
