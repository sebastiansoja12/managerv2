package com.warehouse.mcp.application.port.primary;

import java.util.Map;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.mcp.application.port.primary.result.ConfigurationView;
import com.warehouse.mcp.application.port.primary.result.DeliveryOverview;
import com.warehouse.mcp.application.port.primary.result.ShipmentOverview;
import com.warehouse.mcp.application.port.secondary.ConfigurationServicePort;
import com.warehouse.mcp.application.port.secondary.CourierServicePort;
import com.warehouse.mcp.application.port.secondary.CurrentUserServicePort;
import com.warehouse.mcp.application.port.secondary.DeliveryServicePort;
import com.warehouse.mcp.application.port.secondary.DepartmentServicePort;
import com.warehouse.mcp.application.port.secondary.ShipmentServicePort;
import com.warehouse.mcp.domain.exception.McpInvalidRequestException;
import com.warehouse.mcp.domain.exception.McpToolAccessException;
import com.warehouse.mcp.domain.service.ConfigurationMerge;
import com.warehouse.mcp.domain.vo.ConfigurationSection;
import com.warehouse.mcp.domain.vo.CourierChange;
import com.warehouse.mcp.domain.vo.CourierDraft;
import com.warehouse.mcp.domain.vo.CurrentUserProfile;
import com.warehouse.mcp.domain.vo.DeliveryConditions;
import com.warehouse.mcp.domain.vo.DeliveryDetails;
import com.warehouse.mcp.domain.vo.DeliveryRoute;
import com.warehouse.mcp.domain.vo.DeliveryRouteChange;
import com.warehouse.mcp.domain.vo.DepartmentAddress;
import com.warehouse.mcp.domain.vo.DepartmentDetails;
import com.warehouse.mcp.domain.vo.PartyRole;
import com.warehouse.mcp.domain.vo.ShipmentDraft;
import com.warehouse.mcp.domain.vo.ShipmentParty;
import com.warehouse.mcp.domain.vo.ShipmentPartyChange;
import com.warehouse.mcp.domain.vo.ShipmentTracking;

public class McpCommandPortImpl implements McpCommandPort {

    private final CurrentUserServicePort currentUserServicePort;

    private final ShipmentServicePort shipmentServicePort;

    private final DepartmentServicePort departmentServicePort;

    private final DeliveryServicePort deliveryServicePort;

    private final CourierServicePort courierServicePort;

    private final ConfigurationServicePort configurationServicePort;

    private final McpQueryPort mcpQueryPort;

    private final ConfigurationMerge configurationMerge;

    private final McpReferences references;

    private final boolean writeEnabled;

    public McpCommandPortImpl(final CurrentUserServicePort currentUserServicePort,
                              final ShipmentServicePort shipmentServicePort,
                              final DepartmentServicePort departmentServicePort,
                              final DeliveryServicePort deliveryServicePort,
                              final CourierServicePort courierServicePort,
                              final ConfigurationServicePort configurationServicePort,
                              final McpQueryPort mcpQueryPort,
                              final ConfigurationMerge configurationMerge,
                              final boolean writeEnabled) {
        this.currentUserServicePort = currentUserServicePort;
        this.shipmentServicePort = shipmentServicePort;
        this.departmentServicePort = departmentServicePort;
        this.deliveryServicePort = deliveryServicePort;
        this.courierServicePort = courierServicePort;
        this.configurationServicePort = configurationServicePort;
        this.mcpQueryPort = mcpQueryPort;
        this.configurationMerge = configurationMerge;
        this.references = new McpReferences(departmentServicePort, courierServicePort);
        this.writeEnabled = writeEnabled;
    }

    @Override
    public DeliveryOverview assignCourier(final DeliveryId deliveryId, final SupplierCode courierCode,
                                          final VehicleId vehicleId) {
        requireChangeAccess();
        return overview(this.deliveryServicePort.assignCourier(deliveryId, this.references.courierId(courierCode),
                vehicleId));
    }

    @Override
    public DeliveryOverview unassignCourier(final DeliveryId deliveryId) {
        requireChangeAccess();
        return overview(this.deliveryServicePort.unassignCourier(deliveryId));
    }

    @Override
    public DeliveryOverview changeDeliveryRoute(final DeliveryId deliveryId, final DeliveryRouteChange route) {
        requireChangeAccess();
        final DeliveryDetails current = this.deliveryServicePort.findDelivery(deliveryId);
        final DeliveryRoute changed = new DeliveryRoute(
                route.originDepartmentCode() != null ? this.references.departmentId(route.originDepartmentCode())
                        : current.originDepartmentId(),
                route.destinationDepartmentCode() != null
                        ? this.references.departmentId(route.destinationDepartmentCode())
                        : current.destinationDepartmentId(),
                route.pickupMethod() != null ? route.pickupMethod() : current.pickupMethod(),
                route.pickupPointId() != null ? route.pickupPointId() : current.pickupPointId(),
                route.deliveryPickupPointId() != null ? route.deliveryPickupPointId()
                        : current.deliveryPickupPointId());
        return overview(this.deliveryServicePort.changeRoute(deliveryId, changed));
    }

    @Override
    public DeliveryOverview changeDeliveryConditions(final DeliveryId deliveryId,
                                                     final DeliveryConditions conditions) {
        requireChangeAccess();
        final DeliveryDetails current = this.deliveryServicePort.findDelivery(deliveryId);
        return overview(this.deliveryServicePort.changeConditions(deliveryId, conditions.applyTo(current)));
    }

    @Override
    public DeliveryOverview commentDeliveryStep(final DeliveryId deliveryId, final int stepNumber,
                                                final String comment) {
        requireChangeAccess();
        return overview(this.deliveryServicePort.commentStep(deliveryId, stepNumber, comment));
    }

    @Override
    public DeliveryOverview correctDeliveryStepStatus(final DeliveryId deliveryId, final int stepNumber,
                                                      final String deliveryStatus) {
        requireChangeAccess();
        return overview(this.deliveryServicePort.correctStepStatus(deliveryId, stepNumber, deliveryStatus));
    }

    @Override
    public void cancelDelivery(final DeliveryId deliveryId) {
        requireChangeAccess();
        this.deliveryServicePort.cancel(deliveryId);
    }

    @Override
    public TrackingNumber createShipment(final ShipmentDraft shipment) {
        requireChangeAccess();
        return this.shipmentServicePort.createShipment(shipment);
    }

    @Override
    public ShipmentOverview changeShipmentParty(final TrackingNumber trackingNumber, final PartyRole role,
                                                final ShipmentPartyChange change) {
        requireChangeAccess();
        final ShipmentTracking shipment = this.shipmentServicePort.findShipmentTracking(trackingNumber);
        final ShipmentParty current = role == PartyRole.SENDER ? shipment.sender() : shipment.recipient();
        this.shipmentServicePort.changeParty(shipment.shipmentId(), role, change.applyTo(current));
        return this.mcpQueryPort.findShipment(trackingNumber);
    }

    @Override
    public DepartmentDetails changeDepartmentAddress(final DepartmentCode departmentCode,
                                                     final DepartmentAddress address) {
        requireChangeAccess();
        this.departmentServicePort.changeAddress(departmentCode, address);
        return this.departmentServicePort.findDepartment(departmentCode);
    }

    @Override
    public DepartmentDetails changeDepartmentType(final DepartmentCode departmentCode, final String type) {
        requireChangeAccess();
        this.departmentServicePort.changeType(departmentCode, type);
        return this.departmentServicePort.findDepartment(departmentCode);
    }

    @Override
    public DepartmentDetails changeDepartmentEmail(final DepartmentCode departmentCode, final String email) {
        requireChangeAccess();
        this.departmentServicePort.changeEmail(departmentCode, email);
        return this.departmentServicePort.findDepartment(departmentCode);
    }

    @Override
    public DepartmentDetails changeDepartmentStatus(final DepartmentCode departmentCode, final String status,
                                                    final String reason) {
        requireChangeAccess();
        if (!currentUser().isAdministrator()) {
            throw new McpToolAccessException("Changing department status requires the ADMIN role or higher");
        }
        this.departmentServicePort.changeStatus(departmentCode, status, reason);
        return this.departmentServicePort.findDepartment(departmentCode);
    }

    @Override
    public DepartmentDetails changeDepartmentActive(final DepartmentCode departmentCode, final boolean active,
                                                    final String reason) {
        requireChangeAccess();
        this.departmentServicePort.changeActive(departmentCode, active, reason);
        return this.departmentServicePort.findDepartment(departmentCode);
    }

    @Override
    public SupplierCode createCourier(final CourierDraft courier) {
        requireChangeAccess();
        if (courier.email() == null || courier.email().isBlank()) {
            throw new McpInvalidRequestException("Email is required to create the courier's user account");
        }
        final CourierDraft draft = courier.departmentCode() != null ? courier : new CourierDraft(
                courier.courierCode(), courier.firstName(), courier.lastName(), courier.telephoneNumber(),
                courier.email(), new DepartmentCode(currentUser().departmentCode()));
        return this.courierServicePort.create(draft);
    }

    @Override
    public void updateCourier(final CourierChange change) {
        requireChangeAccess();
        final CourierChange merged = this.courierServicePort.findCouriers().stream()
                .filter(courier -> change.courierCode().equals(courier.courierCode()))
                .findFirst()
                .map(change::applyTo)
                .orElse(change);
        if (merged.firstName() == null || merged.lastName() == null || merged.telephoneNumber() == null) {
            throw new McpInvalidRequestException("Courier " + change.courierCode().value()
                    + " is not in your department; provide firstName, lastName and telephoneNumber");
        }
        this.courierServicePort.update(merged);
    }

    @Override
    public void changeCourierActive(final SupplierCode courierCode, final boolean active) {
        requireChangeAccess();
        if (active) {
            this.courierServicePort.activate(courierCode);
        } else {
            this.courierServicePort.deactivate(courierCode);
        }
    }

    @Override
    public ConfigurationView updateConfiguration(final ConfigurationSection section,
                                                 final Map<String, Object> changes) {
        requireChangeAccess();
        final Map<String, Object> merged = this.configurationMerge.merge(
                this.configurationServicePort.find(section), changes);
        return new ConfigurationView(section, this.configurationServicePort.update(section, merged));
    }

    private DeliveryOverview overview(final DeliveryDetails delivery) {
        return this.references.overviews().apply(delivery);
    }

    private void requireChangeAccess() {
        if (!this.writeEnabled) {
            throw new McpToolAccessException("Changing data over MCP is disabled on this server (MCP_WRITE_ENABLED)");
        }
        if (!currentUser().canChangeData()) {
            throw new McpToolAccessException("Changing data requires the MANAGER role or higher");
        }
    }

    private CurrentUserProfile currentUser() {
        return this.currentUserServicePort.currentUser();
    }
}
