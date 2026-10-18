package com.warehouse.auth.domain.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;

public interface ImpersonationConsentNotificationServicePort {

    void notifyConsentsChanged(final OperatorId operatorId);
}
