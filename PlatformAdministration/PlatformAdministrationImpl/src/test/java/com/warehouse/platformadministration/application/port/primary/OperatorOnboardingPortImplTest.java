package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.secondary.OperatorCourierServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorDepartmentServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorGeocodingServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorShipmentServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorUserServicePort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.model.OperatorOnboarding;
import com.warehouse.platformadministration.domain.vo.OnboardingStep;
import com.warehouse.platformadministration.domain.vo.OnboardingStepType;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.domain.vo.OperatorUser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OperatorOnboardingPortImplTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    private static final OperatorScope SCOPE = new OperatorScope(OPERATOR_ID, new UserId(1L));

    private final OperatorServicePort operatorServicePort = mock(OperatorServicePort.class);

    private final OperatorUserServicePort operatorUserServicePort = mock(OperatorUserServicePort.class);

    private final OperatorDepartmentServicePort operatorDepartmentServicePort = mock(OperatorDepartmentServicePort.class);

    private final OperatorCourierServicePort operatorCourierServicePort = mock(OperatorCourierServicePort.class);

    private final OperatorGeocodingServicePort operatorGeocodingServicePort = mock(OperatorGeocodingServicePort.class);

    private final OperatorShipmentServicePort operatorShipmentServicePort = mock(OperatorShipmentServicePort.class);

    private final OperatorOnboardingPort port = new OperatorOnboardingPortImpl(operatorServicePort,
            operatorUserServicePort, operatorDepartmentServicePort, operatorCourierServicePort,
            operatorGeocodingServicePort, operatorShipmentServicePort);

    @Test
    void shouldReportMissingOnboardingSteps() {
        when(operatorServicePort.findScope(OPERATOR_ID)).thenReturn(Optional.of(SCOPE));
        when(operatorUserServicePort.findAll(SCOPE)).thenReturn(List.of(
                user(1L, "SUPERADMIN", true), user(2L, "ADMIN", false)));
        when(operatorDepartmentServicePort.findAll(SCOPE)).thenReturn(List.of());
        when(operatorCourierServicePort.findAll(SCOPE)).thenReturn(List.of());
        when(operatorGeocodingServicePort.hasEnabledConfiguration(SCOPE)).thenReturn(true);
        when(operatorShipmentServicePort.hasAnyShipment(SCOPE)).thenReturn(false);

        final OperatorOnboarding onboarding = port.find(OPERATOR_ID);

        assertThat(onboarding.steps()).containsExactly(
                new OnboardingStep(OnboardingStepType.DEPARTMENT, false, 0),
                new OnboardingStep(OnboardingStepType.SUPERADMIN, false, 0),
                new OnboardingStep(OnboardingStepType.GEOCODING, true, 1),
                new OnboardingStep(OnboardingStepType.COURIER, false, 0),
                new OnboardingStep(OnboardingStepType.TEST_SHIPMENT, false, 0));
        assertThat(onboarding.completedSteps()).isEqualTo(1);
        assertThat(onboarding.complete()).isFalse();
    }

    @Test
    void shouldRejectUnknownOperator() {
        when(operatorServicePort.findScope(OPERATOR_ID)).thenReturn(Optional.empty());

        assertThrows(OperatorNotFoundException.class, () -> port.find(OPERATOR_ID));
    }

    private OperatorUser user(final Long id, final String role, final boolean deleted) {
        return new OperatorUser(new UserId(id), "user" + id, "First", "Last", "user@test.pl", role,
                new DepartmentCode("KT1"), deleted);
    }
}
