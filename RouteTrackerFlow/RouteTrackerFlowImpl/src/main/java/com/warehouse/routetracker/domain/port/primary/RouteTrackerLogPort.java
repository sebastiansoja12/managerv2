package com.warehouse.routetracker.domain.port.primary;

import java.time.LocalDateTime;
import java.util.List;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.model.DeviceInformationRequest;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.DepotCodeRequest;
import com.warehouse.routetracker.domain.vo.DeviceIdInformation;
import com.warehouse.routetracker.domain.vo.DeviceVersionInformation;
import com.warehouse.routetracker.domain.vo.DeliveryStatusRequest;
import com.warehouse.routetracker.domain.vo.SupplierCodeRequest;
import com.warehouse.routetracker.domain.vo.TerminalRequest;
import com.warehouse.routetracker.domain.vo.UsernameRequest;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

public interface RouteTrackerLogPort {

    void saveShipmentEvent(final ShipmentId shipmentId,
                           final String eventType,
                           final ParcelStatus parcelStatus,
                           final LocalDateTime occurredAt,
                           final String payload);

    void saveDeliveryStatus(final DeliveryStatusRequest request);

    void saveDepotCode(final DepotCodeRequest request);

    void saveSupplierCode(final SupplierCodeRequest request);

    void saveTerminalRequest(final TerminalRequest request);

    void saveDeviceIdInformation(final DeviceIdInformation information);

    void saveDeviceVersionInformation(final DeviceVersionInformation information);

    void saveUsername(final UsernameRequest request);

    void saveDeviceInformation(final DeviceInformationRequest request);

    RouteLogRecord find(final ShipmentId shipmentId);

    List<RouteLogRecord> findAll();
}
