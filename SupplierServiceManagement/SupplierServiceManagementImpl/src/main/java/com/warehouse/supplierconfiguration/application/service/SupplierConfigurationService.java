package com.warehouse.supplierconfiguration.application.service;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplierconfiguration.application.exception.SupplierConfigurationConflictException;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationHistoryRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationPolicyRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationRepository;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationChangeType;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfiguration;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfigurationPolicy;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationChange;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationRevision;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SupplierConfigurationService {

    private final SupplierConfigurationRepository supplierConfigurationRepository;
    private final SupplierConfigurationPolicyRepository supplierConfigurationPolicyRepository;
    private final SupplierConfigurationHistoryRepository supplierConfigurationHistoryRepository;

    public SupplierConfigurationService(
            final SupplierConfigurationRepository supplierConfigurationRepository,
            final SupplierConfigurationPolicyRepository supplierConfigurationPolicyRepository,
            final SupplierConfigurationHistoryRepository supplierConfigurationHistoryRepository) {
        this.supplierConfigurationRepository = supplierConfigurationRepository;
        this.supplierConfigurationPolicyRepository = supplierConfigurationPolicyRepository;
        this.supplierConfigurationHistoryRepository = supplierConfigurationHistoryRepository;
    }

    public SupplierConfigurationPolicy currentPolicy() {
        return this.supplierConfigurationPolicyRepository.findCurrent()
                .orElseGet(SupplierConfigurationPolicy::empty);
    }

    public SupplierConfigurationPolicy findPolicyForChange(final Long expectedVersion) {
        final SupplierConfigurationPolicy policy = currentPolicy();
        if (!Objects.equals(policy.getVersion(), expectedVersion)) {
            throw new SupplierConfigurationConflictException(
                    "Supplier configuration policy was changed by another user");
        }
        return policy;
    }

    public SupplierConfigurationPolicy storePolicy(final SupplierConfigurationPolicy policy) {
        return this.supplierConfigurationPolicyRepository.save(policy);
    }

    public SupplierConfiguration findOrDefault(final SupplierId supplierId,
                                               final SupplierConfigurationPolicy policy,
                                               final Instant timestamp) {
        return this.supplierConfigurationRepository.findBySupplierId(supplierId)
                .orElseGet(() -> SupplierConfiguration.createDefault(supplierId, policy, timestamp));
    }

    public SupplierConfiguration findOrCreate(final SupplierId supplierId,
                                              final SupplierConfigurationPolicy policy,
                                              final Instant timestamp) {
        return this.supplierConfigurationRepository.findBySupplierId(supplierId)
                .orElseGet(() -> this.supplierConfigurationRepository.save(
                        SupplierConfiguration.createDefault(supplierId, policy, timestamp)));
    }

    public SupplierConfiguration findForChange(final SupplierId supplierId,
                                               final SupplierConfigurationPolicy policy,
                                               final Long expectedVersion,
                                               final Instant timestamp) {
        final SupplierConfiguration configuration = findOrDefault(supplierId, policy, timestamp);
        if (!Objects.equals(configuration.getVersion(), expectedVersion)) {
            throw new SupplierConfigurationConflictException("Supplier configuration was changed by another user");
        }
        return configuration;
    }

    public SupplierConfiguration store(final SupplierConfiguration configuration,
                                       final List<SupplierConfigurationChange> changes,
                                       final SupplierConfigurationChangeType changeType,
                                       final UserId changedBy,
                                       final Instant timestamp) {
        if (changes.isEmpty() && configuration.getVersion() != null) {
            return configuration;
        }
        final SupplierConfiguration savedConfiguration = this.supplierConfigurationRepository.save(configuration);
        if (!changes.isEmpty()) {
            this.supplierConfigurationHistoryRepository.append(new SupplierConfigurationRevision(UUID.randomUUID(),
                    configuration.getSupplierId(), changeType, changedBy, timestamp, changes));
        }
        return savedConfiguration;
    }

    public List<SupplierConfigurationRevision> findHistory(final SupplierId supplierId) {
        return this.supplierConfigurationHistoryRepository.findBySupplierId(supplierId)
                .stream()
                .sorted(Comparator.comparing(SupplierConfigurationRevision::changedAt).reversed())
                .toList();
    }

    public void releaseSubstitute(final SupplierId substituteSupplierId,
                                  final UserId changedBy,
                                  final Instant timestamp) {
        this.supplierConfigurationRepository.findBySubstituteSupplierId(substituteSupplierId)
                .forEach(configuration -> store(configuration, configuration.releaseSubstitute(changedBy, timestamp),
                        SupplierConfigurationChangeType.UPDATED, changedBy, timestamp));
    }

    public void remove(final SupplierId supplierId) {
        this.supplierConfigurationRepository.deleteBySupplierId(supplierId);
    }
}
