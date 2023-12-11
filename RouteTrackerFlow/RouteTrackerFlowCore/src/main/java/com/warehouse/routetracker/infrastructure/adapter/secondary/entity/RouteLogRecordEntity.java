package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "route_log")
public class RouteLogRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "parcel_id", nullable = false)
    private Long parcelId;

    @Column(name = "return_code")
    private String returnCode;

    @Column(name = "fault_description")
    private String faultDescription;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "route_log_record_id", referencedColumnName = "id")
    private List<RouteLogRecordDetailEntity> routeLogRecordDetails;

    public List<RouteLogRecordDetailEntity> getRouteLogRecordDetails() {
        if (routeLogRecordDetails == null) {
            routeLogRecordDetails = new ArrayList<>();
        }
        return routeLogRecordDetails;
    }

    public void updateRouteLogRecordDetails(List<RouteLogRecordDetailEntity> routeLogRecordEntities) {
        this.routeLogRecordDetails = routeLogRecordEntities;
    }

}
