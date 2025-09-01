package com.warehouse.routetracker.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.helper.Result;
import com.warehouse.routetracker.domain.model.*;
import com.warehouse.routetracker.domain.vo.*;

public interface RouteTrackerLogPort {

    RouteProcess initializeRouteProcess(final ShipmentId shipmentId);

    RouteLogRecord find(final ShipmentId shipmentId);

    List<RouteLogRecord> findAll();

    void saveDeviceIdInformation(DeviceIdInformation deviceIdInformation);

    void saveDeviceVersionInformation(DeviceVersionInformation deviceVersionInformation);

    void saveReturnErrorCode(ErrorInformation information);

    void saveFaultDescription(final FaultDescription faultDescription);

    void saveDescription(DescriptionRequest request);

    void saveSupplierCode(SupplierCodeRequest request);

    void saveUsername(UsernameRequest usernameRequest);

    void saveDepotCode(DepotCodeRequest depotCodeRequest);

    void saveTerminalRequest(TerminalRequest request);

    void saveReturnTrackRequest(ReturnTrackRequest request);

    void saveDeliveryReturnRequest(DeliveryReturnRequest request);

    void saveDeliveryStatus(DeliveryStatusRequest request);

    void saveDeviceInformation(final DeviceInformationRequest request);

    Result<RouteLogRecord, Exception> changePerson(final ShipmentId shipmentId, final Person person);
}
