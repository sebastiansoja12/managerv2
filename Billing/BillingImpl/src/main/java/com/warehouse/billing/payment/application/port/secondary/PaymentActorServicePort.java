package com.warehouse.billing.payment.application.port.secondary;

import com.warehouse.billing.payment.domain.vo.PaymentActor;

public interface PaymentActorServicePort {

    PaymentActor currentActor();
}
