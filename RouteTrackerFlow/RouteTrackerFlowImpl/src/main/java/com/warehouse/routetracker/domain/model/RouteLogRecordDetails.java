package com.warehouse.routetracker.domain.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.domain.enumeration.ProcessType;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RouteLogRecordDetails {

    private Set<RouteLogRecordDetail> routeLogRecordDetailSet;

    public Set<RouteLogRecordDetail> getRouteLogRecordDetailSet() {
        if (routeLogRecordDetailSet == null) {
            routeLogRecordDetailSet = new HashSet<>();
        }
        return routeLogRecordDetailSet;
    }

    public RouteLogRecordDetail getRouteLogRecordDetail(ProcessType processType) {
        return getRouteLogRecordDetailSet()
                .stream()
                .filter(equalProcessType(processType))
                .findFirst()
                .orElseGet(() -> addNewRouteLogRecordDetail(processType, determineShipmentStatus(processType)));
    }

    private ShipmentStatus determineShipmentStatus(ProcessType processType) {
        return switch (processType) {
            case CREATED -> ShipmentStatus.CREATED;
            case ROUTE, MISS -> ShipmentStatus.DELIVERY;
            case RETURN, REJECT -> ShipmentStatus.RETURN;
            case REROUTE -> ShipmentStatus.REROUTE;
            case REDIRECT -> ShipmentStatus.REDIRECT;
            default ->  throw new RuntimeException("Wrong process type or shipment is already created");
        };
    }

    private RouteLogRecordDetail addNewRouteLogRecordDetail(ProcessType processType, ShipmentStatus shipmentStatus) {
		final RouteLogRecordDetail routeLogRecordDetail = createNewRouteLogRecordDetail(processType, shipmentStatus);
		getRouteLogRecordDetailSet().add(routeLogRecordDetail);
		return routeLogRecordDetail;
	}

	private RouteLogRecordDetail createNewRouteLogRecordDetail(ProcessType processType, ShipmentStatus shipmentStatus) {
        return RouteLogRecordDetail
                .builder()
                .shipmentStatus(shipmentStatus)
                .timestamp(LocalDateTime.now())
                .processType(processType)
                .build();
    }

    private Predicate<? super RouteLogRecordDetail> equalRouteLogRecordDetailId(Long id) {
        return routeLogRecordDetail -> Objects.equals(id, routeLogRecordDetail.getId());
    }

    private Predicate<? super RouteLogRecordDetail> equalProcessType(ProcessType processType) {
        return routeLogRecordDetail -> processType.equals(routeLogRecordDetail.getProcessType());
    }
}
