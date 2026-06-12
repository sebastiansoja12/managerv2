package com.warehouse.organisationstructure.operator.domain.port.secondary;

import java.util.function.Consumer;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;

public interface OperatorUserNotifyPort {
    UserId notifyOperatorCreated(final OperatorSnapshot snapshot, final Consumer<UserId> beforeUserCreated);
}
