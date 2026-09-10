package com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.mapper;

import com.warehouse.supplierconfiguration.domain.model.SupplierConfiguration;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfigurationPolicy;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationChange;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationPolicyId;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationRevision;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationSnapshot;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationValues;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.entity.SupplierConfigurationChangeEntity;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.entity.SupplierConfigurationEntity;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.entity.SupplierConfigurationPolicyEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SupplierConfigurationPersistenceMapper {

    public SupplierConfigurationEntity toEntity(final SupplierConfiguration supplierConfiguration) {
        final SupplierConfigurationSnapshot snapshot = supplierConfiguration.snapshot();
        final SupplierConfigurationValues values = snapshot.values();
        return new SupplierConfigurationEntity(
                snapshot.supplierId(),
                values.settings(),
                values.maxDeliveryAttempts(),
                values.maxShipmentsPerRoute(),
                values.substituteSupplierId(),
                values.preferredLabelFormat(),
                values.defaultShipmentSortOrder(),
                snapshot.version(),
                snapshot.createdAt(),
                snapshot.updatedAt(),
                snapshot.updatedBy());
    }

    public SupplierConfiguration toModel(final SupplierConfigurationEntity entity) {
        final SupplierConfigurationValues values = SupplierConfigurationValues.defaults()
                .withSettings(entity.getSettings());
        return SupplierConfiguration.restore(new SupplierConfigurationSnapshot(
                entity.getSupplierId(),
                new SupplierConfigurationValues(
                        values.settings(),
                        entity.getMaxDeliveryAttempts(),
                        entity.getMaxShipmentsPerRoute(),
                        entity.getSubstituteSupplierId(),
                        entity.getPreferredLabelFormat(),
                        entity.getDefaultShipmentSortOrder()),
                entity.getVersion(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getUpdatedBy()));
    }

    public SupplierConfigurationPolicyEntity toEntity(final SupplierConfigurationPolicy policy) {
        return new SupplierConfigurationPolicyEntity(
                policy.getPolicyId().value(),
                policy.getEnforcedSettings(),
                policy.getVersion(),
                policy.getUpdatedAt(),
                policy.getUpdatedBy());
    }

    public SupplierConfigurationPolicy toModel(final SupplierConfigurationPolicyEntity entity) {
        return new SupplierConfigurationPolicy(
                new SupplierConfigurationPolicyId(entity.getPolicyId()),
                entity.getEnforcedSettings(),
                entity.getVersion(),
                entity.getUpdatedAt(),
                entity.getUpdatedBy());
    }

    public List<SupplierConfigurationChangeEntity> toEntities(final SupplierConfigurationRevision revision) {
        return revision.changes()
                .stream()
                .map(change -> new SupplierConfigurationChangeEntity(
                        UUID.randomUUID(),
                        revision.revisionId(),
                        revision.supplierId(),
                        revision.changeType(),
                        revision.changedBy(),
                        revision.changedAt(),
                        change.field(),
                        change.previousValue(),
                        change.newValue()))
                .toList();
    }

    public List<SupplierConfigurationRevision> toRevisions(final List<SupplierConfigurationChangeEntity> entities) {
        final Map<UUID, List<SupplierConfigurationChangeEntity>> entitiesByRevision = new LinkedHashMap<>();
        entities.stream()
                .sorted(Comparator.comparing(SupplierConfigurationChangeEntity::getChangedAt))
                .forEach(entity -> entitiesByRevision
                        .computeIfAbsent(entity.getRevisionId(), revisionId -> new ArrayList<>())
                        .add(entity));
        return entitiesByRevision.values()
                .stream()
                .map(this::toRevision)
                .toList();
    }

    private SupplierConfigurationRevision toRevision(final List<SupplierConfigurationChangeEntity> entities) {
        final SupplierConfigurationChangeEntity first = entities.getFirst();
        return new SupplierConfigurationRevision(
                first.getRevisionId(),
                first.getSupplierId(),
                first.getChangeType(),
                first.getChangedBy(),
                first.getChangedAt(),
                entities.stream()
                        .map(entity -> new SupplierConfigurationChange(entity.getField(), entity.getPreviousValue(),
                                entity.getNewValue()))
                        .toList());
    }
}
