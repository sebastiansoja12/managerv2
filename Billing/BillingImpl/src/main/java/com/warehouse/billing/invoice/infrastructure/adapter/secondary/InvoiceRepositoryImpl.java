package com.warehouse.billing.invoice.infrastructure.adapter.secondary;

import com.warehouse.billing.invoice.application.port.primary.command.FindInvoicesCommand;
import com.warehouse.billing.invoice.application.port.primary.result.InvoicePage;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceRepository;
import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity.InvoiceEntity;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.mapper.InvoicePersistenceMapper;
import com.warehouse.commonassets.identificator.InvoiceId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;

import java.util.List;
import java.util.Optional;

public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final OperatorFilteredRepository<InvoiceEntity> repository;

    private final InvoicePersistenceMapper persistenceMapper;

    public InvoiceRepositoryImpl(final OperatorFilteredRepository<InvoiceEntity> repository,
                                 final InvoicePersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void save(final Invoice invoice) {
        final InvoiceEntity entity = this.persistenceMapper.toEntity(invoice);
        if (invoice.version() == null) {
            this.repository.create(entity);
        } else {
            this.repository.update(entity);
        }
    }

    @Override
    public Optional<Invoice> findById(final InvoiceId invoiceId) {
        return this.repository.createCriteria(InvoiceEntity.class)
                .eq("invoiceId.value", invoiceId.value())
                .one()
                .map(this.persistenceMapper::toDomain);
    }

    @Override
    public Optional<Invoice> findByPayment(final PaymentId paymentId, final InvoiceType type) {
        return this.repository.createCriteria(InvoiceEntity.class)
                .eq("paymentId.value", paymentId.value())
                .eq("type", type)
                .one()
                .map(this.persistenceMapper::toDomain);
    }

    @Override
    public InvoicePage find(final FindInvoicesCommand command) {
        final long totalElements = criteria(command).count();
        final List<Invoice> invoices = criteria(command)
                .desc("issuedAt")
                .firstResult(command.page() * command.size())
                .maxResults(command.size())
                .list()
                .stream()
                .map(this.persistenceMapper::toDomain)
                .toList();
        return new InvoicePage(invoices, command.page(), command.size(), totalElements);
    }

    private Criteria<InvoiceEntity> criteria(final FindInvoicesCommand command) {
        final Criteria<InvoiceEntity> criteria = this.repository.createCriteria(InvoiceEntity.class);
        if (command.priceQuoteId() != null) {
            criteria.eq("priceQuoteId.value", command.priceQuoteId().value());
        }
        return criteria;
    }
}
