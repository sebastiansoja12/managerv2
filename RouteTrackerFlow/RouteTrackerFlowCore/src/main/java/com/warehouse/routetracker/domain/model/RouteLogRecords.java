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


    private List<RouteLogRecord> routeLogRecords;

    public List<RouteLogRecord> getRouteLogRecords() {
        if (routeLogRecords == null) {
            routeLogRecords = new ArrayList<>();
        }
        return routeLogRecords;
    }

    void saveRequestParcels(List<Parcel> parcels) {
        final List<RouteLogRecord> routeLogRecords = parcels
                .stream()
                .map(this::toRouteLogRecord)
                .toList();

        getRouteLogRecords().addAll(routeLogRecords);
    }

    private RouteLogRecord toRouteLogRecord(Parcel e) {
        return RouteLogRecord
                .builder()
                .parcelId(e.getId())
                .returnCode("400")
                .build();
    }
}
