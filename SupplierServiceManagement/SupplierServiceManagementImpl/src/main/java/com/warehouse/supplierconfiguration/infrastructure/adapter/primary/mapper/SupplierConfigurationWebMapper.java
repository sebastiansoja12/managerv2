package com.warehouse.supplierconfiguration.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.api.dto.SupplierConfigurationDto;
import com.warehouse.supplierconfiguration.application.port.primary.command.ResetSupplierConfigurationCommand;
import com.warehouse.supplierconfiguration.application.port.primary.command.SupplierConfigurationTarget;
import com.warehouse.supplierconfiguration.application.port.primary.command.UpdateSupplierConfigurationCommand;
import com.warehouse.supplierconfiguration.application.port.primary.command.UpdateSupplierConfigurationPolicyCommand;
import com.warehouse.supplierconfiguration.application.port.primary.result.SubstituteSupplierResult;
import com.warehouse.supplierconfiguration.application.port.primary.result.SupplierConfigurationResult;
import com.warehouse.supplierconfiguration.domain.enumeration.LabelFormat;
import com.warehouse.supplierconfiguration.domain.enumeration.ShipmentSortOrder;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierSetting;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfigurationPolicy;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationRevision;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationValues;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationViolation;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SubstituteSupplierApi;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationApiResponse;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationErrorApi;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationPolicyApi;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationPolicyUpdateApiRequest;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationResetApiRequest;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationRevisionApi;
import com.warehouse.supplierconfiguration.infrastructure.adapter.primary.dto.SupplierConfigurationUpdateApiRequest;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SupplierConfigurationWebMapper {

    private SupplierConfigurationWebMapper() {
    }

    public static UpdateSupplierConfigurationCommand map(final SupplierConfigurationTarget target,
                                                         final SupplierConfigurationUpdateApiRequest request) {
        return new UpdateSupplierConfigurationCommand(target, request.version(), new SupplierConfigurationValues(
                mapSettings(request.settings()),
                request.maxDeliveryAttempts(),
                request.maxShipmentsPerRoute(),
                null,
                request.preferredLabelFormat() != null ? LabelFormat.valueOf(request.preferredLabelFormat()) : null,
                request.defaultShipmentSortOrder() != null
                        ? ShipmentSortOrder.valueOf(request.defaultShipmentSortOrder())
                        : null),
                request.substituteSupplierCode() != null ? new SupplierCode(request.substituteSupplierCode().trim()) : null);
    }

    public static ResetSupplierConfigurationCommand map(final SupplierConfigurationTarget target,
                                                        final SupplierConfigurationResetApiRequest request) {
        return new ResetSupplierConfigurationCommand(target, request.version());
    }

    public static UpdateSupplierConfigurationPolicyCommand map(final SupplierConfigurationPolicyUpdateApiRequest request) {
        return new UpdateSupplierConfigurationPolicyCommand(request.version(), mapSettings(request.enforcedSettings()));
    }

    public static SupplierConfigurationApiResponse map(final SupplierConfigurationResult result) {
        final SupplierConfigurationValues values = result.values();
        return new SupplierConfigurationApiResponse(
                result.supplierId().value(),
                result.supplierCode().value(),
                result.firstName(),
                result.lastName(),
                mapSettingNames(values.settings()),
                values.maxDeliveryAttempts(),
                values.maxShipmentsPerRoute(),
                result.substituteSupplierCode() != null ? result.substituteSupplierCode().value() : null,
                values.preferredLabelFormat().name(),
                values.defaultShipmentSortOrder().name(),
                mapSettingNames(result.enforcedSettings()),
                Arrays.stream(SupplierSetting.values())
                        .map(setting -> new SupplierConfigurationApiResponse.SupplierSettingDefinitionApi(
                                setting.name(),
                                setting.getGroup().name(),
                                setting.getCategory().name(),
                                setting.getDefaultValue()))
                        .toList(),
                result.version(),
                result.updatedAt(),
                userIdValue(result.updatedBy()));
    }

    public static SupplierConfigurationRevisionApi map(final SupplierConfigurationRevision revision) {
        return new SupplierConfigurationRevisionApi(
                revision.revisionId(),
                revision.changeType().name(),
                userIdValue(revision.changedBy()),
                revision.changedAt(),
                revision.changes()
                        .stream()
                        .map(change -> new SupplierConfigurationRevisionApi.SupplierConfigurationChangeApi(
                                change.field(), change.previousValue(), change.newValue()))
                        .toList());
    }

    public static SubstituteSupplierApi map(final SubstituteSupplierResult result) {
        return new SubstituteSupplierApi(result.supplierId().value(), result.supplierCode().value(),
                result.firstName(), result.lastName());
    }

    public static SupplierConfigurationPolicyApi map(final SupplierConfigurationPolicy policy) {
        return new SupplierConfigurationPolicyApi(
                mapSettingNames(policy.getEnforcedSettings()),
                policy.getVersion(),
                policy.getUpdatedAt(),
                userIdValue(policy.getUpdatedBy()));
    }

    public static SupplierConfigurationDto mapToApiDto(final SupplierConfigurationResult result) {
        final SupplierConfigurationValues values = result.values();
        return new SupplierConfigurationDto(
                result.supplierId(),
                mapSettingNames(values.settings()),
                values.maxDeliveryAttempts(),
                values.maxShipmentsPerRoute(),
                values.substituteSupplierId(),
                values.preferredLabelFormat().name(),
                values.defaultShipmentSortOrder().name());
    }

    public static SupplierConfigurationErrorApi map(final String code,
                                                    final String message,
                                                    final List<SupplierConfigurationViolation> violations) {
        return new SupplierConfigurationErrorApi(code, message, violations.stream()
                .map(violation -> new SupplierConfigurationErrorApi.SupplierConfigurationFieldErrorApi(
                        violation.field(), violation.code().name()))
                .toList());
    }

    private static Map<SupplierSetting, Boolean> mapSettings(final Map<String, Boolean> settings) {
        final EnumMap<SupplierSetting, Boolean> mappedSettings = new EnumMap<>(SupplierSetting.class);
        if (settings != null) {
            settings.forEach((setting, enabled) -> mappedSettings.put(SupplierSetting.valueOf(setting), enabled));
        }
        return mappedSettings;
    }

    private static Map<String, Boolean> mapSettingNames(final Map<SupplierSetting, Boolean> settings) {
        final Map<String, Boolean> mappedSettings = new LinkedHashMap<>();
        settings.forEach((setting, enabled) -> mappedSettings.put(setting.name(), enabled));
        return mappedSettings;
    }

    private static Long userIdValue(final UserId userId) {
        return userId != null ? userId.value() : null;
    }
}
