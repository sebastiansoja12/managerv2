package com.warehouse.supplierconfiguration.application.port.primary;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplierconfiguration.application.port.primary.command.ResetSupplierConfigurationCommand;
import com.warehouse.supplierconfiguration.application.port.primary.command.SupplierConfigurationTarget;
import com.warehouse.supplierconfiguration.application.port.primary.command.UpdateSupplierConfigurationCommand;
import com.warehouse.supplierconfiguration.application.port.primary.command.UpdateSupplierConfigurationPolicyCommand;
import com.warehouse.supplierconfiguration.application.port.primary.result.SubstituteSupplierResult;
import com.warehouse.supplierconfiguration.application.port.primary.result.SupplierConfigurationResult;
import com.warehouse.supplierconfiguration.application.port.secondary.CurrentUserServicePort;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierDirectoryServicePort;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierProfile;
import com.warehouse.supplierconfiguration.application.service.SubstituteSupplierService;
import com.warehouse.supplierconfiguration.application.service.SupplierConfigurationResultFactory;
import com.warehouse.supplierconfiguration.application.service.SupplierConfigurationService;
import com.warehouse.supplierconfiguration.application.service.SupplierResolutionService;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationChangeType;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfiguration;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfigurationPolicy;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationChange;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationRevision;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationValues;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class SupplierConfigurationPortImpl implements SupplierConfigurationPort {

    private final SupplierConfigurationService supplierConfigurationService;
    private final SupplierResolutionService supplierResolutionService;
    private final SubstituteSupplierService substituteSupplierService;
    private final SupplierConfigurationResultFactory supplierConfigurationResultFactory;
    private final SupplierDirectoryServicePort supplierDirectoryServicePort;
    private final CurrentUserServicePort currentUserServicePort;
    private final Clock clock;

    public SupplierConfigurationPortImpl(
            final SupplierConfigurationService supplierConfigurationService,
            final SupplierResolutionService supplierResolutionService,
            final SubstituteSupplierService substituteSupplierService,
            final SupplierConfigurationResultFactory supplierConfigurationResultFactory,
            final SupplierDirectoryServicePort supplierDirectoryServicePort,
            final CurrentUserServicePort currentUserServicePort,
            final Clock clock) {
        this.supplierConfigurationService = supplierConfigurationService;
        this.supplierResolutionService = supplierResolutionService;
        this.substituteSupplierService = substituteSupplierService;
        this.supplierConfigurationResultFactory = supplierConfigurationResultFactory;
        this.supplierDirectoryServicePort = supplierDirectoryServicePort;
        this.currentUserServicePort = currentUserServicePort;
        this.clock = clock;
    }

    @Override
    @Transactional
    public SupplierConfigurationResult get(final SupplierConfigurationTarget target) {
        final SupplierProfile supplier = this.supplierResolutionService.resolve(target);
        final SupplierConfigurationPolicy policy = this.supplierConfigurationService.currentPolicy();
        final SupplierConfiguration configuration = this.supplierConfigurationService
                .findOrCreate(supplier.supplierId(), policy, this.clock.instant());
        return this.supplierConfigurationResultFactory.create(supplier, configuration, policy);
    }

    @Override
    @Transactional
    public SupplierConfigurationResult update(final UpdateSupplierConfigurationCommand command) {
        final SupplierProfile supplier = this.supplierResolutionService.resolve(command.target());
        final SupplierConfigurationPolicy policy = this.supplierConfigurationService.currentPolicy();
        final Instant timestamp = this.clock.instant();
        final SupplierConfiguration configuration = this.supplierConfigurationService
                .findForChange(supplier.supplierId(), policy, command.expectedVersion(), timestamp);
        final SupplierConfigurationValues requestedValues = command.values().withSubstitute(
                this.substituteSupplierService.resolveSubstituteId(command.substituteSupplierCode()));
        final UserId changedBy = this.currentUserServicePort.getCurrentUserId();
        final List<SupplierConfigurationChange> changes = configuration.change(requestedValues, policy, changedBy,
                timestamp);
        final SupplierConfiguration storedConfiguration = this.supplierConfigurationService.store(configuration,
                changes, SupplierConfigurationChangeType.UPDATED, changedBy, timestamp);
        return this.supplierConfigurationResultFactory.create(supplier, storedConfiguration, policy);
    }

    @Override
    @Transactional
    public SupplierConfigurationResult resetToDefaults(final ResetSupplierConfigurationCommand command) {
        final SupplierProfile supplier = this.supplierResolutionService.resolve(command.target());
        final SupplierConfigurationPolicy policy = this.supplierConfigurationService.currentPolicy();
        final Instant timestamp = this.clock.instant();
        final SupplierConfiguration configuration = this.supplierConfigurationService
                .findForChange(supplier.supplierId(), policy, command.expectedVersion(), timestamp);
        final UserId changedBy = this.currentUserServicePort.getCurrentUserId();
        final List<SupplierConfigurationChange> changes = configuration.resetToDefaults(policy, changedBy, timestamp);
        final SupplierConfiguration storedConfiguration = this.supplierConfigurationService.store(configuration,
                changes, SupplierConfigurationChangeType.RESET_TO_DEFAULTS, changedBy, timestamp);
        return this.supplierConfigurationResultFactory.create(supplier, storedConfiguration, policy);
    }

    @Override
    public List<SupplierConfigurationRevision> getHistory(final SupplierConfigurationTarget target) {
        final SupplierProfile supplier = this.supplierResolutionService.resolve(target);
        return this.supplierConfigurationService.findHistory(supplier.supplierId());
    }

    @Override
    public List<SubstituteSupplierResult> getSubstituteCandidates(final SupplierConfigurationTarget target) {
        final SupplierProfile supplier = this.supplierResolutionService.resolve(target);
        return this.substituteSupplierService.findCandidates(supplier.supplierId())
                .stream()
                .map(candidate -> new SubstituteSupplierResult(candidate.supplierId(), candidate.supplierCode(),
                        candidate.firstName(), candidate.lastName()))
                .toList();
    }

    @Override
    public SupplierConfigurationPolicy getPolicy() {
        return this.supplierConfigurationService.currentPolicy();
    }

    @Override
    @Transactional
    public SupplierConfigurationPolicy updatePolicy(final UpdateSupplierConfigurationPolicyCommand command) {
        final SupplierConfigurationPolicy policy = this.supplierConfigurationService
                .findPolicyForChange(command.expectedVersion());
        policy.changeEnforcedSettings(command.enforcedSettings(), this.currentUserServicePort.getCurrentUserId(),
                this.clock.instant());
        return this.supplierConfigurationService.storePolicy(policy);
    }

    @Override
    public Optional<SupplierConfigurationResult> findEffectiveBySupplierId(final SupplierId supplierId) {
        final SupplierConfigurationPolicy policy = this.supplierConfigurationService.currentPolicy();
        final Instant timestamp = this.clock.instant();
        return this.supplierDirectoryServicePort.findBySupplierId(supplierId)
                .map(supplier -> this.supplierConfigurationResultFactory.create(supplier,
                        this.supplierConfigurationService.findOrDefault(supplier.supplierId(), policy, timestamp),
                        policy));
    }

    @Override
    public Optional<SupplierConfigurationResult> findEffectiveByUserId(final UserId userId) {
        final SupplierConfigurationPolicy policy = this.supplierConfigurationService.currentPolicy();
        final Instant timestamp = this.clock.instant();
        return this.supplierDirectoryServicePort.findByUserId(userId)
                .map(supplier -> this.supplierConfigurationResultFactory.create(supplier,
                        this.supplierConfigurationService.findOrDefault(supplier.supplierId(), policy, timestamp),
                        policy));
    }

    @Override
    @Transactional
    public void releaseSubstitute(final SupplierId substituteSupplierId, final UserId changedBy) {
        this.supplierConfigurationService.releaseSubstitute(substituteSupplierId, changedBy, this.clock.instant());
    }

    @Override
    @Transactional
    public void removeConfiguration(final SupplierId supplierId, final UserId changedBy) {
        this.supplierConfigurationService.releaseSubstitute(supplierId, changedBy, this.clock.instant());
        this.supplierConfigurationService.remove(supplierId);
    }
}
