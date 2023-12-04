package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.enumeration.ProcessType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class RouteLogRecordDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zebra_id")
    private Long zebraId;

    @Column(name = "version")
    private String version;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "process_type", nullable = false)
    private ProcessType processType;

    @Column(name = "request", nullable = false)
    private String request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcel_id", referencedColumnName = "id")
    private ParcelEntity parcel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depot_code", referencedColumnName = "depot_code")
    private DepotEntity depot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_code", referencedColumnName = "supplier_code")
    private SupplierEntity supplier;
}
