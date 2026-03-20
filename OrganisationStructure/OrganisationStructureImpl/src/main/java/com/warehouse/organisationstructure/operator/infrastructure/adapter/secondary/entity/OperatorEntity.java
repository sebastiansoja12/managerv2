package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.operator.domain.model.OperatorStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;

import java.time.Instant;
import java.time.LocalDate;

@Entity(name = "organisationstructure.OperatorEntity")
@Table(name = "operators")
@Audited
public class OperatorEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "operator_id", nullable = false))
    private OperatorId operatorId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "registering_user_id"))
    private UserId registeringUserId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "tax_id", nullable = false))
    private TaxId taxId;

    @Column(name = "supports_lockers", nullable = false)
    private boolean supportsLockers;

    @Column(name = "supports_international_shipping", nullable = false)
    private boolean supportsInternationalShipping;

    @Column(name = "supports_cash_on_delivery", nullable = false)
    private boolean supportsCashOnDelivery;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "name", nullable = false)
    private String companyName;

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;

    @Column(name = "founded_date")
    private LocalDate foundedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OperatorStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public OperatorEntity() {
    }

    public OperatorEntity(final OperatorId operatorId,
                          final UserId registeringUserId,
                          final TaxId taxId,
                          final boolean supportsLockers,
                          final boolean supportsInternationalShipping,
                          final boolean supportsCashOnDelivery,
                          final String contactPhone,
                          final String contactEmail,
                          final String companyName,
                          final LocalDate contractStartDate,
                          final LocalDate contractEndDate,
                          final LocalDate foundedDate,
                          final OperatorStatus status,
                          final Instant createdAt,
                          final Instant updatedAt) {
        this.operatorId = operatorId;
        this.registeringUserId = registeringUserId;
        this.taxId = taxId;
        this.supportsLockers = supportsLockers;
        this.supportsInternationalShipping = supportsInternationalShipping;
        this.supportsCashOnDelivery = supportsCashOnDelivery;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.companyName = companyName;
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.foundedDate = foundedDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    void prePersist() {
        final Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
        if (status == null) {
            status = OperatorStatus.ACTIVE;
        }
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }

    public UserId getRegisteringUserId() {
        return registeringUserId;
    }

    public TaxId getTaxId() {
        return taxId;
    }

    public boolean isSupportsLockers() {
        return supportsLockers;
    }

    public boolean isSupportsInternationalShipping() {
        return supportsInternationalShipping;
    }

    public boolean isSupportsCashOnDelivery() {
        return supportsCashOnDelivery;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public LocalDate getContractStartDate() {
        return contractStartDate;
    }

    public LocalDate getContractEndDate() {
        return contractEndDate;
    }

    public LocalDate getFoundedDate() {
        return foundedDate;
    }

    public OperatorStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
