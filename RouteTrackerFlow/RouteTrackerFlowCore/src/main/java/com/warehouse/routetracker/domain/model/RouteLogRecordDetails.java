package com.warehouse.routetracker.domain.model;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RouteLogRecordDetails {


    private Set<RouteLogRecordDetail> routeLogRecordDetailSet;

    private final String description = "Parcel created. Not yet shipped";


    public Set<RouteLogRecordDetail> getRouteLogRecordDetailSet() {
        if (routeLogRecordDetailSet == null) {
            routeLogRecordDetailSet = new HashSet<>();
        }
        return routeLogRecordDetailSet;
    }

    public RouteLogRecordDetail getRouteLogRecordDetail(ProcessType processType, Long id) {
        return getRouteLogRecordDetailSet()
                .stream()
                .filter(equalProcessType(processType))
                .filter(equalRouteLogRecordDetailId(id))
                .findFirst()
                .orElseGet(this::addNewRouteLogRecordDetail);
    }

    private RouteLogRecordDetail addNewRouteLogRecordDetail() {
        return RouteLogRecordDetail
                .builder()
                .parcelStatus(ParcelStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .processType(ProcessType.CREATED)
                .description(description)
                .build();
    }

    private Predicate<? super RouteLogRecordDetail> equalRouteLogRecordDetailId(Long id) {
        return routeLogRecordDetail -> Objects.equals(id, routeLogRecordDetail.getId());
    }

    private Predicate<? super RouteLogRecordDetail> equalProcessType(ProcessType processType) {
        return routeLogRecordDetail -> processType.equals(routeLogRecordDetail.getProcessType());
    }
}
