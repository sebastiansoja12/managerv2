package com.warehouse.supplierconfiguration.domain.model;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplierconfiguration.SupplierConfigurationFixture;
import com.warehouse.supplierconfiguration.domain.enumeration.LabelFormat;
import com.warehouse.supplierconfiguration.domain.enumeration.ShipmentSortOrder;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationViolationCode;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierSetting;
import com.warehouse.supplierconfiguration.domain.exception.InvalidSupplierConfigurationException;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationChange;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationValues;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationViolation;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SupplierConfigurationTest {

    private static final SupplierId SUPPLIER_ID = new SupplierId(11L);
    private static final UserId CHANGED_BY = new UserId(101L);
    private static final Instant CREATED_AT = Instant.parse("2026-10-22T08:00:00Z");
    private static final Instant CHANGED_AT = Instant.parse("2026-10-22T09:00:00Z");

    @Test
    void shouldCreateConfigurationWithDefaultValues() {
        final SupplierConfiguration configuration = SupplierConfiguration.createDefault(SUPPLIER_ID,
                SupplierConfigurationPolicy.empty(), CREATED_AT);

        final SupplierConfigurationValues values = configuration.getValues();
        assertThat(values.isEnabled(SupplierSetting.REQUIRE_PICKUP_SCAN)).isTrue();
        assertThat(values.isEnabled(SupplierSetting.REQUIRE_PROOF_OF_DELIVERY)).isTrue();
        assertThat(values.isEnabled(SupplierSetting.ALLOW_SHIPMENT_TRANSFER)).isFalse();
        assertThat(values.isEnabled(SupplierSetting.ALLOW_DELIVERY_REORDERING)).isTrue();
        assertThat(values.maxDeliveryAttempts()).isEqualTo(3);
        assertThat(values.maxShipmentsPerRoute()).isNull();
        assertThat(values.substituteSupplierId()).isNull();
        assertThat(values.preferredLabelFormat()).isEqualTo(LabelFormat.PDF_A6);
        assertThat(values.defaultShipmentSortOrder()).isEqualTo(ShipmentSortOrder.DELIVERY_ORDER);
        assertThat(configuration.getVersion()).isNull();
    }

    @Test
    void shouldReturnChangedFieldsWhenConfigurationChanges() {
        final SupplierConfiguration configuration = defaultConfiguration();
        final SupplierConfigurationValues requestedValues = SupplierConfigurationValues.defaults()
                .withSettings(Map.of(SupplierSetting.ALLOW_MANUAL_SHIPMENT_ADDITION, true));

        final List<SupplierConfigurationChange> changes = configuration.change(requestedValues,
                SupplierConfigurationPolicy.empty(), CHANGED_BY, CHANGED_AT);

        assertThat(changes).containsExactly(
                new SupplierConfigurationChange("ALLOW_MANUAL_SHIPMENT_ADDITION", "false", "true"));
        assertThat(configuration.getValues().isEnabled(SupplierSetting.ALLOW_MANUAL_SHIPMENT_ADDITION)).isTrue();
        assertThat(configuration.snapshot().updatedBy()).isEqualTo(CHANGED_BY);
        assertThat(configuration.snapshot().updatedAt()).isEqualTo(CHANGED_AT);
    }

    @Test
    void shouldReturnNoChangesWhenValuesAreTheSame() {
        final SupplierConfiguration configuration = defaultConfiguration();

        final List<SupplierConfigurationChange> changes = configuration.change(SupplierConfigurationValues.defaults(),
                SupplierConfigurationPolicy.empty(), CHANGED_BY, CHANGED_AT);

        assertThat(changes).isEmpty();
        assertThat(configuration.snapshot().updatedAt()).isEqualTo(CREATED_AT);
    }

    @Test
    void shouldRequireProofOfDeliveryWhenSignatureIsRequired() {
        final SupplierConfiguration configuration = defaultConfiguration();
        final SupplierConfigurationValues requestedValues = SupplierConfigurationValues.defaults()
                .withSettings(Map.of(
                        SupplierSetting.REQUIRE_RECIPIENT_SIGNATURE, true,
                        SupplierSetting.REQUIRE_PROOF_OF_DELIVERY, false));

        configuration.change(requestedValues, SupplierConfigurationPolicy.empty(), CHANGED_BY, CHANGED_AT);

        assertThat(configuration.getValues().isEnabled(SupplierSetting.REQUIRE_PROOF_OF_DELIVERY)).isTrue();
    }

    @Test
    void shouldRejectNonPositiveLimits() {
        final SupplierConfiguration configuration = defaultConfiguration();
        final SupplierConfigurationValues requestedValues = withParameters(0, 0, null);

        final InvalidSupplierConfigurationException exception = assertThrows(
                InvalidSupplierConfigurationException.class,
                () -> configuration.change(requestedValues, SupplierConfigurationPolicy.empty(), CHANGED_BY,
                        CHANGED_AT));

        assertThat(exception.getViolations()).containsExactlyInAnyOrder(
                new SupplierConfigurationViolation("MAX_DELIVERY_ATTEMPTS",
                        SupplierConfigurationViolationCode.MUST_BE_POSITIVE),
                new SupplierConfigurationViolation("MAX_SHIPMENTS_PER_ROUTE",
                        SupplierConfigurationViolationCode.MUST_BE_POSITIVE));
    }

    @Test
    void shouldAcceptMissingRouteLimit() {
        final SupplierConfiguration configuration = defaultConfiguration();

        configuration.change(withParameters(5, null, null), SupplierConfigurationPolicy.empty(), CHANGED_BY,
                CHANGED_AT);

        assertThat(configuration.getValues().maxDeliveryAttempts()).isEqualTo(5);
        assertThat(configuration.getValues().maxShipmentsPerRoute()).isNull();
    }

    @Test
    void shouldRejectSupplierAsOwnSubstitute() {
        final SupplierConfiguration configuration = defaultConfiguration();

        final InvalidSupplierConfigurationException exception = assertThrows(
                InvalidSupplierConfigurationException.class,
                () -> configuration.change(withParameters(3, null, SUPPLIER_ID), SupplierConfigurationPolicy.empty(),
                        CHANGED_BY, CHANGED_AT));

        assertThat(exception.getViolations()).containsExactly(new SupplierConfigurationViolation(
                "SUBSTITUTE_SUPPLIER_ID", SupplierConfigurationViolationCode.SUBSTITUTE_SELF));
    }

    @Test
    void shouldRejectDisablingSettingEnforcedByPolicy() {
        final SupplierConfiguration configuration = defaultConfiguration();
        final SupplierConfigurationPolicy policy = SupplierConfigurationFixture.policy(
                Map.of(SupplierSetting.REQUIRE_DELIVERY_SCAN, true));
        final SupplierConfigurationValues requestedValues = SupplierConfigurationValues.defaults()
                .withSettings(Map.of(SupplierSetting.REQUIRE_DELIVERY_SCAN, false));

        final InvalidSupplierConfigurationException exception = assertThrows(
                InvalidSupplierConfigurationException.class,
                () -> configuration.change(requestedValues, policy, CHANGED_BY, CHANGED_AT));

        assertThat(exception.getViolations()).containsExactly(new SupplierConfigurationViolation(
                "REQUIRE_DELIVERY_SCAN", SupplierConfigurationViolationCode.ENFORCED_BY_POLICY));
    }

    @Test
    void shouldExposePolicyValuesAsEffectiveValues() {
        final SupplierConfiguration configuration = defaultConfiguration();
        final SupplierConfigurationPolicy policy = SupplierConfigurationFixture.policy(
                Map.of(SupplierSetting.REQUIRE_RECIPIENT_SIGNATURE, true));

        final SupplierConfigurationValues effectiveValues = configuration.effectiveValues(policy);

        assertThat(effectiveValues.isEnabled(SupplierSetting.REQUIRE_RECIPIENT_SIGNATURE)).isTrue();
        assertThat(effectiveValues.isEnabled(SupplierSetting.REQUIRE_PROOF_OF_DELIVERY)).isTrue();
        assertThat(configuration.getValues().isEnabled(SupplierSetting.REQUIRE_RECIPIENT_SIGNATURE)).isFalse();
    }

    @Test
    void shouldResetToDefaultsAllowedByPolicy() {
        final SupplierConfiguration configuration = defaultConfiguration();
        configuration.change(SupplierConfigurationValues.defaults()
                        .withSettings(Map.of(SupplierSetting.AUTO_GENERATE_LABELS, true)),
                SupplierConfigurationPolicy.empty(), CHANGED_BY, CHANGED_AT);
        final SupplierConfigurationPolicy policy = SupplierConfigurationFixture.policy(
                Map.of(SupplierSetting.ALLOW_DELIVERY_REORDERING, false));

        final List<SupplierConfigurationChange> changes = configuration.resetToDefaults(policy, CHANGED_BY,
                CHANGED_AT);

        assertThat(changes).containsExactlyInAnyOrder(
                new SupplierConfigurationChange("AUTO_GENERATE_LABELS", "true", "false"),
                new SupplierConfigurationChange("ALLOW_DELIVERY_REORDERING", "true", "false"));
    }

    @Test
    void shouldRejectPolicyDisablingProofOfDeliveryWhileRequiringSignature() {
        final SupplierConfigurationPolicy policy = SupplierConfigurationPolicy.empty();

        final InvalidSupplierConfigurationException exception = assertThrows(
                InvalidSupplierConfigurationException.class,
                () -> policy.changeEnforcedSettings(Map.of(
                        SupplierSetting.REQUIRE_PROOF_OF_DELIVERY, false,
                        SupplierSetting.REQUIRE_RECIPIENT_SIGNATURE, true), CHANGED_BY, CHANGED_AT));

        assertThat(exception.getViolations()).hasSize(1);
    }

    private SupplierConfiguration defaultConfiguration() {
        return SupplierConfiguration.createDefault(SUPPLIER_ID, SupplierConfigurationPolicy.empty(), CREATED_AT);
    }

    private SupplierConfigurationValues withParameters(final Integer maxDeliveryAttempts,
                                                       final Integer maxShipmentsPerRoute,
                                                       final SupplierId substituteSupplierId) {
        final SupplierConfigurationValues defaults = SupplierConfigurationValues.defaults();
        return new SupplierConfigurationValues(defaults.settings(), maxDeliveryAttempts, maxShipmentsPerRoute,
                substituteSupplierId, defaults.preferredLabelFormat(), defaults.defaultShipmentSortOrder());
    }
}
