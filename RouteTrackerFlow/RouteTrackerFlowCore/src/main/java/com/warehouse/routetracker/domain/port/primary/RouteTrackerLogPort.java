package com.warehouse.routetracker.domain.port.primary;

import java.util.List;

import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.domain.model.RouteLogRecordToChange;
import com.warehouse.routetracker.domain.vo.*;

public interface RouteTrackerLogPort {


    void saveDelivery(List<DeliveryInformation> deliveryInformation);

    void deleteRoute(RouteDeleteRequest request);

    List<RouteResponse> saveRoutes(List<RouteRequest> routeRequest);

    List<RouteInformation> getRouteListByParcelId(Long parcelId);

    List<RouteInformation> findRoutesByUsername(String username);

    RouteProcess initializeRouteProcess(ParcelId parcelId);

    void saveZebraIdInformation(ZebraIdInformation zebraIdInformation);

    void saveZebraVersionInformation(ZebraVersionInformation zebraVersionInformation);

    void saveReturnErrorCode(ErrorInformation information);

    void saveFaultDescription(ProcessType processType, Long parcelId, String faultDescription);

    void saveTerminalRequest(TerminalRequest request);

    void saveReturnTrackRequest(ReturnTrackRequest request);

    RouteLogRecordToChange find(Long parcelId);

    List<RouteLogRecordToChange> findAll();
}
