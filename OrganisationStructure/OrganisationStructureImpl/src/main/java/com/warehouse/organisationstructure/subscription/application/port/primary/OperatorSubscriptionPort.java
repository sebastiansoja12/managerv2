package com.warehouse.organisationstructure.subscription.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.subscription.application.port.primary.command.AssignStarterSubscriptionCommand;
import com.warehouse.organisationstructure.subscription.application.port.primary.command.AssignSubscriptionCommand;
import com.warehouse.organisationstructure.subscription.application.port.primary.result.OperatorSubscriptionResult;

import java.util.Optional;

public interface OperatorSubscriptionPort {

    Optional<OperatorSubscriptionResult> find(final OperatorId operatorId);

    OperatorSubscriptionResult assign(final AssignSubscriptionCommand command);

    Optional<OperatorSubscriptionResult> findCurrent();

    boolean isCurrentAccessAllowed();

    void assignStarter(final AssignStarterSubscriptionCommand command);
}
