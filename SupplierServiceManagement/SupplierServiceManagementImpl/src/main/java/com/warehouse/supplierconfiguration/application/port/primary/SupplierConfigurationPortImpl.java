package com.warehouse.supplierconfiguration.application.port.primary;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplierconfiguration.application.exception.SupplierConfigurationAccessDeniedException;
import com.warehouse.supplierconfiguration.application.exception.SupplierConfigurationConflictException;
import com.warehouse.supplierconfiguration.application.exception.SupplierConfigurationNotFoundException;
import com.warehouse.supplierconfiguration.application.port.primary.command.ResetSupplierConfigurationCommand;
import com.warehouse.supplierconfiguration.application.port.primary.command.SupplierConfigurationTarget;
import com.warehouse.supplierconfiguration.application.port.primary.command.UpdateSupplierConfigurationCommand;
import com.warehouse.supplierconfiguration.application.port.primary.command.UpdateSupplierConfigurationPolicyCommand;
import com.warehouse.supplierconfiguration.application.port.primary.result.SubstituteSupplierResult;
import com.warehouse.supplierconfiguration.application.port.primary.result.SupplierConfigurationResult;
import com.warehouse.supplierconfiguration.application.port.secondary.CurrentUserServicePort;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationHistoryRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationPolicyRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierDirectoryServicePort;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierProfile;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationChangeType;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationParameter;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationViolationCode;
import com.warehouse.supplierconfiguration.domain.exception.InvalidSupplierConfigurationException;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfiguration;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfigurationPolicy;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationChange;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationRevision;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationValues;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationViolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SupplierConfigurationPortImpl implements SupplierConfigurationPort {

    private final SupplierConfigurationRepository supplierConfigurationRepository;
    private final SupplierConfigurationPolicyRepository supplierConfigurationPolicyRepository;
    private final SupplierConfigurationHistoryRepository supplierConfigurationHistoryRepository;
    private final SupplierDirectoryServicePort supplierDirectoryServicePort;
    private final CurrentUserServicePort currentUserServicePort;
    private final Clock clock;

    public SupplierConfigurationPortImpl(
            final SupplierConfigurationRepository supplierConfigurationRepository,
            final SupplierConfigurationPolicyRepository supplierConfigurationPolicyRepository,
            final SupplierConfigurationHistoryRepository supplierConfigurationHistoryRepository,
            final SupplierDirectoryServicePort supplierDirectoryServicePort,
            final CurrentUserServicePort currentUserServicePort,
            final Clock clock) {
        this.supplierConfigurationRepository = supplierConfigurationRepository;
        this.supplierConfigurationPolicyRepository = supplierConfigurationPolicyRepository;
        this.supplierConfigurationHistoryRepository = supplierConfigurationHistoryRepository;
        this.supplierDirectoryServicePort = supplierDirectoryServicePort;
        this.currentUserServicePort = currentUserServicePort;
        this.clock = clock;
    }

    @Override
    @Transactional
    public SupplierConfigurationResult get(final SupplierConfigurationTarget target) {
        final SupplierProfile supplier = resolveSupplier(target);
        final SupplierConfigurationPolicy policy = currentPolicy();
        final SupplierConfiguration configuration = this.supplierConfigurationRepository
                .findBySupplierId(supplier.supplierId())
                .orElseGet(() -> this.supplierConfigurationRepository.save(
                        SupplierConfiguration.createDefault(supplier.supplierId(), policy, this.clock.instant())));
        return result(supplier, configuration, policy);
    }

    @Override
    @Transactional
    public SupplierConfigurationResult update(final UpdateSupplierConfigurationCommand command) {
        final SupplierProfile supplier = resolveSupplier(command.target());
        final SupplierConfigurationPolicy policy = currentPolicy();
        final SupplierConfiguration configuration = findOrDefault(supplier, policy);
        ensureVersion(configuration, command.expectedVersion());
        final SupplierConfigurationValues requestedValues = withSubstitute(command.values(),
                resolveSubstitute(command.substituteSupplierCode()));
        final Instant timestamp = this.clock.instant();
        final UserId changedBy = this.currentUserServicePort.getCurrentUserId();
        final List<SupplierConfigurationChange> changes = configuration.change(requestedValues, policy, changedBy,
                timestamp);
        return persist(supplier, configuration, policy, changes, SupplierConfigurationChangeType.UPDATED, changedBy,
                timestamp);
    }

    @Override
    @Transactional
    public SupplierConfigurationResult resetToDefaults(final ResetSupplierConfigurationCommand command) {
        final SupplierProfile supplier = resolveSupplier(command.target());
        final SupplierConfigurationPolicy policy = currentPolicy();
        final SupplierConfiguration configuration = findOrDefault(supplier, policy);
        ensureVersion(configuration, command.expectedVersion());
        final Instant timestamp = this.clock.instant();
        final UserId changedBy = this.currentUserServicePort.getCurrentUserId();
        final List<SupplierConfigurationChange> changes = configuration.resetToDefaults(policy, changedBy, timestamp);
        return persist(supplier, configuration, policy, changes, SupplierConfigurationChangeType.RESET_TO_DEFAULTS,
                changedBy, timestamp);
    }

    @Override
    public List<SupplierConfigurationRevision> getHistory(final SupplierConfigurationTarget target) {
        final SupplierProfile supplier = resolveSupplier(target);
        return this.supplierConfigurationHistoryRepository.findBySupplierId(supplier.supplierId())
                .stream()
                .sorted(Comparator.comparing(SupplierConfigurationRevision::changedAt).reversed())
                .toList();
    }

    @Override
    public List<SubstituteSupplierResult> getSubstituteCandidates(final SupplierConfigurationTarget target) {
        final SupplierProfile supplier = resolveSupplier(target);
        return this.supplierDirectoryServicePort.findActiveSuppliers()
                .stream()
                .filter(candidate -> !candidate.supplierId().equals(supplier.supplierId()))
                .map(candidate -> new SubstituteSupplierResult(candidate.supplierId(), candidate.supplierCode(),
                        candidate.firstName(), candidate.lastName()))
                .toList();
    }

    @Override
    public SupplierConfigurationPolicy getPolicy() {
        return currentPolicy();
    }

    @Override
    @Transactional
    public SupplierConfigurationPolicy updatePolicy(final UpdateSupplierConfigurationPolicyCommand command) {
        final SupplierConfigurationPolicy policy = currentPolicy();
        if (!Objects.equals(policy.getVersion(), command.expectedVersion())) {
            throw new SupplierConfigurationConflictException("Supplier configuration policy was changed by another user");
        }
        policy.changeEnforcedSettings(command.enforcedSettings(), this.currentUserServicePort.getCurrentUserId(),
                this.clock.instant());
        return this.supplierConfigurationPolicyRepository.save(policy);
    }

    @Override
    public Optional<SupplierConfigurationResult> findEffectiveBySupplierId(final SupplierId supplierId) {
        return this.supplierDirectoryServicePort.findBySupplierId(supplierId)
                .map(this::effectiveResult);
    }

    @Override
    public Optional<SupplierConfigurationResult> findEffectiveByUserId(final UserId userId) {
        return this.supplierDirectoryServicePort.findByUserId(userId)
                .map(this::effectiveResult);
    }

    private SupplierConfigurationResult effectiveResult(final SupplierProfile supplier) {
        final SupplierConfigurationPolicy policy = currentPolicy();
        return result(supplier, findOrDefault(supplier, policy), policy);
    }

    private SupplierConfigurationResult persist(final SupplierProfile supplier,
                                                final SupplierConfiguration configuration,
                                                final SupplierConfigurationPolicy policy,
                                                final List<SupplierConfigurationChange> changes,
                                                final SupplierConfigurationChangeType changeType,
                                                final UserId changedBy,
                                                final Instant timestamp) {
        if (changes.isEmpty() && configuration.getVersion() != null) {
            return result(supplier, configuration, policy);
        }
        final SupplierConfiguration savedConfiguration = this.supplierConfigurationRepository.save(configuration);
        if (!changes.isEmpty()) {
            this.supplierConfigurationHistoryRepository.append(new SupplierConfigurationRevision(UUID.randomUUID(),
                    supplier.supplierId(), changeType, changedBy, timestamp, changes));
        }
        return result(supplier, savedConfiguration, policy);
    }

    private SupplierProfile resolveSupplier(final SupplierConfigurationTarget target) {
        return switch (target) {
            case SupplierConfigurationTarget.CurrentSupplier currentSupplier -> this.supplierDirectoryServicePort
                    .findByUserId(this.currentUserServicePort.getCurrentUserId())
                    .orElseThrow(() -> new SupplierConfigurationNotFoundException(
                            "Current user is not assigned to any supplier"));
            case SupplierConfigurationTarget.ManagedSupplier managedSupplier -> {
                final SupplierProfile supplier = this.supplierDirectoryServicePort
                        .findBySupplierCode(managedSupplier.supplierCode())
                        .orElseThrow(() -> new SupplierConfigurationNotFoundException(
                                "Supplier not found: " + managedSupplier.supplierCode().value()));
                if (!this.currentUserServicePort.canAccessDepartment(supplier.departmentId())) {
                    throw new SupplierConfigurationAccessDeniedException(
                            "Supplier is outside of the current user organizational scope");
                }
                yield supplier;
            }
        };
    }

    private SupplierConfiguration findOrDefault(final SupplierProfile supplier,
                                                final SupplierConfigurationPolicy policy) {
        return this.supplierConfigurationRepository.findBySupplierId(supplier.supplierId())
                .orElseGet(() -> SupplierConfiguration.createDefault(supplier.supplierId(), policy,
                        this.clock.instant()));
    }

    private SupplierConfigurationPolicy currentPolicy() {
        return this.supplierConfigurationPolicyRepository.findCurrent()
                .orElseGet(SupplierConfigurationPolicy::empty);
    }

    private void ensureVersion(final SupplierConfiguration configuration, final Long expectedVersion) {
        if (!Objects.equals(configuration.getVersion(), expectedVersion)) {
            throw new SupplierConfigurationConflictException("Supplier configuration was changed by another user");
        }
    }

    private SupplierId resolveSubstitute(final SupplierCode substituteSupplierCode) {
        if (substituteSupplierCode == null || substituteSupplierCode.value() == null
                || substituteSupplierCode.value().isBlank()) {
            return null;
        }
        return this.supplierDirectoryServicePort.findBySupplierCode(substituteSupplierCode)
                .filter(SupplierProfile::active)
                .map(SupplierProfile::supplierId)
                .orElseThrow(() -> new InvalidSupplierConfigurationException(List.of(new SupplierConfigurationViolation(
                        SupplierConfigurationParameter.SUBSTITUTE_SUPPLIER_ID.name(),
                        SupplierConfigurationViolationCode.SUBSTITUTE_UNAVAILABLE))));
    }

    private SupplierConfigurationValues withSubstitute(final SupplierConfigurationValues values,
                                                       final SupplierId substituteSupplierId) {
        return new SupplierConfigurationValues(values.settings(), values.maxDeliveryAttempts(),
                values.maxShipmentsPerRoute(), substituteSupplierId, values.preferredLabelFormat(),
                values.defaultShipmentSortOrder());
    }

    private SupplierCode substituteSupplierCode(final SupplierConfigurationValues values) {
        if (values.substituteSupplierId() == null) {
            return null;
        }
        return this.supplierDirectoryServicePort.findBySupplierId(values.substituteSupplierId())
                .map(SupplierProfile::supplierCode)
                .orElse(null);
    }

    private SupplierConfigurationResult result(final SupplierProfile supplier,
                                               final SupplierConfiguration configuration,
                                               final SupplierConfigurationPolicy policy) {
        final SupplierConfigurationValues effectiveValues = configuration.effectiveValues(policy);
        return new SupplierConfigurationResult(
                supplier.supplierId(),
                supplier.supplierCode(),
                supplier.firstName(),
                supplier.lastName(),
                effectiveValues,
                substituteSupplierCode(effectiveValues),
                policy.getEnforcedSettings(),
                configuration.getVersion(),
                configuration.snapshot().updatedAt(),
                configuration.snapshot().updatedBy());
    }
}
