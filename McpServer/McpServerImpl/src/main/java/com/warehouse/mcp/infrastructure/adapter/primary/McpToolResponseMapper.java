package com.warehouse.mcp.infrastructure.adapter.primary;

import java.time.Instant;
import java.time.LocalDateTime;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.application.port.primary.result.DepartmentList;
import com.warehouse.mcp.application.port.primary.result.DeviceList;
import com.warehouse.mcp.application.port.primary.result.ShipmentOverview;
import com.warehouse.mcp.domain.vo.CurrentUserProfile;
import com.warehouse.mcp.domain.vo.DepartmentDetails;
import com.warehouse.mcp.domain.vo.DepartmentEntry;
import com.warehouse.mcp.domain.vo.DeviceAccess;
import com.warehouse.mcp.domain.vo.MaskedShipmentParty;
import com.warehouse.mcp.domain.vo.ProcessDetails;
import com.warehouse.mcp.domain.vo.ProcessStep;
import com.warehouse.mcp.domain.vo.ShipmentEvent;
import com.warehouse.mcp.domain.vo.ShipmentRoute;
import com.warehouse.mcp.infrastructure.adapter.primary.response.CurrentUserToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DepartmentDetailsToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DepartmentListToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DepartmentToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DeviceListToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DeviceToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ProcessStepToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ProcessToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ShipmentEventToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ShipmentPartyToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ShipmentRouteToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ShipmentToolResponse;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.mcp.application.port.primary.result.ConfigurationView;
import com.warehouse.mcp.application.port.primary.result.CourierList;
import com.warehouse.mcp.application.port.primary.result.DeliveryList;
import com.warehouse.mcp.application.port.primary.result.DeliveryOverview;
import com.warehouse.mcp.domain.vo.Courier;
import com.warehouse.mcp.domain.vo.DeliveryDetails;
import com.warehouse.mcp.domain.vo.DeliveryStep;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ConfigurationToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.CourierListToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.CourierToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DeliveryListToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DeliveryStepToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DeliveryToolResponse;

public class McpToolResponseMapper {

    public CurrentUserToolResponse map(final CurrentUserProfile user) {
        final String fullName = String.join(" ",
                user.firstName() != null ? user.firstName() : "",
                user.lastName() != null ? user.lastName() : "").trim();
        return new CurrentUserToolResponse(user.username(), fullName.isEmpty() ? null : fullName, user.role(),
                user.departmentCode(), user.operatorId() != null ? user.operatorId().value() : null);
    }

    public ShipmentToolResponse map(final ShipmentOverview shipment) {
        return new ShipmentToolResponse(
                shipment.shipmentId() != null ? shipment.shipmentId().getValue() : null,
                shipment.trackingNumber(),
                name(shipment.status()),
                format(shipment.createdAt()),
                format(shipment.updatedAt()),
                map(shipment.sender()),
                map(shipment.recipient()),
                shipment.totalEvents(),
                shipment.latestEvents().stream().map(this::map).toList());
    }

    public ProcessToolResponse map(final ProcessDetails process) {
        return new ProcessToolResponse(
                process.processId().value().toString(),
                process.status(),
                format(process.createdAt()),
                format(process.modifiedAt()),
                process.faultDescription(),
                process.deviceId(),
                process.departmentCode(),
                process.userId(),
                process.steps().stream().map(this::map).toList());
    }

    public DeviceListToolResponse map(final DeviceList deviceList) {
        return new DeviceListToolResponse(deviceList.totalDevices(), deviceList.devices().size(),
                deviceList.devices().stream().map(this::map).toList());
    }

    public DepartmentListToolResponse map(final DepartmentList departmentList) {
        return new DepartmentListToolResponse(departmentList.totalDepartments(), departmentList.departments().size(),
                departmentList.departments().stream().map(this::map).toList());
    }

    public DepartmentDetailsToolResponse map(final DepartmentDetails department) {
        return new DepartmentDetailsToolResponse(code(department.departmentCode()), department.city(),
                department.street(), department.zipCode(), department.countryCode(), department.latitude(),
                department.longitude());
    }

    public ShipmentRouteToolResponse map(final TrackingNumber trackingNumber, final ShipmentRoute route) {
        return new ShipmentRouteToolResponse(trackingNumber.value(),
                route.departments().stream().map(this::map).toList(),
                route.distanceInKilometers() != null ? Math.round(route.distanceInKilometers() * 10) / 10.0 : null);
    }

    public DeliveryListToolResponse map(final DeliveryList deliveryList) {
        return new DeliveryListToolResponse(deliveryList.deliveries().size(),
                deliveryList.deliveries().stream().map(this::map).toList());
    }

    public DeliveryToolResponse map(final DeliveryOverview overview) {
        final DeliveryDetails delivery = overview.delivery();
        return new DeliveryToolResponse(
                delivery.deliveryId() != null ? delivery.deliveryId().getId() : null,
                delivery.shipmentId() != null ? delivery.shipmentId().getValue() : null,
                delivery.type(),
                delivery.deliveryStatus(),
                delivery.lifecycleStatus(),
                code(overview.originDepartmentCode()),
                code(overview.destinationDepartmentCode()),
                delivery.pickupMethod(),
                delivery.method(),
                delivery.pickupPointId(),
                delivery.deliveryPickupPointId(),
                code(overview.courierCode()),
                delivery.supplierId() != null ? delivery.supplierId().value() : null,
                delivery.vehicleId() != null ? delivery.vehicleId().value() : null,
                delivery.signatureRequired(),
                delivery.instructions(),
                format(delivery.createdAt()),
                format(delivery.plannedAt()),
                format(delivery.deliveredAt()),
                delivery.steps().stream().map(this::map).toList());
    }

    public CourierListToolResponse map(final CourierList courierList) {
        return new CourierListToolResponse(courierList.totalCouriers(), courierList.couriers().size(),
                courierList.couriers().stream().map(this::map).toList());
    }

    public ConfigurationToolResponse map(final ConfigurationView configuration) {
        return new ConfigurationToolResponse(configuration.section().name(), configuration.configuration());
    }

    private DeliveryStepToolResponse map(final DeliveryStep step) {
        return new DeliveryStepToolResponse(step.stepNumber(), format(step.attemptedAt()), step.outcome(),
                step.deliveryStatus(), step.supplierId() != null ? step.supplierId().value() : null, step.comment(),
                step.failureReason());
    }

    private CourierToolResponse map(final Courier courier) {
        return new CourierToolResponse(code(courier.courierCode()), courier.firstName(), courier.lastName(),
                courier.telephoneNumber(), code(courier.departmentCode()), courier.status());
    }

    private DepartmentToolResponse map(final DepartmentEntry department) {
        return new DepartmentToolResponse(code(department.departmentCode()), department.type(), department.status());
    }

    private DeviceToolResponse map(final DeviceAccess device) {
        return new DeviceToolResponse(device.deviceId(), device.deviceType(), device.status(), device.departmentCode(),
                device.assignmentMode(), device.ownerUserId(), device.version(), device.paired(),
                format(device.lastSeenAt()), device.operatorUserId(), format(device.operatorSessionExpiresAt()));
    }

    private ShipmentPartyToolResponse map(final MaskedShipmentParty party) {
        if (party == null) {
            return null;
        }
        return new ShipmentPartyToolResponse(party.initials(), party.email(), party.telephoneNumber(), party.city(),
                party.postalCode(), party.countryCode());
    }

    private ShipmentEventToolResponse map(final ShipmentEvent event) {
        return new ShipmentEventToolResponse(
                format(event.timestamp()),
                name(event.status()),
                name(event.processType()),
                event.processId() != null ? event.processId().value().toString() : null,
                event.departmentCode(),
                event.username(),
                event.description());
    }

    private ProcessStepToolResponse map(final ProcessStep step) {
        return new ProcessStepToolResponse(format(step.createdAt()), name(step.processType()), name(step.serviceType()),
                step.sourceService(), step.targetService(), step.departmentCode(), step.faultDescription());
    }

    private String code(final SupplierCode courierCode) {
        return courierCode != null ? courierCode.value() : null;
    }

    private String code(final DepartmentCode departmentCode) {
        return departmentCode != null ? departmentCode.value() : null;
    }

    private String name(final Enum<?> value) {
        return value != null ? value.name() : null;
    }

    private String format(final LocalDateTime value) {
        return value != null ? value.toString() : null;
    }

    private String format(final Instant value) {
        return value != null ? value.toString() : null;
    }
}
