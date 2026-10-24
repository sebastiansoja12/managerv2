package com.warehouse.terminal.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TeamId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.terminal.domain.enumeration.DeviceAssignmentMode;

public class OwnershipProfile {
    private UserId userId;
    private UserId previousUserId;
    private DepartmentCode departmentCode;
    private TeamId teamId;
    private VehicleId vehicleId;
    private String assignedRole;
    private Instant assignedAt;
    private Instant unassignedAt;
    private final OperatorId operatorId;
    private DeviceAssignmentMode assignmentMode;

	public OwnershipProfile(final Instant assignedAt, final String assignedRole, final DepartmentCode departmentCode,
			final UserId previousUserId, final TeamId teamId, final Instant unassignedAt, final UserId userId,
			final VehicleId vehicleId, final OperatorId operatorId, final DeviceAssignmentMode assignmentMode) {
        this.assignedAt = assignedAt;
        this.assignedRole = assignedRole;
        this.departmentCode = departmentCode;
        this.previousUserId = previousUserId;
        this.teamId = teamId;
        this.unassignedAt = unassignedAt;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.operatorId = operatorId;
        this.assignmentMode = assignmentMode;
    }

    public static OwnershipProfile initializeOwnership(final String assignedRole,
                                                       final UserId userId,
                                                       final DepartmentCode departmentCode,
                                                       final VehicleId vehicleId,
                                                       final OperatorId operatorId) {
        return new OwnershipProfile(Instant.now(), assignedRole, departmentCode, null, null, null, userId, null,
                operatorId, DeviceAssignmentMode.PERSONAL);
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }

    public DeviceAssignmentMode getAssignmentMode() {
        return assignmentMode;
    }

    public void setAssignmentMode(final DeviceAssignmentMode assignmentMode) {
        this.assignmentMode = assignmentMode;
    }

    public void reassignTo(final UserId newUserId, final Instant timestamp) {
        if (newUserId == null || newUserId.equals(this.userId)) {
            return;
        }
        this.previousUserId = this.userId;
        this.userId = newUserId;
        this.assignedAt = timestamp;
        this.unassignedAt = null;
    }

    public boolean isShared() {
        return DeviceAssignmentMode.SHARED.equals(assignmentMode);
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(final Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getAssignedRole() {
        return assignedRole;
    }

    public void setAssignedRole(final String assignedRole) {
        this.assignedRole = assignedRole;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public UserId getPreviousUserId() {
        return previousUserId;
    }

    public void setPreviousUserId(final UserId previousUserId) {
        this.previousUserId = previousUserId;
    }

    public TeamId getTeamId() {
        return teamId;
    }

    public void setTeamId(final TeamId teamId) {
        this.teamId = teamId;
    }

    public Instant getUnassignedAt() {
        return unassignedAt;
    }

    public void setUnassignedAt(final Instant unassignedAt) {
        this.unassignedAt = unassignedAt;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(final UserId userId) {
        this.userId = userId;
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(final VehicleId vehicleId) {
        this.vehicleId = vehicleId;
    }

    public OwnershipProfile update(final OwnershipProfile ownership) {
        if (ownership.getUserId() != null) {
            this.userId = ownership.getUserId();
        }
        if (ownership.getPreviousUserId() != null) {
            this.previousUserId = ownership.getPreviousUserId();
        }
        if (ownership.getDepartmentCode() != null) {
            this.departmentCode = ownership.getDepartmentCode();
        }
        if (ownership.getTeamId() != null) {
            this.teamId = ownership.getTeamId();
        }
        if (ownership.getVehicleId() != null) {
            this.vehicleId = ownership.getVehicleId();
        }
        if (ownership.getAssignedRole() != null) {
            this.assignedRole = ownership.getAssignedRole();
        }
        if (ownership.getAssignedAt() != null) {
            this.assignedAt = ownership.getAssignedAt();
        }
        if (ownership.getUnassignedAt() != null) {
            this.unassignedAt = ownership.getUnassignedAt();
        }
        if (ownership.getAssignmentMode() != null) {
            this.assignmentMode = ownership.getAssignmentMode();
        }
        return this;
    }
}
