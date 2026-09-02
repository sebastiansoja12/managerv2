package com.warehouse.platformadministration.infrastructure.adapter.primary.mapper;

import com.warehouse.platformadministration.domain.model.OperatorDataErasure;
import com.warehouse.platformadministration.domain.model.OperatorDataExport;
import com.warehouse.platformadministration.domain.model.OperatorOnboarding;
import com.warehouse.platformadministration.domain.vo.OperatorApiKey;
import com.warehouse.platformadministration.domain.vo.OperatorDevice;
import com.warehouse.platformadministration.domain.vo.OperatorProfile;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorApiKeyResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorDataErasureResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorDataExportResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorDeviceResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.OperatorOnboardingResponse;

public final class OperatorOperationsResponseMapper {

    private OperatorOperationsResponseMapper() {
    }

    public static OperatorOnboardingResponse toResponse(final OperatorOnboarding onboarding) {
        return new OperatorOnboardingResponse(onboarding.operatorId().value(), onboarding.completedSteps(),
                onboarding.steps().size(), onboarding.complete(), onboarding.steps()
                .stream()
                .map(step -> new OperatorOnboardingResponse.Step(step.type().name(), step.completed(), step.count()))
                .toList());
    }

    public static OperatorDeviceResponse toResponse(final OperatorDevice device) {
        return new OperatorDeviceResponse(device.deviceId(), device.deviceType(), device.status(),
                device.userId() != null ? device.userId().value() : null,
                device.departmentCode() != null ? device.departmentCode().getValue() : null, device.version(),
                device.lastUpdate(), device.paired(), device.pairKeyExpiresAt(), device.lastLoginAt());
    }

    public static OperatorApiKeyResponse toResponse(final OperatorApiKey apiKey) {
        return new OperatorApiKeyResponse(apiKey.userId().value(), apiKey.username(), apiKey.firstName(),
                apiKey.lastName(), apiKey.role(),
                apiKey.departmentCode() != null ? apiKey.departmentCode().getValue() : null);
    }

    public static OperatorDataErasureResponse toResponse(final OperatorDataErasure erasure) {
        return new OperatorDataErasureResponse(erasure.operatorId().value(), erasure.requestedBy(),
                erasure.executedAt(), erasure.usersAnonymized(), erasure.couriersAnonymized(), erasure.devicesRetired());
    }

    public static OperatorDataExportResponse toResponse(final OperatorDataExport export) {
        return new OperatorDataExportResponse(
                OperatorDataExportResponse.FORMAT,
                export.exportedAt(),
                export.exportedBy(),
                toOperator(export.operator()),
                export.users().stream().map(OperatorDirectoryResponseMapper::toResponse).toList(),
                export.departments().stream().map(OperatorDirectoryResponseMapper::toResponse).toList(),
                export.couriers().stream().map(OperatorDirectoryResponseMapper::toResponse).toList(),
                export.devices().stream().map(OperatorOperationsResponseMapper::toResponse).toList(),
                export.apiKeys().stream().map(OperatorOperationsResponseMapper::toResponse).toList(),
                export.shipmentSettings().values(),
                export.auditLog().stream().map(PlatformAdministrationResponseMapper::toResponse).toList());
    }

    private static OperatorDataExportResponse.Operator toOperator(final OperatorProfile profile) {
        return new OperatorDataExportResponse.Operator(profile.operatorId().value(), profile.companyName(),
                profile.taxId(), profile.contactEmail(), profile.contactPhone(), profile.contractStartDate(),
                profile.contractEndDate(), profile.foundedDate(), profile.status(), profile.createdAt());
    }
}
