package com.warehouse.organisationstructure.department.domain.vo;

public record DepartmentStatusPolicy(boolean deletionPrevented,
                                     boolean archivingPrevented,
                                     boolean statusChangeReasonRequired,
                                     boolean deactivationWithActiveWorkBlocked,
                                     boolean automaticWorkTransfer,
                                     int autoArchiveInactiveAfterDays) {

    public static DepartmentStatusPolicy unrestricted() {
        return new DepartmentStatusPolicy(false, false, false, false, false, 0);
    }
}
