package com.warehouse.supplierconfiguration.application.port.primary;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.supplierconfiguration.SupplierConfigurationFixture;
import com.warehouse.supplierconfiguration.application.exception.SupplierConfigurationAccessDeniedException;
import com.warehouse.supplierconfiguration.application.exception.SupplierConfigurationConflictException;
import com.warehouse.supplierconfiguration.application.exception.SupplierConfigurationNotFoundException;
import com.warehouse.supplierconfiguration.application.port.primary.command.ResetSupplierConfigurationCommand;
import com.warehouse.supplierconfiguration.application.port.primary.command.SupplierConfigurationTarget;
import com.warehouse.supplierconfiguration.application.port.primary.command.UpdateSupplierConfigurationCommand;
import com.warehouse.supplierconfiguration.application.port.primary.result.SubstituteSupplierResult;
import com.warehouse.supplierconfiguration.application.port.primary.result.SupplierConfigurationResult;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationChangeType;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationViolationCode;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierSetting;
import com.warehouse.supplierconfiguration.domain.exception.InvalidSupplierConfigurationException;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationRevision;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationValues;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SupplierConfigurationPortImplTest {

    private final SupplierConfigurationFixture.InMemorySupplierConfigurationRepository configurationRepository =
            new SupplierConfigurationFixture.InMemorySupplierConfigurationRepository();
    private final SupplierConfigurationFixture.InMemorySupplierConfigurationPolicyRepository policyRepository =
            new SupplierConfigurationFixture.InMemorySupplierConfigurationPolicyRepository();
    private final SupplierConfigurationFixture.InMemorySupplierConfigurationHistoryRepository historyRepository =
            new SupplierConfigurationFixture.InMemorySupplierConfigurationHistoryRepository();
    private final SupplierConfigurationFixture.FakeCurrentUser currentUser =
            new SupplierConfigurationFixture.FakeCurrentUser();

    private final SupplierConfigurationPortImpl port = new SupplierConfigurationPortImpl(
            configurationRepository,
            policyRepository,
            historyRepository,
            new SupplierConfigurationFixture.InMemorySupplierDirectory(),
            currentUser,
            Clock.fixed(Instant.parse("2026-10-22T10:00:00Z"), ZoneOffset.UTC));

    @Test
    void shouldReturnCurrentSupplierConfiguration() {
        final SupplierConfigurationResult result = port.get(SupplierConfigurationTarget.currentSupplier());

        assertThat(result.supplierId()).isEqualTo(SupplierConfigurationFixture.FIRST_SUPPLIER.supplierId());
        assertThat(result.values()).isEqualTo(SupplierConfigurationValues.defaults());
        assertThat(result.version()).isEqualTo(0L);
    }

    @Test
    void shouldThrowExceptionWhenCurrentUserIsNotSupplier() {
        currentUser.signInAs(SupplierConfigurationFixture.ADMIN_USER);

        assertThatThrownBy(() -> port.get(SupplierConfigurationTarget.currentSupplier()))
                .isInstanceOf(SupplierConfigurationNotFoundException.class);
    }

    @Test
    void shouldSaveCurrentSupplierChangesAndRecordHistory() {
        final SupplierConfigurationResult current = port.get(SupplierConfigurationTarget.currentSupplier());

        final SupplierConfigurationResult updated = port.update(new UpdateSupplierConfigurationCommand(
                SupplierConfigurationTarget.currentSupplier(), current.version(),
                current.values().withSettings(Map.of(SupplierSetting.REQUIRE_RECIPIENT_SIGNATURE, true)), null));

        assertThat(updated.values().isEnabled(SupplierSetting.REQUIRE_RECIPIENT_SIGNATURE)).isTrue();
        assertThat(updated.version()).isEqualTo(1L);
        final List<SupplierConfigurationRevision> history = port.getHistory(
                SupplierConfigurationTarget.currentSupplier());
        assertThat(history).hasSize(1);
        assertThat(history.getFirst().changedBy()).isEqualTo(SupplierConfigurationFixture.FIRST_SUPPLIER_USER);
        assertThat(history.getFirst().changes()).extracting("field")
                .containsExactly("REQUIRE_RECIPIENT_SIGNATURE");
    }

    @Test
    void shouldKeepOtherSupplierConfigurationUnchanged() {
        final SupplierConfigurationResult first = port.get(SupplierConfigurationTarget.currentSupplier());
        port.update(new UpdateSupplierConfigurationCommand(SupplierConfigurationTarget.currentSupplier(),
                first.version(), first.values().withSettings(Map.of(SupplierSetting.AUTO_GENERATE_LABELS, true)), null));

        currentUser.signInAs(SupplierConfigurationFixture.SECOND_SUPPLIER_USER);
        final SupplierConfigurationResult second = port.get(SupplierConfigurationTarget.currentSupplier());

        assertThat(second.supplierId()).isEqualTo(SupplierConfigurationFixture.SECOND_SUPPLIER.supplierId());
        assertThat(second.values().isEnabled(SupplierSetting.AUTO_GENERATE_LABELS)).isFalse();
    }

    @Test
    void shouldRejectStaleVersion() {
        final SupplierConfigurationResult current = port.get(SupplierConfigurationTarget.currentSupplier());
        port.update(new UpdateSupplierConfigurationCommand(SupplierConfigurationTarget.currentSupplier(),
                current.version(), current.values().withSettings(Map.of(SupplierSetting.AUTO_MARK_FOR_RETURN, true)),
                null));

        assertThatThrownBy(() -> port.update(new UpdateSupplierConfigurationCommand(
                SupplierConfigurationTarget.currentSupplier(), current.version(),
                current.values().withSettings(Map.of(SupplierSetting.SKIP_INVALID_SHIPMENTS, true)), null)))
                .isInstanceOf(SupplierConfigurationConflictException.class);
    }

    @Test
    void shouldAllowAdministratorToUpdateSupplierInScope() {
        currentUser.signInAs(SupplierConfigurationFixture.ADMIN_USER);
        final SupplierConfigurationTarget target = SupplierConfigurationTarget.managedSupplier(
                SupplierConfigurationFixture.FIRST_SUPPLIER.supplierCode());
        final SupplierConfigurationResult current = port.get(target);

        port.update(new UpdateSupplierConfigurationCommand(target, current.version(),
                current.values().withSettings(Map.of(SupplierSetting.ALLOW_SHIPMENT_TRANSFER, true)), null));

        currentUser.signInAs(SupplierConfigurationFixture.FIRST_SUPPLIER_USER);
        final SupplierConfigurationResult visibleToSupplier = port.get(SupplierConfigurationTarget.currentSupplier());
        assertThat(visibleToSupplier.values().isEnabled(SupplierSetting.ALLOW_SHIPMENT_TRANSFER)).isTrue();
        assertThat(port.getHistory(SupplierConfigurationTarget.currentSupplier()).getFirst().changedBy())
                .isEqualTo(SupplierConfigurationFixture.ADMIN_USER);
    }

    @Test
    void shouldRejectSupplierOutsideOrganizationalScope() {
        currentUser.signInAs(SupplierConfigurationFixture.ADMIN_USER);

        assertThatThrownBy(() -> port.get(SupplierConfigurationTarget.managedSupplier(
                SupplierConfigurationFixture.OTHER_DEPARTMENT_SUPPLIER.supplierCode())))
                .isInstanceOf(SupplierConfigurationAccessDeniedException.class);
    }

    @Test
    void shouldRejectInactiveSubstitute() {
        final SupplierConfigurationResult current = port.get(SupplierConfigurationTarget.currentSupplier());
        final InvalidSupplierConfigurationException exception = assertThrows(
                InvalidSupplierConfigurationException.class,
                () -> port.update(new UpdateSupplierConfigurationCommand(SupplierConfigurationTarget.currentSupplier(),
                        current.version(), current.values(),
                        SupplierConfigurationFixture.INACTIVE_SUPPLIER.supplierCode())));

        assertThat(exception.getViolations()).extracting("code")
                .containsExactly(SupplierConfigurationViolationCode.SUBSTITUTE_UNAVAILABLE);
    }

    @Test
    void shouldSaveActiveSubstitute() {
        final SupplierConfigurationResult current = port.get(SupplierConfigurationTarget.currentSupplier());

        final SupplierConfigurationResult updated = port.update(new UpdateSupplierConfigurationCommand(
                SupplierConfigurationTarget.currentSupplier(), current.version(),
                current.values(), SupplierConfigurationFixture.SECOND_SUPPLIER.supplierCode()));

        assertThat(updated.values().substituteSupplierId())
                .isEqualTo(SupplierConfigurationFixture.SECOND_SUPPLIER.supplierId());
        assertThat(updated.substituteSupplierCode())
                .isEqualTo(SupplierConfigurationFixture.SECOND_SUPPLIER.supplierCode());
    }

    @Test
    void shouldRejectUnknownSubstituteCode() {
        final SupplierConfigurationResult current = port.get(SupplierConfigurationTarget.currentSupplier());

        final InvalidSupplierConfigurationException exception = assertThrows(
                InvalidSupplierConfigurationException.class,
                () -> port.update(new UpdateSupplierConfigurationCommand(SupplierConfigurationTarget.currentSupplier(),
                        current.version(), current.values(), new SupplierCode("UNKNOWN"))));

        assertThat(exception.getViolations()).extracting("code")
                .containsExactly(SupplierConfigurationViolationCode.SUBSTITUTE_UNAVAILABLE);
    }

    @Test
    void shouldRejectCurrentSupplierAsSubstitute() {
        final SupplierConfigurationResult current = port.get(SupplierConfigurationTarget.currentSupplier());

        final InvalidSupplierConfigurationException exception = assertThrows(
                InvalidSupplierConfigurationException.class,
                () -> port.update(new UpdateSupplierConfigurationCommand(SupplierConfigurationTarget.currentSupplier(),
                        current.version(), current.values(), SupplierConfigurationFixture.FIRST_SUPPLIER.supplierCode())));

        assertThat(exception.getViolations()).extracting("code")
                .containsExactly(SupplierConfigurationViolationCode.SUBSTITUTE_SELF);
    }

    @Test
    void shouldListActiveSubstituteCandidatesWithoutCurrentSupplier() {
        final List<SubstituteSupplierResult> candidates = port.getSubstituteCandidates(
                SupplierConfigurationTarget.currentSupplier());

        assertThat(candidates).extracting(SubstituteSupplierResult::supplierId).containsExactlyInAnyOrder(
                SupplierConfigurationFixture.SECOND_SUPPLIER.supplierId(),
                SupplierConfigurationFixture.OTHER_DEPARTMENT_SUPPLIER.supplierId());
    }

    @Test
    void shouldExposePolicyEnforcedSettings() {
        policyRepository.store(SupplierConfigurationFixture.policy(Map.of(SupplierSetting.REQUIRE_DELIVERY_SCAN, true)));

        final SupplierConfigurationResult result = port.get(SupplierConfigurationTarget.currentSupplier());

        assertThat(result.enforcedSettings()).containsEntry(SupplierSetting.REQUIRE_DELIVERY_SCAN, true);
        assertThat(result.values().isEnabled(SupplierSetting.REQUIRE_DELIVERY_SCAN)).isTrue();
    }

    @Test
    void shouldResetToDefaultsAndRecordHistory() {
        final SupplierConfigurationResult current = port.get(SupplierConfigurationTarget.currentSupplier());
        final SupplierConfigurationResult updated = port.update(new UpdateSupplierConfigurationCommand(
                SupplierConfigurationTarget.currentSupplier(), current.version(),
                current.values().withSettings(Map.of(SupplierSetting.NOTIFY_ABOUT_NEW_SHIPMENTS, false)), null));

        final SupplierConfigurationResult reset = port.resetToDefaults(new ResetSupplierConfigurationCommand(
                SupplierConfigurationTarget.currentSupplier(), updated.version()));

        assertThat(reset.values()).isEqualTo(SupplierConfigurationValues.defaults());
        assertThat(port.getHistory(SupplierConfigurationTarget.currentSupplier()))
                .extracting(SupplierConfigurationRevision::changeType)
                .contains(SupplierConfigurationChangeType.RESET_TO_DEFAULTS);
    }

    @Test
    void shouldReturnEffectiveConfigurationForProcesses() {
        policyRepository.store(SupplierConfigurationFixture.policy(
                Map.of(SupplierSetting.REQUIRE_RECIPIENT_SIGNATURE, true)));

        final SupplierConfigurationResult result = port
                .findEffectiveByUserId(SupplierConfigurationFixture.SECOND_SUPPLIER_USER)
                .orElseThrow();

        assertThat(result.values().isEnabled(SupplierSetting.REQUIRE_RECIPIENT_SIGNATURE)).isTrue();
        assertThat(configurationRepository.findBySupplierId(SupplierConfigurationFixture.SECOND_SUPPLIER.supplierId()))
                .isEmpty();
    }
}
