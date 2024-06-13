package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import java.time.LocalDateTime;

import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.enumeration.ProcessType;

import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.ParcelStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "route_log_details")
public class RouteLogRecordDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zebra_id")
    private Long zebraId;

    @Column(name = "version")
    private String version;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "description")
    private String description;

    @Column(name = "process_type")
    private ProcessType processType;

    @Column(name = "request")
    @Size(min = 5, max = 65555)
    private String request;

    @Column(name = "parcel_status")
    private ParcelStatus parcelStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depot_code", referencedColumnName = "depot_code")
    private DepotEntity depot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_code", referencedColumnName = "supplier_code")
    private SupplierEntity supplier;
}
