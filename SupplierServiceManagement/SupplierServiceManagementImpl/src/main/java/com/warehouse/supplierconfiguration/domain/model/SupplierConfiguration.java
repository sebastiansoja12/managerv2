package com.warehouse.supplierconfiguration.domain.model;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationParameter;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationViolationCode;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierSetting;
import com.warehouse.supplierconfiguration.domain.exception.InvalidSupplierConfigurationException;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationChange;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationSnapshot;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationValues;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationViolation;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SupplierConfiguration {

    private final SupplierId supplierId;
    private SupplierConfigurationValues values;
    private final Long version;
    private final Instant createdAt;
    private Instant updatedAt;
    private UserId updatedBy;

    private SupplierConfiguration(final SupplierId supplierId,
                                  final SupplierConfigurationValues values,
                                  final Long version,
                                  final Instant createdAt,
                                  final Instant updatedAt,
                                  final UserId updatedBy) {
        this.supplierId = supplierId;
        this.values = values;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public static SupplierConfiguration createDefault(final SupplierId supplierId,
                                                      final SupplierConfigurationPolicy policy,
                                                      final Instant timestamp) {
        final SupplierConfigurationValues values = normalize(policy.applyTo(SupplierConfigurationValues.defaults()));
        return new SupplierConfiguration(supplierId, values, null, timestamp, timestamp, null);
    }

    public static SupplierConfiguration restore(final SupplierConfigurationSnapshot snapshot) {
        return new SupplierConfiguration(snapshot.supplierId(), snapshot.values(), snapshot.version(),
                snapshot.createdAt(), snapshot.updatedAt(), snapshot.updatedBy());
    }

    public List<SupplierConfigurationChange> change(final SupplierConfigurationValues requestedValues,
                                                    final SupplierConfigurationPolicy policy,
                                                    final UserId changedBy,
                                                    final Instant timestamp) {
        final SupplierConfigurationValues mergedValues = new SupplierConfigurationValues(
                policy.applyTo(values).withSettings(requestedValues.settings()).settings(),
                requestedValues.maxDeliveryAttempts(),
                requestedValues.maxShipmentsPerRoute(),
                requestedValues.substituteSupplierId(),
                requestedValues.preferredLabelFormat(),
                requestedValues.defaultShipmentSortOrder());
        final SupplierConfigurationValues normalizedValues = normalize(mergedValues);
        validate(normalizedValues, policy);
        return apply(normalizedValues, changedBy, timestamp);
    }

    public List<SupplierConfigurationChange> resetToDefaults(final SupplierConfigurationPolicy policy,
                                                             final UserId changedBy,
                                                             final Instant timestamp) {
        return apply(normalize(policy.applyTo(SupplierConfigurationValues.defaults())), changedBy, timestamp);
    }

    public SupplierConfigurationValues effectiveValues(final SupplierConfigurationPolicy policy) {
        return normalize(policy.applyTo(values));
    }

    public SupplierConfigurationSnapshot snapshot() {
        return new SupplierConfigurationSnapshot(supplierId, values, version, createdAt, updatedAt, updatedBy);
    }

    public SupplierId getSupplierId() {
        return supplierId;
    }

    public SupplierConfigurationValues getValues() {
        return values;
    }

    public Long getVersion() {
        return version;
    }

    private static SupplierConfigurationValues normalize(final SupplierConfigurationValues candidate) {
        if (candidate.isEnabled(SupplierSetting.REQUIRE_RECIPIENT_SIGNATURE)
                || candidate.isEnabled(SupplierSetting.REQUIRE_DELIVERY_PHOTO)) {
            return candidate.withSettings(Map.of(SupplierSetting.REQUIRE_PROOF_OF_DELIVERY, true));
        }
        return candidate;
    }

    private void validate(final SupplierConfigurationValues candidate, final SupplierConfigurationPolicy policy) {
        final List<SupplierConfigurationViolation> violations = new ArrayList<>();
        Arrays.stream(SupplierSetting.values())
                .filter(setting -> candidate.settings().get(setting) == null)
                .forEach(setting -> violations.add(new SupplierConfigurationViolation(setting.name(),
                        SupplierConfigurationViolationCode.REQUIRED)));
        if (candidate.maxDeliveryAttempts() == null) {
            violations.add(violation(SupplierConfigurationParameter.MAX_DELIVERY_ATTEMPTS,
                    SupplierConfigurationViolationCode.REQUIRED));
        } else if (candidate.maxDeliveryAttempts() <= 0) {
            violations.add(violation(SupplierConfigurationParameter.MAX_DELIVERY_ATTEMPTS,
                    SupplierConfigurationViolationCode.MUST_BE_POSITIVE));
        }
        if (candidate.maxShipmentsPerRoute() != null && candidate.maxShipmentsPerRoute() <= 0) {
            violations.add(violation(SupplierConfigurationParameter.MAX_SHIPMENTS_PER_ROUTE,
                    SupplierConfigurationViolationCode.MUST_BE_POSITIVE));
        }
        if (supplierId.equals(candidate.substituteSupplierId())) {
            violations.add(violation(SupplierConfigurationParameter.SUBSTITUTE_SUPPLIER_ID,
                    SupplierConfigurationViolationCode.SUBSTITUTE_SELF));
        }
        if (candidate.preferredLabelFormat() == null) {
            violations.add(violation(SupplierConfigurationParameter.PREFERRED_LABEL_FORMAT,
                    SupplierConfigurationViolationCode.REQUIRED));
        }
        if (candidate.defaultShipmentSortOrder() == null) {
            violations.add(violation(SupplierConfigurationParameter.DEFAULT_SHIPMENT_SORT_ORDER,
                    SupplierConfigurationViolationCode.REQUIRED));
        }
        violations.addAll(policy.findViolations(candidate));
        if (!violations.isEmpty()) {
            throw new InvalidSupplierConfigurationException(violations);
        }
    }

    private List<SupplierConfigurationChange> apply(final SupplierConfigurationValues nextValues,
                                                    final UserId changedBy,
                                                    final Instant timestamp) {
        final List<SupplierConfigurationChange> changes = differences(values, nextValues);
        if (!changes.isEmpty()) {
            this.values = nextValues;
            this.updatedBy = changedBy;
            this.updatedAt = timestamp;
        }
        return changes;
    }

    private static List<SupplierConfigurationChange> differences(final SupplierConfigurationValues previous,
                                                                 final SupplierConfigurationValues next) {
        final List<SupplierConfigurationChange> changes = new ArrayList<>();
        Arrays.stream(SupplierSetting.values())
                .forEach(setting -> addChange(changes, setting.name(),
                        previous.settings().get(setting), next.settings().get(setting)));
        addChange(changes, SupplierConfigurationParameter.MAX_DELIVERY_ATTEMPTS.name(),
                previous.maxDeliveryAttempts(), next.maxDeliveryAttempts());
        addChange(changes, SupplierConfigurationParameter.MAX_SHIPMENTS_PER_ROUTE.name(),
                previous.maxShipmentsPerRoute(), next.maxShipmentsPerRoute());
        addChange(changes, SupplierConfigurationParameter.SUBSTITUTE_SUPPLIER_ID.name(),
                previous.substituteSupplierId() != null ? previous.substituteSupplierId().value() : null,
                next.substituteSupplierId() != null ? next.substituteSupplierId().value() : null);
        addChange(changes, SupplierConfigurationParameter.PREFERRED_LABEL_FORMAT.name(),
                previous.preferredLabelFormat(), next.preferredLabelFormat());
        addChange(changes, SupplierConfigurationParameter.DEFAULT_SHIPMENT_SORT_ORDER.name(),
                previous.defaultShipmentSortOrder(), next.defaultShipmentSortOrder());
        return changes;
    }

    private static void addChange(final List<SupplierConfigurationChange> changes,
                                  final String field,
                                  final Object previousValue,
                                  final Object newValue) {
        if (!Objects.equals(previousValue, newValue)) {
            changes.add(new SupplierConfigurationChange(field,
                    previousValue != null ? previousValue.toString() : null,
                    newValue != null ? newValue.toString() : null));
        }
    }

    private static SupplierConfigurationViolation violation(final SupplierConfigurationParameter parameter,
                                                            final SupplierConfigurationViolationCode code) {
        return new SupplierConfigurationViolation(parameter.name(), code);
    }
}
