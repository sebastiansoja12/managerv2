package com.warehouse.billing.payment.application.port.secondary;

import java.time.Duration;

public interface PaymentDeadlineServicePort {

    Duration paymentDeadline();
}
