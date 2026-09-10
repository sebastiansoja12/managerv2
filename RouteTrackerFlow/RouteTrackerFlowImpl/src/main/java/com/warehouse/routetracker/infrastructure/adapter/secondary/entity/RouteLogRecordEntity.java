package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import java.util.ArrayList;
import java.util.List;

import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "route_log")
public class RouteLogRecordEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false))
    private RouteLogRecordId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id", nullable = false, unique = true))
    private ShipmentId shipmentId;

    @Column(name = "return_code")
    private String returnCode;

    @Column(name = "fault_description")
    private String faultDescription;

    @OneToMany(mappedBy = "routeLogRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id.value DESC")
    private List<RouteLogRecordDetailEntity> routeLogRecordDetails;

    public List<RouteLogRecordDetailEntity> getRouteLogRecordDetails() {
        if (routeLogRecordDetails == null) {
            routeLogRecordDetails = new ArrayList<>();
        }
        return routeLogRecordDetails;
    }

    @PrePersist
    private void assignId() {
        if (id == null) {
            id = RouteLogRecordId.generate();
        }
    }
}
