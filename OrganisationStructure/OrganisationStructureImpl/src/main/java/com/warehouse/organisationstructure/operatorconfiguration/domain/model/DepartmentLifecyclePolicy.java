package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class DepartmentLifecyclePolicy {

    private final boolean requireStatusChangeReason;
    private final boolean blockDeactivationWithActiveWork;
    private final DepartmentWorkTransferMode workTransferOnDeactivation;
    private final int autoArchiveInactiveAfterDays;

    public DepartmentLifecyclePolicy(final boolean requireStatusChangeReason,
                                     final boolean blockDeactivationWithActiveWork,
                                     final DepartmentWorkTransferMode workTransferOnDeactivation,
                                     final int autoArchiveInactiveAfterDays) {
        this.requireStatusChangeReason = requireStatusChangeReason;
        this.blockDeactivationWithActiveWork = blockDeactivationWithActiveWork;
        this.workTransferOnDeactivation = workTransferOnDeactivation;
        this.autoArchiveInactiveAfterDays = autoArchiveInactiveAfterDays;
    }

    public static DepartmentLifecyclePolicy defaultConfiguration() {
        return new DepartmentLifecyclePolicy(
                false,
                true,
                DepartmentWorkTransferMode.MANUAL,
                0
        );
    }

    public boolean isRequireStatusChangeReason() { return requireStatusChangeReason; }
    public boolean isBlockDeactivationWithActiveWork() { return blockDeactivationWithActiveWork; }
    public DepartmentWorkTransferMode getWorkTransferOnDeactivation() { return workTransferOnDeactivation; }
    public int getAutoArchiveInactiveAfterDays() { return autoArchiveInactiveAfterDays; }
}
