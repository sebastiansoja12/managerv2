package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
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

import java.util.List;

public class OperatorOnboardingPortImpl implements OperatorOnboardingPort {

    private static final String SUPERADMIN_ROLE = "SUPERADMIN";

    private final OperatorServicePort operatorServicePort;

    private final OperatorUserServicePort operatorUserServicePort;

    private final OperatorDepartmentServicePort operatorDepartmentServicePort;

    private final OperatorCourierServicePort operatorCourierServicePort;

    private final OperatorGeocodingServicePort operatorGeocodingServicePort;

    private final OperatorShipmentServicePort operatorShipmentServicePort;

    public OperatorOnboardingPortImpl(final OperatorServicePort operatorServicePort,
                                      final OperatorUserServicePort operatorUserServicePort,
                                      final OperatorDepartmentServicePort operatorDepartmentServicePort,
                                      final OperatorCourierServicePort operatorCourierServicePort,
                                      final OperatorGeocodingServicePort operatorGeocodingServicePort,
                                      final OperatorShipmentServicePort operatorShipmentServicePort) {
        this.operatorServicePort = operatorServicePort;
        this.operatorUserServicePort = operatorUserServicePort;
        this.operatorDepartmentServicePort = operatorDepartmentServicePort;
        this.operatorCourierServicePort = operatorCourierServicePort;
        this.operatorGeocodingServicePort = operatorGeocodingServicePort;
        this.operatorShipmentServicePort = operatorShipmentServicePort;
    }

    @Override
    public OperatorOnboarding find(final OperatorId operatorId) {
        final OperatorScope scope = operatorServicePort.findScope(operatorId)
                .orElseThrow(() -> new OperatorNotFoundException(operatorId));
        final long superadmins = operatorUserServicePort.findAll(scope)
                .stream()
                .filter(user -> SUPERADMIN_ROLE.equals(user.role()) && !user.deleted())
                .count();
        return new OperatorOnboarding(operatorId, List.of(
                OnboardingStep.counted(OnboardingStepType.DEPARTMENT, operatorDepartmentServicePort.findAll(scope).size()),
                OnboardingStep.counted(OnboardingStepType.SUPERADMIN, superadmins),
                OnboardingStep.flag(OnboardingStepType.GEOCODING, operatorGeocodingServicePort.hasEnabledConfiguration(scope)),
                OnboardingStep.counted(OnboardingStepType.COURIER, operatorCourierServicePort.findAll(scope).size()),
                OnboardingStep.flag(OnboardingStepType.TEST_SHIPMENT, operatorShipmentServicePort.hasAnyShipment(scope))));
    }
}
