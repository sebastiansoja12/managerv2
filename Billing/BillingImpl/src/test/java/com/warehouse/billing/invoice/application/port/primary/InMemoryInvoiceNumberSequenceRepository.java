package com.warehouse.billing.invoice.application.port.primary;

import com.warehouse.billing.invoice.application.port.secondary.InvoiceNumberSequenceRepository;
import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.commonassets.identificator.OperatorId;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

class InMemoryInvoiceNumberSequenceRepository implements InvoiceNumberSequenceRepository {

    private final Map<String, Long> sequences = new HashMap<>();

    private final Supplier<OperatorId> currentOperator;

    InMemoryInvoiceNumberSequenceRepository(final Supplier<OperatorId> currentOperator) {
        this.currentOperator = currentOperator;
    }

    @Override
    public long next(final InvoiceType type, final YearMonth period) {
        return this.sequences.merge(this.currentOperator.get().value() + "/" + type + "/" + period, 1L, Long::sum);
    }
}
