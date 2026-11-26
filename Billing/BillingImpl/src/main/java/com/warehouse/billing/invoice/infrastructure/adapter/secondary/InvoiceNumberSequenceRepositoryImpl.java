package com.warehouse.billing.invoice.infrastructure.adapter.secondary;

import com.warehouse.billing.invoice.application.port.secondary.InvoiceNumberSequenceRepository;
import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity.InvoiceNumberSequenceEntity;
import com.warehouse.commonassets.identificator.InvoiceNumberSequenceId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;

import java.time.YearMonth;

public class InvoiceNumberSequenceRepositoryImpl implements InvoiceNumberSequenceRepository {

    private final InvoiceNumberSequenceJpaRepository invoiceNumberSequenceJpaRepository;

    private final OperatorFilteredRepository<InvoiceNumberSequenceEntity> repository;

    private final OperatorContextProvider operatorContextProvider;

    public InvoiceNumberSequenceRepositoryImpl(
            final InvoiceNumberSequenceJpaRepository invoiceNumberSequenceJpaRepository,
            final OperatorFilteredRepository<InvoiceNumberSequenceEntity> repository,
            final OperatorContextProvider operatorContextProvider) {
        this.invoiceNumberSequenceJpaRepository = invoiceNumberSequenceJpaRepository;
        this.repository = repository;
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public long next(final InvoiceType type, final YearMonth period) {
        final InvoiceNumberSequenceEntity sequence = this.invoiceNumberSequenceJpaRepository.findForUpdate(
                        this.operatorContextProvider.currentOperatorId().value(), type, period.toString())
                .orElseGet(() -> {
                    final InvoiceNumberSequenceEntity created = new InvoiceNumberSequenceEntity(
                            InvoiceNumberSequenceId.nextId(), type, period.toString());
                    this.repository.create(created);
                    return created;
                });
        return sequence.increment();
    }
}
