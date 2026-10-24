package com.warehouse.mcp.application.port.primary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.application.port.primary.result.DeviceList;
import com.warehouse.mcp.application.port.primary.result.ShipmentOverview;
import com.warehouse.mcp.domain.vo.CurrentUserProfile;
import com.warehouse.mcp.domain.vo.DeviceFilter;
import com.warehouse.mcp.domain.vo.ProcessDetails;

public interface McpQueryPort {
    CurrentUserProfile currentUser();
    ShipmentOverview findShipment(final TrackingNumber trackingNumber);
    ProcessDetails findProcess(final ProcessId processId);
    DeviceList listDevices(final DeviceFilter filter);
}
