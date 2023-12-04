package com.warehouse.routetracker.domain.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RouteLogRecords {


    private List<RouteLogRecordToChange> routeLogRecords;

    public List<RouteLogRecordToChange> getRouteLogRecords() {
        if (routeLogRecords == null) {
            routeLogRecords = new ArrayList<>();
        }
        return routeLogRecords;
    }

    void saveRequestParcels(List<Parcel> parcels) {
        final List<RouteLogRecordToChange> routeLogRecords = parcels
                .stream()
                .map(this::toRouteLogRecord)
                .toList();

        getRouteLogRecords().addAll(routeLogRecords);
    }

    private RouteLogRecordToChange toRouteLogRecord(Parcel e) {
        return RouteLogRecordToChange
                .builder()
                .parcelId(e.getId())
                .returnCode("400")
                .build();
    }
}
