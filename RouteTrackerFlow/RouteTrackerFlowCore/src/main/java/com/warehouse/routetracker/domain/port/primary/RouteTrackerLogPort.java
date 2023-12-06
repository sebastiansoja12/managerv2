package com.warehouse.routetracker.domain.port.primary;

import java.util.List;

import com.warehouse.routetracker.domain.model.Error;
import com.warehouse.routetracker.domain.model.ProcessType;
import com.warehouse.routetracker.domain.model.RouteLogRecordToChange;
import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.domain.model.RouteInformation;

public interface RouteTrackerLogPort {


    void initializeRoute(Long parcelId);

    void saveDelivery(List<DeliveryInformation> deliveryInformation);

    void deleteRoute(RouteDeleteRequest request);

    List<RouteResponse> saveRoutes(List<RouteRequest> routeRequest);

    List<RouteInformation> getRouteListByParcelId(Long parcelId);

    List<RouteInformation> findRoutesByUsername(String username);

    RouteProcess initializeRouteProcess(ParcelId parcelId);

    void saveZebraIdInformation(ZebraIdInformation zebraIdInformation);

    void saveZebraVersionInformation(ZebraVersionInformation zebraVersionInformation);

    void saveReturnErrorCode(Long parcelId, Error error);

    void saveFaultDescription(ProcessType processType, Long parcelId, String faultDescription);

    void saveTerminalRequest(TerminalRequest request);

    void saveReturnTrackRequest(ReturnTrackRequest request);

    RouteLogRecordToChange find(Long parcelId);
}
