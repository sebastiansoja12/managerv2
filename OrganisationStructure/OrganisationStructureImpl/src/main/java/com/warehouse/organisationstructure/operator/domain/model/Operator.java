package com.warehouse.organisationstructure.operator.domain.model;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorProvisioningDetails;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class Operator {

    private OperatorId operatorId;
    private UserId registeringUserId;
    private TaxId taxId;
    private boolean supportsLockers;
    private boolean supportsInternationalShipping;
    private boolean supportsCashOnDelivery;
    private String contactPhone;
    private String contactEmail;
    private String companyName;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private LocalDate foundedDate;
    private OperatorConfiguration configuration;
    private OperatorProvisioningDetails provisioningDetails;
    private OperatorStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public Operator(final OperatorId operatorId,
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
                    final OperatorConfiguration configuration,
                    final OperatorStatus status,
                    final Instant createdAt,
                    final Instant updatedAt) {
        this(operatorId, registeringUserId, taxId, supportsLockers, supportsInternationalShipping,
                supportsCashOnDelivery, contactPhone, contactEmail, companyName, contractStartDate, contractEndDate,
                foundedDate, configuration, null, status, createdAt, updatedAt);
    }

    public Operator(final OperatorId operatorId,
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
                    final OperatorConfiguration configuration,
                    final OperatorProvisioningDetails provisioningDetails,
                    final OperatorStatus status,
                    final Instant createdAt,
                    final Instant updatedAt) {
        this.operatorId = Objects.requireNonNull(operatorId, "OperatorId must not be null");
        this.registeringUserId = registeringUserId;
        this.taxId = Objects.requireNonNull(taxId, "Tax id must not be null");
        this.supportsLockers = supportsLockers;
        this.supportsInternationalShipping = supportsInternationalShipping;
        this.supportsCashOnDelivery = supportsCashOnDelivery;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.companyName = Objects.requireNonNull(companyName, "Company name must not be null");
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.foundedDate = foundedDate;
        this.configuration = configuration != null
                ? configuration
                : OperatorConfiguration.defaultFor(supportsInternationalShipping, supportsCashOnDelivery, supportsLockers);
        this.provisioningDetails = provisioningDetails;
        this.status = Objects.requireNonNull(status, "Operator status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "UpdatedAt must not be null");
    }

    public boolean isActive() {
        return OperatorStatus.ACTIVE.equals(status);
    }

    public OperatorSnapshot snapshot() {
        return new OperatorSnapshot(
                operatorId,
                registeringUserId,
                taxId,
                supportsLockers,
                supportsInternationalShipping,
                supportsCashOnDelivery,
                contactPhone,
                contactEmail,
                companyName,
                contractStartDate,
                contractEndDate,
                foundedDate,
                configuration,
                provisioningDetails,
                status,
                createdAt,
                updatedAt
        );
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

    public OperatorConfiguration getConfiguration() {
        return configuration;
    }

    public OperatorProvisioningDetails getProvisioningDetails() {
        return provisioningDetails;
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

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setRegisteringUserId(final UserId registeringUserId) {
        this.registeringUserId = registeringUserId;
    }

    public void setTaxId(final TaxId taxId) {
        this.taxId = taxId;
    }

    public void setSupportsLockers(final boolean supportsLockers) {
        this.supportsLockers = supportsLockers;
    }

    public void setSupportsInternationalShipping(final boolean supportsInternationalShipping) {
        this.supportsInternationalShipping = supportsInternationalShipping;
    }

    public void setSupportsCashOnDelivery(final boolean supportsCashOnDelivery) {
        this.supportsCashOnDelivery = supportsCashOnDelivery;
    }

    public void setContactPhone(final String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setContactEmail(final String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setCompanyName(final String companyName) {
        this.companyName = companyName;
    }

    public void setContractStartDate(final LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public void setContractEndDate(final LocalDate contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public void setFoundedDate(final LocalDate foundedDate) {
        this.foundedDate = foundedDate;
    }

    public void setConfiguration(final OperatorConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setProvisioningDetails(final OperatorProvisioningDetails provisioningDetails) {
        this.provisioningDetails = provisioningDetails;
    }

    public void setOperatorId(final OperatorId operatorId) {
        this.operatorId = operatorId;
    }

    public void setStatus(final OperatorStatus status) {
        this.status = status;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void assignRegisteringUser(final UserId userId) {
        this.registeringUserId = userId;
        markAsModified();
    }

    private void markAsModified() {
        this.updatedAt = Instant.now();
    }
}
