package com.warehouse.routetracker.domain.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
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
                .orElseGet(() -> addNewRouteLogRecordDetail(processType, determineParcelStatus(processType)));
    }

    private ParcelStatus determineParcelStatus(ProcessType processType) {
        return switch (processType) {
            case CREATED -> ParcelStatus.CREATED;
            case ROUTE, MISS -> ParcelStatus.DELIVERY;
            case RETURN, REJECT -> ParcelStatus.RETURN;
            case REROUTE -> ParcelStatus.REROUTE;
            case REDIRECT -> ParcelStatus.REDIRECT;
            default ->  throw new RuntimeException("Wrong process type or parcel is already created");
        };
    }

    private RouteLogRecordDetail addNewRouteLogRecordDetail(ProcessType processType, ParcelStatus parcelStatus) {
		final RouteLogRecordDetail routeLogRecordDetail = createNewRouteLogRecordDetail(processType, parcelStatus);
		getRouteLogRecordDetailSet().add(routeLogRecordDetail);
		return routeLogRecordDetail;
	}

	private RouteLogRecordDetail createNewRouteLogRecordDetail(ProcessType processType, ParcelStatus parcelStatus) {
        return RouteLogRecordDetail
                .builder()
                .parcelStatus(parcelStatus)
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
