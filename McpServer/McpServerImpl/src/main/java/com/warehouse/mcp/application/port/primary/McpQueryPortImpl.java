package com.warehouse.mcp.application.port.primary;

import java.util.List;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.application.port.primary.result.DeviceList;
import com.warehouse.mcp.application.port.primary.result.ShipmentOverview;
import com.warehouse.mcp.application.port.secondary.CurrentUserServicePort;
import com.warehouse.mcp.application.port.secondary.DeviceServicePort;
import com.warehouse.mcp.application.port.secondary.ProcessServicePort;
import com.warehouse.mcp.application.port.secondary.ShipmentServicePort;
import com.warehouse.mcp.domain.exception.McpToolAccessException;
import com.warehouse.mcp.domain.service.PersonalDataMasking;
import com.warehouse.mcp.domain.vo.CurrentUserProfile;
import com.warehouse.mcp.domain.vo.DeviceAccess;
import com.warehouse.mcp.domain.vo.DeviceFilter;
import com.warehouse.mcp.domain.vo.ProcessDetails;
import com.warehouse.mcp.domain.vo.ShipmentTracking;

public class McpQueryPortImpl implements McpQueryPort {

    static final int MAX_SHIPMENT_EVENTS = 10;
    static final int MAX_DEVICES = 50;

    private final CurrentUserServicePort currentUserServicePort;

    private final ShipmentServicePort shipmentServicePort;

    private final ProcessServicePort processServicePort;

    private final DeviceServicePort deviceServicePort;

    private final PersonalDataMasking personalDataMasking;

    public McpQueryPortImpl(final CurrentUserServicePort currentUserServicePort,
                            final ShipmentServicePort shipmentServicePort,
                            final ProcessServicePort processServicePort,
                            final DeviceServicePort deviceServicePort,
                            final PersonalDataMasking personalDataMasking) {
        this.currentUserServicePort = currentUserServicePort;
        this.shipmentServicePort = shipmentServicePort;
        this.processServicePort = processServicePort;
        this.deviceServicePort = deviceServicePort;
        this.personalDataMasking = personalDataMasking;
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
}
