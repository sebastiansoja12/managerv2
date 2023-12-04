package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class RouteLogRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "routeLogRecordEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "route_log_record_id", referencedColumnName = "id")
    private List<RouteLogRecordDetailEntity> routeLogRecordDetails;


}
