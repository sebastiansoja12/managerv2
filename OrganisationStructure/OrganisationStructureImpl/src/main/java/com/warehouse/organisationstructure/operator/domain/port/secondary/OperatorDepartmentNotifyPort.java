package com.warehouse.organisationstructure.operator.domain.port.secondary;

import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;

public interface OperatorDepartmentNotifyPort {
    void notifyOperatorCreated(final OperatorSnapshot snapshot);
}
