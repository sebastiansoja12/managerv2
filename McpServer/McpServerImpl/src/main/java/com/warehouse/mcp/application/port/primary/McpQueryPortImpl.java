package com.warehouse.mcp.application.port.primary;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.application.port.primary.result.DepartmentList;
import com.warehouse.mcp.application.port.primary.result.DeviceList;
import com.warehouse.mcp.application.port.primary.result.ShipmentOverview;
import com.warehouse.mcp.application.port.secondary.CurrentUserServicePort;
import com.warehouse.mcp.application.port.secondary.DeliveryNetworkServicePort;
import com.warehouse.mcp.application.port.secondary.DepartmentServicePort;
import com.warehouse.mcp.application.port.secondary.DeviceServicePort;
import com.warehouse.mcp.application.port.secondary.ProcessServicePort;
import com.warehouse.mcp.application.port.secondary.ShipmentServicePort;
import com.warehouse.mcp.domain.exception.McpToolAccessException;
import com.warehouse.mcp.domain.service.PersonalDataMasking;
import com.warehouse.mcp.domain.vo.CurrentUserProfile;
import com.warehouse.mcp.domain.vo.DepartmentDetails;
import com.warehouse.mcp.domain.vo.DepartmentEntry;
import com.warehouse.mcp.domain.vo.DepartmentFilter;
import com.warehouse.mcp.domain.vo.DeviceAccess;
import com.warehouse.mcp.domain.vo.DeviceFilter;
import com.warehouse.mcp.domain.vo.ProcessDetails;
import com.warehouse.mcp.domain.vo.ShipmentRoute;
import com.warehouse.mcp.domain.vo.ShipmentTracking;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.mcp.application.port.primary.result.ConfigurationView;
import com.warehouse.mcp.application.port.primary.result.CourierList;
import com.warehouse.mcp.application.port.primary.result.DeliveryList;
import com.warehouse.mcp.application.port.primary.result.DeliveryOverview;
import com.warehouse.mcp.domain.vo.ConfigurationSection;
import com.warehouse.mcp.domain.vo.DeliveryFilter;
import com.warehouse.mcp.application.port.secondary.ConfigurationServicePort;
import com.warehouse.mcp.application.port.secondary.CourierServicePort;
import com.warehouse.mcp.application.port.secondary.DeliveryServicePort;
import com.warehouse.mcp.domain.vo.Courier;
import com.warehouse.mcp.domain.vo.DeliveryDetails;
import java.util.function.Function;

public class McpQueryPortImpl implements McpQueryPort {

    static final int MAX_SHIPMENT_EVENTS = 10;
    static final int MAX_DEVICES = 50;
    static final int MAX_DEPARTMENTS = 100;
    static final int DEFAULT_DELIVERIES = 25;
    static final int MAX_DELIVERIES = 100;
    static final int MAX_COURIERS = 100;

    private final CurrentUserServicePort currentUserServicePort;

    private final ShipmentServicePort shipmentServicePort;

    private final ProcessServicePort processServicePort;

    private final DeviceServicePort deviceServicePort;

    private final DepartmentServicePort departmentServicePort;

    private final DeliveryNetworkServicePort deliveryNetworkServicePort;

    private final DeliveryServicePort deliveryServicePort;

    private final CourierServicePort courierServicePort;

    private final ConfigurationServicePort configurationServicePort;

    private final PersonalDataMasking personalDataMasking;

    private final McpReferences references;

    public McpQueryPortImpl(final CurrentUserServicePort currentUserServicePort,
                            final ShipmentServicePort shipmentServicePort,
                            final ProcessServicePort processServicePort,
                            final DeviceServicePort deviceServicePort,
                            final DepartmentServicePort departmentServicePort,
                            final DeliveryNetworkServicePort deliveryNetworkServicePort,
                            final DeliveryServicePort deliveryServicePort,
                            final CourierServicePort courierServicePort,
                            final ConfigurationServicePort configurationServicePort,
                            final PersonalDataMasking personalDataMasking) {
        this.currentUserServicePort = currentUserServicePort;
        this.shipmentServicePort = shipmentServicePort;
        this.processServicePort = processServicePort;
        this.deviceServicePort = deviceServicePort;
        this.departmentServicePort = departmentServicePort;
        this.deliveryNetworkServicePort = deliveryNetworkServicePort;
        this.deliveryServicePort = deliveryServicePort;
        this.courierServicePort = courierServicePort;
        this.configurationServicePort = configurationServicePort;
        this.personalDataMasking = personalDataMasking;
        this.references = new McpReferences(departmentServicePort, courierServicePort);
    }

    @Override
    public CurrentUserProfile currentUser() {
        return this.currentUserServicePort.currentUser();
    }

    @Override
    public ShipmentOverview findShipment(final TrackingNumber trackingNumber) {
        final ShipmentTracking tracking = this.shipmentServicePort.findShipmentTracking(trackingNumber);
        return new ShipmentOverview(
                tracking.shipmentId(),
                tracking.trackingNumber(),
                tracking.status(),
                tracking.createdAt(),
                tracking.updatedAt(),
                this.personalDataMasking.mask(tracking.sender()),
                this.personalDataMasking.mask(tracking.recipient()),
                tracking.events().size(),
                tracking.events().stream().limit(MAX_SHIPMENT_EVENTS).toList());
    }

    @Override
    public ProcessDetails findProcess(final ProcessId processId) {
        return this.processServicePort.findProcess(processId);
    }

    @Override
    public DeviceList listDevices(final DeviceFilter filter) {
        if (!this.currentUserServicePort.currentUser().canManageDevices()) {
            throw new McpToolAccessException("Listing devices requires the MANAGER role or higher");
        }
        final List<DeviceAccess> devices = this.deviceServicePort.findAllDevices().stream()
                .filter(filter::matches)
                .toList();
        return new DeviceList(devices.size(), devices.stream().limit(MAX_DEVICES).toList());
    }

    @Override
    public DepartmentList listDepartments(final DepartmentFilter filter) {
        final List<DepartmentEntry> departments = this.departmentServicePort.findAllDepartments().stream()
                .filter(filter::matches)
                .toList();
        return new DepartmentList(departments.size(), departments.stream().limit(MAX_DEPARTMENTS).toList());
    }

    @Override
    public DepartmentDetails findDepartment(final DepartmentCode departmentCode) {
        return this.departmentServicePort.findDepartment(departmentCode);
    }

    @Override
    public ShipmentRoute findShipmentRoute(final TrackingNumber trackingNumber) {
        return this.deliveryNetworkServicePort.findShipmentRoute(trackingNumber);
    }

    @Override
    public DeliveryList listDeliveries(final DeliveryFilter filter) {
        final int limit = filter.limit() == null ? DEFAULT_DELIVERIES
                : Math.max(1, Math.min(filter.limit(), MAX_DELIVERIES));
        final Function<DeliveryDetails, DeliveryOverview> overview =
                this.references.overviews();
        return new DeliveryList(this.deliveryServicePort.findDeliveries(
                        this.references.departmentId(filter.departmentCode()),
                        this.references.courierId(filter.courierCode()), limit).stream()
                .map(overview)
                .toList());
    }

    @Override
    public DeliveryOverview findDelivery(final DeliveryId deliveryId) {
        return this.references.overviews().apply(this.deliveryServicePort.findDelivery(deliveryId));
    }

    @Override
    public DeliveryOverview findShipmentDelivery(final TrackingNumber trackingNumber) {
        return this.references.overviews().apply(this.deliveryServicePort.findDeliveryByShipment(
                this.shipmentServicePort.findShipmentTracking(trackingNumber).shipmentId()));
    }

    @Override
    public CourierList listCouriers() {
        if (!this.currentUserServicePort.currentUser().canManageDevices()) {
            throw new McpToolAccessException("Listing couriers requires the MANAGER role or higher");
        }
        final List<Courier> couriers = this.courierServicePort.findCouriers();
        return new CourierList(couriers.size(), couriers.stream().limit(MAX_COURIERS).toList());
    }

    @Override
    public ConfigurationView findConfiguration(final ConfigurationSection section) {
        return new ConfigurationView(section, this.configurationServicePort.find(section));
    }
}
