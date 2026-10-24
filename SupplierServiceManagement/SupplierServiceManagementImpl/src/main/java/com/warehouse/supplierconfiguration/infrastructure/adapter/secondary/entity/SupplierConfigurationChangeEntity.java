package com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationChangeType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "supplierConfiguration.SupplierConfigurationChangeEntity")
@Table(name = "supplier_configuration_changes")
public class SupplierConfigurationChangeEntity extends BelongsToOperator {

    @Id
    @Column(name = "change_id", nullable = false)
    private UUID changeId;

    @Column(name = "revision_id", nullable = false)
    private UUID revisionId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "supplier_id", nullable = false))
    private SupplierId supplierId;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false, length = 32)
    private SupplierConfigurationChangeType changeType;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "changed_by"))
    private UserId changedBy;

    @Column(name = "changed_at", nullable = false)
    private Instant changedAt;

    @Column(name = "field", nullable = false, length = 64)
    private String field;

    @Column(name = "previous_value", length = 64)
    private String previousValue;

    @Column(name = "new_value", length = 64)
    private String newValue;

    protected SupplierConfigurationChangeEntity() {
    }

    public SupplierConfigurationChangeEntity(final UUID changeId,
                                             final UUID revisionId,
                                             final SupplierId supplierId,
                                             final SupplierConfigurationChangeType changeType,
                                             final UserId changedBy,
                                             final Instant changedAt,
                                             final String field,
                                             final String previousValue,
                                             final String newValue) {
        this.changeId = changeId;
        this.revisionId = revisionId;
        this.supplierId = supplierId;
        this.changeType = changeType;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
        this.field = field;
        this.previousValue = previousValue;
        this.newValue = newValue;
    }

    public UUID getChangeId() {
        return changeId;
    }

    public UUID getRevisionId() {
        return revisionId;
    }

    public SupplierId getSupplierId() {
        return supplierId;
    }

    public SupplierConfigurationChangeType getChangeType() {
        return changeType;
    }

    public UserId getChangedBy() {
        return changedBy;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public String getField() {
        return field;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public String getNewValue() {
        return newValue;
    }
}
