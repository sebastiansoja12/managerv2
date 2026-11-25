package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentDeadlineJobTest {

    private static final Instant NOW = Instant.parse("2026-11-27T08:15:00Z");

    @Test
    void shouldExpireOverduePaymentsOfEveryActiveOperator() {
        final PaymentPort paymentPort = mock(PaymentPort.class);
        final OperatorApiService operatorApiService = mock(OperatorApiService.class);
        final OperatorContext operatorContext = mock(OperatorContext.class);
        final List<OperatorId> operators = new ArrayList<>();
        when(operatorApiService.findActiveOperators()).thenReturn(activeOperators());
        doAnswer(invocation -> {
            operators.add(invocation.getArgument(0, OperatorId.class));
            invocation.getArgument(1, Runnable.class).run();
            return null;
        }).when(operatorContext).runAs(any(OperatorId.class), any(Runnable.class));
        doThrow(new IllegalStateException("Database is unavailable")).doNothing()
                .when(paymentPort).expireOverdue(NOW);
        final PaymentDeadlineJob job = new PaymentDeadlineJob(paymentPort, operatorApiService, operatorContext,
                Clock.fixed(NOW, ZoneOffset.UTC));

        job.expireOverduePayments();

        assertThat(operators).containsExactly(OperatorId.of(1L), OperatorId.of(2L));
        verify(paymentPort, times(2)).expireOverdue(NOW);
    }

    private Set<OperatorIdDto> activeOperators() {
        final Set<OperatorIdDto> operators = new LinkedHashSet<>();
        operators.add(new OperatorIdDto(1L));
        operators.add(new OperatorIdDto(2L));
        return operators;
    }
}
