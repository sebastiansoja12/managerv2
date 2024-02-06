package com.warehouse.routetracker.domain.port.primary;

import java.util.List;

import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.vo.*;

public interface RouteTrackerLogPort {

    RouteProcess initializeRouteProcess(ParcelId parcelId);

    void saveZebraIdInformation(ZebraIdInformation zebraIdInformation);

    void saveZebraVersionInformation(ZebraVersionInformation zebraVersionInformation);

    void saveReturnErrorCode(ErrorInformation information);

    void saveFaultDescription(ProcessType processType, Long parcelId, String faultDescription);

    void saveTerminalRequest(TerminalRequest request);

    void saveReturnTrackRequest(ReturnTrackRequest request);

    RouteLogRecord find(Long parcelId);

    List<RouteLogRecord> findAll();
}
