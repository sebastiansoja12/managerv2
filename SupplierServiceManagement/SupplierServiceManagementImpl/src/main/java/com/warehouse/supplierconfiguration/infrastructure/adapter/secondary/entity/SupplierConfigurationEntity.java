package com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.supplierconfiguration.domain.enumeration.LabelFormat;
import com.warehouse.supplierconfiguration.domain.enumeration.ShipmentSortOrder;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierSetting;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "supplierConfiguration.SupplierConfigurationEntity")
@Table(name = "supplier_configurations")
public class SupplierConfigurationEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "supplier_id", nullable = false))
    private SupplierId supplierId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "supplier_configuration_settings",
            joinColumns = @JoinColumn(name = "supplier_id", nullable = false))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "setting", length = 64)
    @Column(name = "enabled", nullable = false)
    private Map<SupplierSetting, Boolean> settings = new HashMap<>();

    @Column(name = "max_delivery_attempts", nullable = false)
    private Integer maxDeliveryAttempts;

    @Column(name = "max_shipments_per_route")
    private Integer maxShipmentsPerRoute;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "substitute_supplier_id"))
    private SupplierId substituteSupplierId;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_label_format", nullable = false, length = 16)
    private LabelFormat preferredLabelFormat;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_shipment_sort_order", nullable = false, length = 32)
    private ShipmentSortOrder defaultShipmentSortOrder;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "updated_by"))
    private UserId updatedBy;

    protected SupplierConfigurationEntity() {
    }

    public SupplierConfigurationEntity(final SupplierId supplierId,
                                       final Map<SupplierSetting, Boolean> settings,
                                       final Integer maxDeliveryAttempts,
                                       final Integer maxShipmentsPerRoute,
                                       final SupplierId substituteSupplierId,
                                       final LabelFormat preferredLabelFormat,
                                       final ShipmentSortOrder defaultShipmentSortOrder,
                                       final Long version,
                                       final Instant createdAt,
                                       final Instant updatedAt,
                                       final UserId updatedBy) {
        this.supplierId = supplierId;
        this.settings = new HashMap<>(settings);
        this.maxDeliveryAttempts = maxDeliveryAttempts;
        this.maxShipmentsPerRoute = maxShipmentsPerRoute;
        this.substituteSupplierId = substituteSupplierId;
        this.preferredLabelFormat = preferredLabelFormat;
        this.defaultShipmentSortOrder = defaultShipmentSortOrder;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public SupplierId getSupplierId() {
        return supplierId;
    }

    public Map<SupplierSetting, Boolean> getSettings() {
        return settings;
    }

    public Integer getMaxDeliveryAttempts() {
        return maxDeliveryAttempts;
    }

    public Integer getMaxShipmentsPerRoute() {
        return maxShipmentsPerRoute;
    }

    public SupplierId getSubstituteSupplierId() {
        return substituteSupplierId;
    }

    public LabelFormat getPreferredLabelFormat() {
        return preferredLabelFormat;
    }

    public ShipmentSortOrder getDefaultShipmentSortOrder() {
        return defaultShipmentSortOrder;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public UserId getUpdatedBy() {
        return updatedBy;
    }
}
