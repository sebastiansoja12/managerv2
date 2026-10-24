package com.warehouse.supplierconfiguration.domain.model;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationViolationCode;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierSetting;
import com.warehouse.supplierconfiguration.domain.exception.InvalidSupplierConfigurationException;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationPolicyId;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationValues;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationViolation;

import java.time.Instant;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SupplierConfigurationPolicy {

    private final SupplierConfigurationPolicyId policyId;
    private Map<SupplierSetting, Boolean> enforcedSettings;
    private final Long version;
    private Instant updatedAt;
    private UserId updatedBy;

    public SupplierConfigurationPolicy(final SupplierConfigurationPolicyId policyId,
                                       final Map<SupplierSetting, Boolean> enforcedSettings,
                                       final Long version,
                                       final Instant updatedAt,
                                       final UserId updatedBy) {
        this.policyId = policyId;
        this.enforcedSettings = copy(enforcedSettings);
        this.version = version;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public static SupplierConfigurationPolicy empty() {
        return new SupplierConfigurationPolicy(SupplierConfigurationPolicyId.generate(), Map.of(), null, null, null);
    }

    public void changeEnforcedSettings(final Map<SupplierSetting, Boolean> enforcedSettings,
                                       final UserId changedBy,
                                       final Instant timestamp) {
        validateConsistency(enforcedSettings);
        this.enforcedSettings = copy(enforcedSettings);
        this.updatedBy = changedBy;
        this.updatedAt = timestamp;
    }

    public Optional<Boolean> enforcedValue(final SupplierSetting setting) {
        return Optional.ofNullable(enforcedSettings.get(setting));
    }

    public SupplierConfigurationValues applyTo(final SupplierConfigurationValues values) {
        return values.withSettings(enforcedSettings);
    }

    public List<SupplierConfigurationViolation> findViolations(final SupplierConfigurationValues values) {
        return enforcedSettings.entrySet()
                .stream()
                .filter(entry -> values.isEnabled(entry.getKey()) != entry.getValue())
                .map(entry -> new SupplierConfigurationViolation(entry.getKey().name(),
                        SupplierConfigurationViolationCode.ENFORCED_BY_POLICY))
                .toList();
    }

    private void validateConsistency(final Map<SupplierSetting, Boolean> requestedSettings) {
        final boolean proofOfDeliveryDisabled = Boolean.FALSE.equals(
                requestedSettings.get(SupplierSetting.REQUIRE_PROOF_OF_DELIVERY));
        final boolean confirmationRequired = Boolean.TRUE.equals(
                requestedSettings.get(SupplierSetting.REQUIRE_RECIPIENT_SIGNATURE))
                || Boolean.TRUE.equals(requestedSettings.get(SupplierSetting.REQUIRE_DELIVERY_PHOTO));
        if (proofOfDeliveryDisabled && confirmationRequired) {
            throw new InvalidSupplierConfigurationException(List.of(new SupplierConfigurationViolation(
                    SupplierSetting.REQUIRE_PROOF_OF_DELIVERY.name(),
                    SupplierConfigurationViolationCode.ENFORCED_BY_POLICY)));
        }
    }

    private static Map<SupplierSetting, Boolean> copy(final Map<SupplierSetting, Boolean> settings) {
        final EnumMap<SupplierSetting, Boolean> copiedSettings = new EnumMap<>(SupplierSetting.class);
        copiedSettings.putAll(settings);
        return Collections.unmodifiableMap(copiedSettings);
    }

    public SupplierConfigurationPolicyId getPolicyId() {
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
