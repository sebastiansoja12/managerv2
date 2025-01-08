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
- Old route process - to be deleted in 2024.2

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
