package com.warehouse.billing.customer.infrastructure.adapter.secondary;

import com.warehouse.billing.customer.application.port.secondary.BillingCustomerRepository;
import com.warehouse.billing.customer.domain.model.BillingCustomer;
import com.warehouse.billing.customer.infrastructure.adapter.secondary.entity.BillingCustomerEntity;
import com.warehouse.billing.customer.infrastructure.adapter.secondary.mapper.BillingCustomerPersistenceMapper;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;

import java.util.List;
import java.util.Optional;

public class BillingCustomerRepositoryImpl implements BillingCustomerRepository {

    private final OperatorFilteredRepository<BillingCustomerEntity> repository;

    private final BillingCustomerPersistenceMapper persistenceMapper;

    public BillingCustomerRepositoryImpl(final OperatorFilteredRepository<BillingCustomerEntity> repository,
                                         final BillingCustomerPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void create(final BillingCustomer billingCustomer) {
        this.repository.create(this.persistenceMapper.toEntity(billingCustomer));
    }

    @Override
    public void update(final BillingCustomer billingCustomer) {
        this.repository.update(this.persistenceMapper.toEntity(billingCustomer));
    }

    @Override
    public Optional<BillingCustomer> findById(final BillingCustomerId billingCustomerId) {
        return this.repository.createCriteria(BillingCustomerEntity.class)
                .eq("billingCustomerId.value", billingCustomerId.value())
                .one()
                .map(this.persistenceMapper::toDomain);
    }

    @Override
    public Optional<BillingCustomer> findByUser(final UserId userId) {
        return this.repository.createCriteria(BillingCustomerEntity.class)
                .eq("userId.value", userId.value())
                .one()
                .map(this.persistenceMapper::toDomain);
    }

    @Override
    public Optional<BillingCustomer> findByTaxId(final TaxId taxId) {
        return this.repository.createCriteria(BillingCustomerEntity.class)
                .eq("taxId.value", taxId.value())
                .one()
                .map(this.persistenceMapper::toDomain);
    }

    @Override
    public List<BillingCustomer> findAll() {
        return this.repository.createCriteria(BillingCustomerEntity.class)
                .asc("companyName")
                .list()
                .stream()
                .map(this.persistenceMapper::toDomain)
                .toList();
    }
}
