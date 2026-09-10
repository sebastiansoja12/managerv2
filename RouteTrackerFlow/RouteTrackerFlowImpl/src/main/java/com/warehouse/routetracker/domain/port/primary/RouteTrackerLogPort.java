package com.warehouse.routetracker.domain.port.primary;

import com.warehouse.routetracker.domain.model.DeviceInformationRequest;
import com.warehouse.routetracker.domain.model.ShipmentStatusStateChangeCommand;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import java.util.List;

public interface RouteTrackerLogPort {

    void createOrChangeShipmentState(final ShipmentStatusStateChangeCommand command);

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
