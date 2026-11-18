package com.warehouse.mcp.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.application.port.primary.result.DepartmentList;
import com.warehouse.mcp.application.port.primary.result.DeviceList;
import com.warehouse.mcp.application.port.primary.result.ShipmentOverview;
import com.warehouse.mcp.domain.vo.CurrentUserProfile;
import com.warehouse.mcp.domain.vo.DepartmentDetails;
import com.warehouse.mcp.domain.vo.DepartmentFilter;
import com.warehouse.mcp.domain.vo.DeviceFilter;
import com.warehouse.mcp.domain.vo.ProcessDetails;
import com.warehouse.mcp.domain.vo.ShipmentRoute;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.mcp.application.port.primary.result.ConfigurationView;
import com.warehouse.mcp.application.port.primary.result.CourierList;
import com.warehouse.mcp.application.port.primary.result.DeliveryList;
import com.warehouse.mcp.application.port.primary.result.DeliveryOverview;
import com.warehouse.mcp.domain.vo.ConfigurationSection;
import com.warehouse.mcp.domain.vo.DeliveryFilter;

public interface McpQueryPort {
    CurrentUserProfile currentUser();
    ShipmentOverview findShipment(final TrackingNumber trackingNumber);
    ProcessDetails findProcess(final ProcessId processId);
    DeviceList listDevices(final DeviceFilter filter);
    DepartmentList listDepartments(final DepartmentFilter filter);
    DepartmentDetails findDepartment(final DepartmentCode departmentCode);
    ShipmentRoute findShipmentRoute(final TrackingNumber trackingNumber);
    DeliveryList listDeliveries(final DeliveryFilter filter);
    DeliveryOverview findDelivery(final DeliveryId deliveryId);
    DeliveryOverview findShipmentDelivery(final TrackingNumber trackingNumber);
    CourierList listCouriers();
    ConfigurationView findConfiguration(final ConfigurationSection section);
}
