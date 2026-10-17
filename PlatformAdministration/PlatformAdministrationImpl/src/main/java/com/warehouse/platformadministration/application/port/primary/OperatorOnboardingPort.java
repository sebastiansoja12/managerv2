package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.domain.model.OperatorOnboarding;

public interface OperatorOnboardingPort {

    OperatorOnboarding find(final OperatorId operatorId);
}
