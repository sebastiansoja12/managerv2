package com.warehouse.mcp.application.port.primary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.mcp.application.port.primary.result.ConfigurationView;
import com.warehouse.mcp.application.port.primary.result.DeliveryOverview;
import com.warehouse.mcp.application.port.primary.result.ShipmentOverview;
import com.warehouse.mcp.domain.vo.ConfigurationSection;
import com.warehouse.mcp.domain.vo.CourierChange;
import com.warehouse.mcp.domain.vo.CourierDraft;
import com.warehouse.mcp.domain.vo.DeliveryConditions;
import com.warehouse.mcp.domain.vo.DeliveryRouteChange;
import com.warehouse.mcp.domain.vo.DepartmentAddress;
import com.warehouse.mcp.domain.vo.DepartmentDetails;
import com.warehouse.mcp.domain.vo.PartyRole;
import com.warehouse.mcp.domain.vo.ShipmentDraft;
import com.warehouse.mcp.domain.vo.ShipmentPartyChange;

import java.util.Map;

public interface McpCommandPort {
    DeliveryOverview assignCourier(final DeliveryId deliveryId, final SupplierCode courierCode,
                                   final VehicleId vehicleId);
    DeliveryOverview unassignCourier(final DeliveryId deliveryId);
    DeliveryOverview changeDeliveryRoute(final DeliveryId deliveryId, final DeliveryRouteChange route);
    DeliveryOverview changeDeliveryConditions(final DeliveryId deliveryId, final DeliveryConditions conditions);
    DeliveryOverview commentDeliveryStep(final DeliveryId deliveryId, final int stepNumber, final String comment);
    DeliveryOverview correctDeliveryStepStatus(final DeliveryId deliveryId, final int stepNumber,
                                               final String deliveryStatus);
    void cancelDelivery(final DeliveryId deliveryId);
    TrackingNumber createShipment(final ShipmentDraft shipment);
    ShipmentOverview changeShipmentParty(final TrackingNumber trackingNumber, final PartyRole role,
                                         final ShipmentPartyChange change);
    DepartmentDetails changeDepartmentAddress(final DepartmentCode departmentCode, final DepartmentAddress address);
    DepartmentDetails changeDepartmentType(final DepartmentCode departmentCode, final String type);
    DepartmentDetails changeDepartmentEmail(final DepartmentCode departmentCode, final String email);
    DepartmentDetails changeDepartmentStatus(final DepartmentCode departmentCode, final String status);
    DepartmentDetails changeDepartmentActive(final DepartmentCode departmentCode, final boolean active);
    SupplierCode createCourier(final CourierDraft courier);
    void updateCourier(final CourierChange change);
    void changeCourierActive(final SupplierCode courierCode, final boolean active);
    ConfigurationView updateConfiguration(final ConfigurationSection section, final Map<String, Object> changes);
}
