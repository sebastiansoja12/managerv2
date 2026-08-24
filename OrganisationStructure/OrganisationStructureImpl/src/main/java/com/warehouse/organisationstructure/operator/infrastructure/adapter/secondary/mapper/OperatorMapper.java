package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.dto.DefaultShipmentStatusDto;
import com.warehouse.organisationstructure.api.dto.DeliveryTimeConfigurationDto;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.OperatorDto;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import com.warehouse.organisationstructure.api.dto.OperatorStatusDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLabelConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLabelFormatDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLimitsDto;
import com.warehouse.organisationstructure.api.dto.ShipmentNotificationChannelDto;
import com.warehouse.organisationstructure.api.dto.ShipmentNotificationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentServiceLevelDto;
import com.warehouse.organisationstructure.api.dto.ShipmentValidationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentWorkflowConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShippingCapabilitiesDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberDateFormatDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberRuleDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberSourceDto;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.domain.model.OperatorStatus;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.entity.OperatorEntity;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DefaultShipmentStatus;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DeliveryTimeConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.LabelFormat;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.NotificationChannel;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ServiceLevel;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLabelConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLimits;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentNotificationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentValidationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentWorkflowConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShippingCapabilities;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberDateFormat;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberRule;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberSource;

public final class OperatorMapper {

    private OperatorMapper() {
    }

    public static Operator toModel(final OperatorEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Operator(
                entity.getOperatorId(),
                entity.getRegisteringUserId(),
                entity.getTaxId(),
                entity.isSupportsLockers(),
                entity.isSupportsInternationalShipping(),
                entity.isSupportsCashOnDelivery(),
                entity.getContactPhone(),
                entity.getContactEmail(),
                entity.getCompanyName(),
                entity.getContractStartDate(),
                entity.getContractEndDate(),
                entity.getFoundedDate(),
                null,
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static OperatorEntity toEntity(final Operator operator) {
        return new OperatorEntity(
                operator.getOperatorId(),
                operator.getRegisteringUserId(),
                operator.getTaxId(),
                operator.isSupportsLockers(),
                operator.isSupportsInternationalShipping(),
                operator.isSupportsCashOnDelivery(),
                operator.getContactPhone(),
                operator.getContactEmail(),
                operator.getCompanyName(),
                operator.getContractStartDate(),
                operator.getContractEndDate(),
                operator.getFoundedDate(),
                operator.getStatus(),
                operator.getCreatedAt(),
                operator.getUpdatedAt()
        );
    }

    public static OperatorDto toDto(final Operator operator) {
        if (operator == null) {
            return null;
        }
        return new OperatorDto(
                operator.getOperatorId(),
                operator.getRegisteringUserId(),
                operator.getTaxId(),
                operator.isSupportsLockers(),
                operator.isSupportsInternationalShipping(),
                operator.isSupportsCashOnDelivery(),
                operator.getContactPhone(),
                operator.getContactEmail(),
                operator.getCompanyName(),
                operator.getContractStartDate(),
                operator.getContractEndDate(),
                operator.getFoundedDate(),
                toDtoConfiguration(operator.getConfiguration()),
                toDtoStatus(operator.getStatus()),
                operator.getCreatedAt(),
                operator.getUpdatedAt()
        );
    }

    public static OperatorIdDto toDtoId(final OperatorId operatorId) {
        return new OperatorIdDto(operatorId.value());
    }

    public static OperatorConfiguration toModelConfiguration(final OperatorConfigurationDto dto) {
        if (dto == null) {
            return null;
        }
        return new OperatorConfiguration(
                toModelShippingCapabilities(dto.shippingCapabilities()),
                toModelShipmentConfiguration(dto.shipmentConfiguration()),
                toModelDeliveryTimeConfiguration(dto.deliveryTimeConfiguration())
        );
    }

    public static OperatorConfigurationDto toDtoConfiguration(final OperatorConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new OperatorConfigurationDto(
                toDtoShippingCapabilities(configuration.getShippingCapabilities()),
                toDtoShipmentConfiguration(configuration.getShipmentConfiguration()),
                toDtoDeliveryTimeConfiguration(configuration.getDeliveryTimeConfiguration())
        );
    }

    private static ShippingCapabilities toModelShippingCapabilities(
            final ShippingCapabilitiesDto shippingCapabilities) {
        if (shippingCapabilities == null) {
            return null;
        }
        return new ShippingCapabilities(
                shippingCapabilities.supportsDomesticShipping(),
                shippingCapabilities.supportsInternationalShipping(),
                shippingCapabilities.supportsExpressShipping(),
                shippingCapabilities.supportsSameDayDelivery(),
                shippingCapabilities.supportsCashOnDelivery(),
                shippingCapabilities.supportsParcelLockers(),
                shippingCapabilities.supportsPickupPoints(),
                shippingCapabilities.supportsHomeDelivery(),
                shippingCapabilities.supportsSaturdayDelivery(),
                shippingCapabilities.supportsSundayDelivery(),
                shippingCapabilities.supportsReturnShipments(),
                shippingCapabilities.providesTracking(),
                shippingCapabilities.providesInsurance()
        );
    }

    private static ShipmentLimits toModelShipmentLimits(
            final ShipmentLimitsDto shipmentLimits) {
        if (shipmentLimits == null) {
            return null;
        }
        return new ShipmentLimits(
                shipmentLimits.maxWeight(),
                shipmentLimits.minWeight(),
                shipmentLimits.maxLength(),
                shipmentLimits.maxWidth(),
                shipmentLimits.maxHeight(),
                shipmentLimits.maxShipmentValue(),
                shipmentLimits.allowOversized()
        );
    }

    public static ShipmentConfiguration toModelShipmentConfiguration(
            final ShipmentConfigurationDto shipmentConfiguration) {
        if (shipmentConfiguration == null) {
            return null;
        }
        return new ShipmentConfiguration(
                toModelShipmentValidationConfiguration(shipmentConfiguration.validationConfiguration()),
                toModelShipmentLabelConfiguration(shipmentConfiguration.labelConfiguration()),
                toModelShipmentLimits(shipmentConfiguration.shipmentLimits()),
                toModelShipmentWorkflowConfiguration(shipmentConfiguration.workflowConfiguration()),
                toModelTrackingNumberRule(shipmentConfiguration.trackingNumberRule()),
                toModelShipmentNotificationConfiguration(shipmentConfiguration.notificationConfiguration())
        );
    }

    private static ShipmentValidationConfiguration toModelShipmentValidationConfiguration(
            final ShipmentValidationConfigurationDto validationConfiguration) {
        if (validationConfiguration == null) {
            return null;
        }
        return new ShipmentValidationConfiguration(
                validationConfiguration.validateAddressData(),
                validationConfiguration.requireRecipientPhone(),
                validationConfiguration.requireRecipientEmail(),
                validationConfiguration.preventDuplicateTracking(),
                validationConfiguration.requireSenderReference(),
                validationConfiguration.validatePostalCode()
        );
    }

    private static ShipmentLabelConfiguration toModelShipmentLabelConfiguration(
            final ShipmentLabelConfigurationDto labelConfiguration) {
        if (labelConfiguration == null) {
            return null;
        }
        return new ShipmentLabelConfiguration(
                labelConfiguration.autoGenerateLabels(),
                labelConfiguration.includeReturnLabel(),
                labelConfiguration.attachPackingSlip(),
                labelConfiguration.labelFormat() != null
                        ? LabelFormat.valueOf(labelConfiguration.labelFormat().name())
                        : null
        );
    }

    private static ShipmentWorkflowConfiguration toModelShipmentWorkflowConfiguration(
            final ShipmentWorkflowConfigurationDto workflowConfiguration) {
        if (workflowConfiguration == null) {
            return null;
        }
        return new ShipmentWorkflowConfiguration(
                workflowConfiguration.defaultStatus() != null
                        ? DefaultShipmentStatus.valueOf(workflowConfiguration.defaultStatus().name())
                        : null,
                workflowConfiguration.defaultServiceLevel() != null
                        ? ServiceLevel.valueOf(workflowConfiguration.defaultServiceLevel().name())
                        : null,
                workflowConfiguration.autoAssignCourier(),
                workflowConfiguration.autoCloseDelivered(),
                workflowConfiguration.generateTrackingNumber(),
                workflowConfiguration.cancellationWindowMinutes(),
                workflowConfiguration.pickupCutoffTime()
        );
    }

    private static TrackingNumberRule toModelTrackingNumberRule(
            final TrackingNumberRuleDto trackingNumberRule) {
        if (trackingNumberRule == null) {
            return null;
        }
        return new TrackingNumberRule(
                trackingNumberRule.key(),
                trackingNumberRule.separator(),
                trackingNumberRule.source() != null
                        ? TrackingNumberSource.valueOf(trackingNumberRule.source().name())
                        : null,
                trackingNumberRule.randomLength(),
                trackingNumberRule.includeDate(),
                trackingNumberRule.dateFormat() != null
                        ? TrackingNumberDateFormat.valueOf(trackingNumberRule.dateFormat().name())
                        : null,
                trackingNumberRule.uppercase()
        );
    }

    private static ShipmentNotificationConfiguration toModelShipmentNotificationConfiguration(
            final ShipmentNotificationConfigurationDto notificationConfiguration) {
        if (notificationConfiguration == null) {
            return null;
        }
        return new ShipmentNotificationConfiguration(
                notificationConfiguration.notifyRecipientOnCreated(),
                notificationConfiguration.notifyRecipientOnDispatched(),
                notificationConfiguration.notifyRecipientOnDelivered(),
                notificationConfiguration.notifySenderOnException(),
                notificationConfiguration.notificationChannel() != null
                        ? NotificationChannel.valueOf(notificationConfiguration.notificationChannel().name())
                        : null
        );
    }

    private static DeliveryTimeConfiguration toModelDeliveryTimeConfiguration(
            final DeliveryTimeConfigurationDto deliveryTimeConfiguration) {
        if (deliveryTimeConfiguration == null) {
            return null;
        }
        return new DeliveryTimeConfiguration(
                deliveryTimeConfiguration.minDeliveryDays(),
                deliveryTimeConfiguration.maxDeliveryDays(),
                deliveryTimeConfiguration.expressDeliveryDays(),
                deliveryTimeConfiguration.sameDayDeliveryHours(),
                deliveryTimeConfiguration.internationalDeliveryDays()
        );
    }

    private static ShippingCapabilitiesDto toDtoShippingCapabilities(
            final ShippingCapabilities capabilities) {
        if (capabilities == null) {
            return null;
        }
        return new ShippingCapabilitiesDto(
                capabilities.isSupportsDomesticShipping(),
                capabilities.isSupportsInternationalShipping(),
                capabilities.isSupportsExpressShipping(),
                capabilities.isSupportsSameDayDelivery(),
                capabilities.isSupportsCashOnDelivery(),
                capabilities.isSupportsParcelLockers(),
                capabilities.isSupportsPickupPoints(),
                capabilities.isSupportsHomeDelivery(),
                capabilities.isSupportsSaturdayDelivery(),
                capabilities.isSupportsSundayDelivery(),
                capabilities.isSupportsReturnShipments(),
                capabilities.isProvidesTracking(),
                capabilities.isProvidesInsurance()
        );
    }

    private static ShipmentLimitsDto toDtoShipmentLimits(
            final ShipmentLimits limits) {
        if (limits == null) {
            return null;
        }
        return new ShipmentLimitsDto(
                limits.getMaxWeight(),
                limits.getMinWeight(),
                limits.getMaxLength(),
                limits.getMaxWidth(),
                limits.getMaxHeight(),
                limits.getMaxShipmentValue(),
                limits.isAllowOversized()
        );
    }

    public static ShipmentConfigurationDto toDtoShipmentConfiguration(
            final ShipmentConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentConfigurationDto(
                toDtoShipmentValidationConfiguration(configuration.getValidationConfiguration()),
                toDtoShipmentLabelConfiguration(configuration.getLabelConfiguration()),
                toDtoShipmentLimits(configuration.getShipmentLimits()),
                toDtoShipmentWorkflowConfiguration(configuration.getWorkflowConfiguration()),
                toDtoTrackingNumberRule(configuration.getTrackingNumberRule()),
                toDtoShipmentNotificationConfiguration(configuration.getNotificationConfiguration())
        );
    }

    public static ShipmentLimitsDto toDtoCurrentShipmentLimits(final OperatorConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return toDtoShipmentLimits(configuration.getShipmentLimits());
    }

    private static ShipmentValidationConfigurationDto toDtoShipmentValidationConfiguration(
            final ShipmentValidationConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentValidationConfigurationDto(
                configuration.isValidateAddressData(),
                configuration.isRequireRecipientPhone(),
                configuration.isRequireRecipientEmail(),
                configuration.isPreventDuplicateTracking(),
                configuration.isRequireSenderReference(),
                configuration.isValidatePostalCode()
        );
    }

    private static ShipmentLabelConfigurationDto toDtoShipmentLabelConfiguration(
            final ShipmentLabelConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentLabelConfigurationDto(
                configuration.isAutoGenerateLabels(),
                configuration.isIncludeReturnLabel(),
                configuration.isAttachPackingSlip(),
                configuration.getLabelFormat() != null
                        ? ShipmentLabelFormatDto.valueOf(configuration.getLabelFormat().name())
                        : null
        );
    }

    private static ShipmentWorkflowConfigurationDto toDtoShipmentWorkflowConfiguration(
            final ShipmentWorkflowConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentWorkflowConfigurationDto(
                configuration.getDefaultStatus() != null
                        ? DefaultShipmentStatusDto.valueOf(configuration.getDefaultStatus().name())
                        : null,
                configuration.getDefaultServiceLevel() != null
                        ? ShipmentServiceLevelDto.valueOf(configuration.getDefaultServiceLevel().name())
                        : null,
                configuration.isAutoAssignCourier(),
                configuration.isAutoCloseDelivered(),
                configuration.isGenerateTrackingNumber(),
                configuration.getCancellationWindowMinutes(),
                configuration.getPickupCutoffTime()
        );
    }

    private static TrackingNumberRuleDto toDtoTrackingNumberRule(
            final TrackingNumberRule rule) {
        if (rule == null) {
            return null;
        }
        return new TrackingNumberRuleDto(
                rule.getKey(),
                rule.getSeparator(),
                rule.getSource() != null ? TrackingNumberSourceDto.valueOf(rule.getSource().name()) : null,
                rule.getRandomLength(),
                rule.isIncludeDate(),
                rule.getDateFormat() != null ? TrackingNumberDateFormatDto.valueOf(rule.getDateFormat().name()) : null,
                rule.isUppercase()
        );
    }

    private static ShipmentNotificationConfigurationDto toDtoShipmentNotificationConfiguration(
            final ShipmentNotificationConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentNotificationConfigurationDto(
                configuration.isNotifyRecipientOnCreated(),
                configuration.isNotifyRecipientOnDispatched(),
                configuration.isNotifyRecipientOnDelivered(),
                configuration.isNotifySenderOnException(),
                configuration.getNotificationChannel() != null
                        ? ShipmentNotificationChannelDto.valueOf(configuration.getNotificationChannel().name())
                        : null
        );
    }

    private static DeliveryTimeConfigurationDto toDtoDeliveryTimeConfiguration(
            final DeliveryTimeConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new DeliveryTimeConfigurationDto(
                configuration.getMinDeliveryDays(),
                configuration.getMaxDeliveryDays(),
                configuration.getExpressDeliveryDays(),
                configuration.getSameDayDeliveryHours(),
                configuration.getInternationalDeliveryDays()
        );
    }

    private static OperatorStatusDto toDtoStatus(final OperatorStatus status) {
        return OperatorStatusDto.valueOf(status.name());
    }
}
