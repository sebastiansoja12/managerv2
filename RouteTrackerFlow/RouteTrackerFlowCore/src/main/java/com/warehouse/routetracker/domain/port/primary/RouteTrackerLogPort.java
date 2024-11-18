package com.warehouse.routetracker.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.model.DeliveryReturnRequest;
import com.warehouse.routetracker.domain.model.FaultDescription;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.*;

public interface RouteTrackerLogPort {

    RouteProcess initializeRouteProcess(final ShipmentId shipmentId);

    RouteLogRecord find(final ShipmentId shipmentId);

    List<RouteLogRecord> findAll();

    void saveTerminalIdInformation(TerminalIdInformation terminalIdInformation);

    void saveZebraVersionInformation(TerminalVersionInformation terminalVersionInformation);

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
}
