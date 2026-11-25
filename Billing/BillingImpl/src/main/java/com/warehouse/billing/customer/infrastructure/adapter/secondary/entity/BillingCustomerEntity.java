package com.warehouse.billing.customer.infrastructure.adapter.secondary.entity;

import com.warehouse.billing.customer.domain.enumeration.BillingCustomerStatus;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Entity(name = "billing.BillingCustomerEntity")
@Table(name = "billing_customers")
public class BillingCustomerEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private BillingCustomerId billingCustomerId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "tax_id", nullable = false, length = 32))
    private TaxId taxId;

    @Column(name = "street")
    private String street;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "country_code", length = 2)
    private String countryCode;

    @Column(name = "invoice_email", nullable = false)
    private String invoiceEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BillingCustomerStatus status;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false, unique = true))
    private UserId userId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public BillingCustomerEntity(final BillingCustomerId billingCustomerId, final String companyName,
                                 final TaxId taxId, final String street, final String postalCode,
                                 final String city, final String countryCode, final String invoiceEmail,
                                 final BillingCustomerStatus status, final UserId userId,
                                 final Instant createdAt, final Instant updatedAt) {
        this.billingCustomerId = billingCustomerId;
        this.companyName = companyName;
        this.taxId = taxId;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.countryCode = countryCode;
        this.invoiceEmail = invoiceEmail;
        this.status = status;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
