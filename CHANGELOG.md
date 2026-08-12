## [2026.3] - 2026-08-12

### Added
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
