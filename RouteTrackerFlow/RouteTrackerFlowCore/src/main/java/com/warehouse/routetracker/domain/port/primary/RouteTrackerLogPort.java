package com.warehouse.routetracker.domain.port.primary;

import java.util.List;

import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.DeliveryReturnRequest;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.*;

public interface RouteTrackerLogPort {

    RouteProcess initializeRouteProcess(ParcelId parcelId);

    RouteLogRecord find(Long parcelId);

    List<RouteLogRecord> findAll();

    void saveZebraIdInformation(ZebraIdInformation zebraIdInformation);

    void saveZebraVersionInformation(ZebraVersionInformation zebraVersionInformation);

    void saveReturnErrorCode(ErrorInformation information);

    void saveFaultDescription(ProcessType processType, Long parcelId, String faultDescription);

    void saveDescription(DescriptionRequest request);

    void saveSupplierCode(SupplierCodeRequest request);

    void saveUsername(UsernameRequest usernameRequest);

    void saveDepotCode(DepotCodeRequest depotCodeRequest);

    void saveCreateRequest(ZebraInitializeRequest request);

    void saveTerminalRequest(TerminalRequest request);

    void saveReturnTrackRequest(ReturnTrackRequest request);

    void saveDeliveryReturnRequest(DeliveryReturnRequest request);
}
