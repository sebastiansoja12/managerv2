package com.warehouse.mail.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.mail.domain.vo.ShipmentNotificationPolicy;

public interface ShipmentNotificationPolicyPort {
    ShipmentNotificationPolicy policyFor(final OperatorId operatorId);
}
