package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Clock;
import java.time.Instant;

@Slf4j
public class PaymentDeadlineJob {

    private final PaymentPort paymentPort;

    private final OperatorApiService operatorApiService;

    private final OperatorContext operatorContext;

    private final Clock clock;

    public PaymentDeadlineJob(final PaymentPort paymentPort,
                              final OperatorApiService operatorApiService,
                              final OperatorContext operatorContext,
                              final Clock clock) {
        this.paymentPort = paymentPort;
        this.operatorApiService = operatorApiService;
        this.operatorContext = operatorContext;
        this.clock = clock;
    }

    @Scheduled(cron = "${billing.payment.deadline-cron:0 */15 * * * *}")
    public void expireOverduePayments() {
        final Instant now = this.clock.instant();
        for (final OperatorIdDto operatorId : this.operatorApiService.findActiveOperators()) {
            try {
                this.operatorContext.runAs(new OperatorId(operatorId.value()),
                        () -> this.paymentPort.expireOverdue(now));
            } catch (final RuntimeException exception) {
                log.warn("Overdue payments of operator {} were not expired: {}", operatorId.value(),
                        exception.getMessage());
            }
        }
    }
}
