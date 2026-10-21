package com.warehouse.palletmanagement.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.PalletTransportId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.palletmanagement.domain.enumeration.TransportType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity(name = "palletmanagement.PalletTransportEntity")
@Table(name = "pallet_transports")
public class PalletTransportEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false))
    private PalletTransportId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "pallet_id", nullable = false, length = 64))
    private PalletId palletId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "pallet_tracking_number", nullable = false, length = 64))
    private PalletTrackingNumber palletTrackingNumber;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "carrier_id", nullable = false))
    private CarrierId carrierId;

    @Column(name = "carrier_name", nullable = false)
    private String carrierName;

    @Enumerated(EnumType.STRING)
    @Column(name = "transport_type", nullable = false, length = 32)
    private TransportType transportType;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "driver_id"))
    private DriverId driverId;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "driver_phone", length = 40)
    private String driverPhone;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "vehicle_id"))
    private VehicleId vehicleId;

    @Column(name = "vehicle_registration_number", nullable = false, length = 32)
    private String vehicleRegistrationNumber;

    @Column(name = "trailer_registration_number", length = 32)
    private String trailerRegistrationNumber;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "assigned_by_user_id"))
    private UserId assignedByUserId;

    @Column(name = "assigned_by_username")
    private String assignedByUsername;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_id"))
    private DepartmentId departmentId;

    protected PalletTransportEntity() {
    }

    public PalletTransportEntity(final PalletTransportId id,
                                 final PalletId palletId,
                                 final PalletTrackingNumber palletTrackingNumber,
                                 final CarrierId carrierId,
                                 final String carrierName,
                                 final TransportType transportType,
                                 final DriverId driverId,
                                 final String driverName,
                                 final String driverPhone,
                                 final VehicleId vehicleId,
                                 final String vehicleRegistrationNumber,
                                 final String trailerRegistrationNumber,
                                 final Instant assignedAt,
                                 final UserId assignedByUserId,
                                 final String assignedByUsername,
                                 final DepartmentId departmentId) {
        this.id = id;
        this.palletId = palletId;
        this.palletTrackingNumber = palletTrackingNumber;
        this.carrierId = carrierId;
        this.carrierName = carrierName;
        this.transportType = transportType;
        this.driverId = driverId;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.vehicleId = vehicleId;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        this.trailerRegistrationNumber = trailerRegistrationNumber;
        this.assignedAt = assignedAt;
        this.assignedByUserId = assignedByUserId;
        this.assignedByUsername = assignedByUsername;
        this.departmentId = departmentId;
    }

    public PalletTransportId getId() {
        return id;
    }

    public PalletId getPalletId() {
        return palletId;
    }

    public PalletTrackingNumber getPalletTrackingNumber() {
        return palletTrackingNumber;
    }

    public CarrierId getCarrierId() {
        return carrierId;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public DriverId getDriverId() {
        return driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public String getTrailerRegistrationNumber() {
        return trailerRegistrationNumber;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public UserId getAssignedByUserId() {
        return assignedByUserId;
    }

    public String getAssignedByUsername() {
        return assignedByUsername;
    }

    public DepartmentId getDepartmentId() {
        return departmentId;
    }
}
