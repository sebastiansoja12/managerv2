package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import com.warehouse.commonassets.identificator.TeamId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.terminal.domain.enumeration.DeviceAssignmentMode;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Ownership {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "previous_user_id"))
    private UserId previousUserId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "team_id"))
    private TeamId teamId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "vehicle_id"))
    private VehicleId vehicleId;

    @Column(name = "assigned_role")
    private String assignedRole;

    @Column(name = "assigned_at")
    private Instant assignedAt;

    @Column(name = "unassigned_at")
    private Instant unassignedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_mode")
    private DeviceAssignmentMode assignmentMode;

    protected Ownership() {}

    public Ownership(final UserId previousUserId,
                     final TeamId teamId,
                     final VehicleId vehicleId,
                     final String assignedRole,
                     final Instant assignedAt,
                     final Instant unassignedAt,
                     final DeviceAssignmentMode assignmentMode) {
        this.previousUserId = previousUserId;
        this.teamId = teamId;
        this.vehicleId = vehicleId;
        this.assignedRole = assignedRole;
        this.assignedAt = assignedAt;
        this.unassignedAt = unassignedAt;
        this.assignmentMode = assignmentMode;
    }

    public UserId getPreviousUserId() {
        return previousUserId;
    }

    public TeamId getTeamId() {
        return teamId;
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }

    public String getAssignedRole() {
        return assignedRole;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public Instant getUnassignedAt() {
        return unassignedAt;
    }

    public DeviceAssignmentMode getAssignmentMode() {
        return assignmentMode;
    }
}
