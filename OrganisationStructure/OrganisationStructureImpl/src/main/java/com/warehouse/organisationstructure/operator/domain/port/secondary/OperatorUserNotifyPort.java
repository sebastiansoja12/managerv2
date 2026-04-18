package com.warehouse.organisationstructure.operator.domain.port.secondary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;

public interface OperatorUserNotifyPort {
    UserId notifyOperatorCreated(final OperatorSnapshot snapshot);
}
