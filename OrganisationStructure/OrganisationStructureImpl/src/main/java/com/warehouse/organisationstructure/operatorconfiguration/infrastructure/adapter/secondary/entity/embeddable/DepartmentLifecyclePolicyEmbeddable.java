package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentLifecyclePolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentWorkTransferMode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class DepartmentLifecyclePolicyEmbeddable {

    @Column(name = "department_require_status_change_reason")
    private boolean requireStatusChangeReason;

    @Column(name = "department_block_deactivation_with_active_work")
    private boolean blockDeactivationWithActiveWork;

    @Enumerated(EnumType.STRING)
    @Column(name = "department_work_transfer_on_deactivation")
    private DepartmentWorkTransferMode workTransferOnDeactivation;

    @Column(name = "department_auto_archive_inactive_after_days")
    private int autoArchiveInactiveAfterDays;

    public DepartmentLifecyclePolicyEmbeddable() {
    }

    public static DepartmentLifecyclePolicyEmbeddable from(final DepartmentLifecyclePolicy configuration) {
        final DepartmentLifecyclePolicy source = configuration != null
                ? configuration
                : DepartmentLifecyclePolicy.defaultConfiguration();
        final DepartmentLifecyclePolicyEmbeddable embeddable = new DepartmentLifecyclePolicyEmbeddable();
        embeddable.requireStatusChangeReason = source.isRequireStatusChangeReason();
        embeddable.blockDeactivationWithActiveWork = source.isBlockDeactivationWithActiveWork();
        embeddable.workTransferOnDeactivation = source.getWorkTransferOnDeactivation();
        embeddable.autoArchiveInactiveAfterDays = source.getAutoArchiveInactiveAfterDays();
        return embeddable;
    }

    public DepartmentLifecyclePolicy toModel() {
        return new DepartmentLifecyclePolicy(
                requireStatusChangeReason,
                blockDeactivationWithActiveWork,
                workTransferOnDeactivation,
                autoArchiveInactiveAfterDays
        );
    }
}
