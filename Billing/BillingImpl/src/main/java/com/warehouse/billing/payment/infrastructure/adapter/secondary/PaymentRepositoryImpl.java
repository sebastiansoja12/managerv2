package com.warehouse.billing.payment.infrastructure.adapter.secondary;

import com.warehouse.billing.payment.application.port.primary.command.FindPaymentsCommand;
import com.warehouse.billing.payment.application.port.primary.result.PaymentPage;
import com.warehouse.billing.payment.application.port.secondary.PaymentRepository;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.entity.PaymentEntity;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.mapper.PaymentPersistenceMapper;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.NoOperatorContextRepository;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class PaymentRepositoryImpl implements PaymentRepository {

    private final OperatorFilteredRepository<PaymentEntity> repository;

    private final NoOperatorContextRepository<PaymentEntity> platformRepository;

    private final PaymentPersistenceMapper persistenceMapper;

    public PaymentRepositoryImpl(final OperatorFilteredRepository<PaymentEntity> repository,
                                 final NoOperatorContextRepository<PaymentEntity> platformRepository,
                                 final PaymentPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.platformRepository = platformRepository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void save(final Payment payment) {
        final PaymentEntity entity = this.persistenceMapper.toEntity(payment);
        if (payment.version() == null) {
            this.repository.create(entity);
        } else {
            this.repository.update(entity);
        }
    }

    @Override
    public Optional<Payment> findById(final PaymentId paymentId) {
        return byId(paymentId).one().map(this.persistenceMapper::toDomain);
    }

    @Override
    public Optional<Payment> findByPriceQuote(final PriceQuoteId priceQuoteId) {
        return this.repository.createCriteria(PaymentEntity.class)
                .eq("priceQuoteId.value", priceQuoteId.value())
                .one()
                .map(this.persistenceMapper::toDomain);
    }

    @Override
    public Optional<Payment> findByGatewayReference(final String gatewayReference) {
        return this.repository.createCriteria(PaymentEntity.class)
                .eq("gatewayReference", gatewayReference)
                .one()
                .map(this.persistenceMapper::toDomain);
    }

    @Override
    public Optional<Payment> findByGatewayPaymentIntent(final String gatewayPaymentIntent) {
        return this.repository.createCriteria(PaymentEntity.class)
                .eq("gatewayPaymentIntent", gatewayPaymentIntent)
                .one()
                .map(this.persistenceMapper::toDomain);
    }

    @Override
    public Optional<OperatorId> findOperatorByGatewayPaymentIntent(final String gatewayPaymentIntent) {
        return this.platformRepository.createCriteria(PaymentEntity.class)
                .eq("gatewayPaymentIntent", gatewayPaymentIntent)
                .one()
                .map(PaymentEntity::operatorId);
    }

    @Override
    public Optional<OperatorId> findOperatorByGatewayReference(final String gatewayReference) {
        return this.platformRepository.createCriteria(PaymentEntity.class)
                .eq("gatewayReference", gatewayReference)
                .one()
                .map(PaymentEntity::operatorId);
    }

    @Override
    public Optional<OperatorId> findOperatorByPaymentId(final PaymentId paymentId) {
        return this.platformRepository.createCriteria(PaymentEntity.class)
                .eq("paymentId.value", paymentId.value())
                .one()
                .map(PaymentEntity::operatorId);
    }

    @Override
    public List<Payment> findOverduePending(final Instant now, final int limit) {
        return this.repository.createCriteria(PaymentEntity.class)
                .eq("status", PaymentStatus.PENDING)
                .lt("deadline", now)
                .asc("deadline")
                .maxResults(limit)
                .list()
                .stream()
                .map(this.persistenceMapper::toDomain)
                .toList();
    }

    @Override
    public PaymentPage find(final FindPaymentsCommand command) {
        final long totalElements = criteria(command).count();
        final List<Payment> payments = criteria(command)
                .desc("createdAt")
                .firstResult(command.page() * command.size())
                .maxResults(command.size())
                .list()
                .stream()
                .map(this.persistenceMapper::toDomain)
                .toList();
        return new PaymentPage(payments, command.page(), command.size(), totalElements);
    }

    private Criteria<PaymentEntity> byId(final PaymentId paymentId) {
        return this.repository.createCriteria(PaymentEntity.class).eq("paymentId.value", paymentId.value());
    }

    private Criteria<PaymentEntity> criteria(final FindPaymentsCommand command) {
        final Criteria<PaymentEntity> criteria = this.repository.createCriteria(PaymentEntity.class);
        if (command.status() != null) {
            criteria.eq("status", command.status());
        }
        if (command.priceQuoteId() != null) {
            criteria.eq("priceQuoteId.value", command.priceQuoteId().value());
        }
        return criteria;
    }
}
