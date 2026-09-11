package com.warehouse.mcp.infrastructure.adapter.primary;

import java.time.Instant;
import java.time.LocalDateTime;

import com.warehouse.mcp.application.port.primary.result.DeviceList;
import com.warehouse.mcp.application.port.primary.result.ShipmentOverview;
import com.warehouse.mcp.domain.vo.CurrentUserProfile;
import com.warehouse.mcp.domain.vo.DeviceAccess;
import com.warehouse.mcp.domain.vo.MaskedShipmentParty;
import com.warehouse.mcp.domain.vo.ProcessDetails;
import com.warehouse.mcp.domain.vo.ProcessStep;
import com.warehouse.mcp.domain.vo.ShipmentEvent;
import com.warehouse.mcp.infrastructure.adapter.primary.response.CurrentUserToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DeviceListToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DeviceToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ProcessStepToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ProcessToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ShipmentEventToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ShipmentPartyToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ShipmentToolResponse;

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
