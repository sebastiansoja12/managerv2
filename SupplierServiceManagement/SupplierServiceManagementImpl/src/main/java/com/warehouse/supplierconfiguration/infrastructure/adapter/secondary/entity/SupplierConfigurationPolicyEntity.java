package com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierSetting;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity(name = "supplierConfiguration.SupplierConfigurationPolicyEntity")
@Table(name = "supplier_configuration_policies")
public class SupplierConfigurationPolicyEntity extends BelongsToOperator {

    @Id
    @Column(name = "policy_id", nullable = false)
    private UUID policyId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "supplier_configuration_policy_rules",
            joinColumns = @JoinColumn(name = "policy_id", nullable = false))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "setting", length = 64)
    @Column(name = "enforced_value", nullable = false)
    private Map<SupplierSetting, Boolean> enforcedSettings = new HashMap<>();

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "updated_by"))
    private UserId updatedBy;

    protected SupplierConfigurationPolicyEntity() {
    }

    public SupplierConfigurationPolicyEntity(final UUID policyId,
                                             final Map<SupplierSetting, Boolean> enforcedSettings,
                                             final Long version,
                                             final Instant updatedAt,
                                             final UserId updatedBy) {
        this.policyId = policyId;
        this.enforcedSettings = new HashMap<>(enforcedSettings);
        this.version = version;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public UUID getPolicyId() {
        return policyId;
    }

    public Map<SupplierSetting, Boolean> getEnforcedSettings() {
        return enforcedSettings;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public UserId getUpdatedBy() {
        return updatedBy;
    }
}
