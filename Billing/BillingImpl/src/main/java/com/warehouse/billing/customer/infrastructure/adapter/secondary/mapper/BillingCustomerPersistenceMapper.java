package com.warehouse.billing.customer.infrastructure.adapter.secondary.mapper;

import com.warehouse.billing.customer.domain.model.BillingCustomer;
import com.warehouse.billing.customer.domain.vo.BillingCustomerAddress;
import com.warehouse.billing.customer.domain.vo.BillingCustomerDetails;
import com.warehouse.billing.customer.infrastructure.adapter.secondary.entity.BillingCustomerEntity;

public class BillingCustomerPersistenceMapper {

    public BillingCustomerEntity toEntity(final BillingCustomer billingCustomer) {
        final BillingCustomerAddress address = billingCustomer.details().address();
        final BillingCustomerEntity entity = new BillingCustomerEntity(billingCustomer.id(),
                billingCustomer.details().companyName(), billingCustomer.taxId(), address.street(),
                address.postalCode(), address.city(), address.countryCode(), billingCustomer.details().invoiceEmail(),
                billingCustomer.status(), billingCustomer.userId(), billingCustomer.createdAt(),
                billingCustomer.updatedAt());
        entity.assignOperator(billingCustomer.operatorId());
        return entity;
    }

    public BillingCustomer toDomain(final BillingCustomerEntity entity) {
        return new BillingCustomer(entity.getBillingCustomerId(), entity.operatorId(), entity.getTaxId(),
                entity.getUserId(), new BillingCustomerDetails(entity.getCompanyName(),
                new BillingCustomerAddress(entity.getStreet(), entity.getPostalCode(), entity.getCity(),
                        entity.getCountryCode()), entity.getInvoiceEmail()),
                entity.getStatus(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
