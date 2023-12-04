package com.warehouse.routetracker.domain.port.primary;

import java.util.List;

import com.warehouse.routetracker.domain.model.Error;
import com.warehouse.routetracker.domain.model.ProcessType;
import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.domain.model.RouteInformation;

public interface RouteTrackerLogPort {


    void initializeRoute(Long parcelId);

    void saveDelivery(List<DeliveryInformation> deliveryInformation);

    void deleteRoute(RouteDeleteRequest request);

    List<RouteResponse> saveRoutes(List<RouteRequest> routeRequest);

    List<RouteInformation> getRouteListByParcelId(Long parcelId);

    List<RouteInformation> findRoutesByUsername(String username);

    void saveZebraIdInformation(ProcessType processType, Long parcelId, Long zebraId);

    void saveZebraVersionInformation(ProcessType processType, Long parcelId, String version);

    void saveReturnErrorCode(ProcessType processType, Error error);

    void saveFaultDescription(ProcessType processType, String faultDescription);

    void saveTerminalRequest(TerminalRequest request);

    void saveReturnTrackRequest(ReturnTrackRequest request);
}
