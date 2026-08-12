package com.warehouse.routetracker.domain.port.primary;

import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.domain.model.DeviceInformationRequest;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.LocalDateTime;
import java.util.List;

public interface RouteTrackerLogPort {

    void createShipmentEvent(final ShipmentId shipmentId,
                             final String eventType,
                             final ShipmentStatus shipmentStatus,
                             final LocalDateTime occurredAt,
                             final String payload,
                             final UserId userId,
                             final DepartmentId departmentId);

    void saveDeliveryStatus(final DeliveryStatusRequest request);

    void saveDepartmentId(final DepartmentIdRequest request);

    void saveSupplierId(final SupplierIdRequest request);

    void saveTerminalRequest(final TerminalRequest request);

    void saveDeviceIdInformation(final DeviceIdInformation information);

    void saveDeviceVersionInformation(final DeviceVersionInformation information);

    void saveUserId(final UserIdRequest request);

    void saveDeviceInformation(final DeviceInformationRequest request);

    RouteLogRecord find(final ShipmentId shipmentId);

    List<RouteLogRecord> findAll();
}
