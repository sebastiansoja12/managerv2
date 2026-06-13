package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.enumeration.ProcessType;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.ShipmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "route_log_details")
public class RouteLogRecordDetailEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false))
    private RouteLogRecordDetailId id;

    @Column(name = "event_id", unique = true)
    private String eventId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_log_record_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private RouteLogRecordEntity routeLogRecord;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "version")
    private String version;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "description")
    private String description;

    @Column(name = "process_type")
    private ProcessType processType;

    @Column(name = "request")
    @Lob
    @Size(min = 5, max = 65555)
    private String request;

    @Column(name = "shipment_status")
    private ShipmentStatus shipmentStatus;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "operator_id"))
    private OperatorId operatorId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId userId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_id"))
    private DepartmentId departmentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "supplier_id"))
    private SupplierId supplierId;
}
