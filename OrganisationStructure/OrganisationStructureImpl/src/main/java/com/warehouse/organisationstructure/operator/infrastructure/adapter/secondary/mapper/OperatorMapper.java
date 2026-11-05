package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.DefaultShipmentStatusDto;
import com.warehouse.organisationstructure.api.dto.DeliveryTimeConfigurationDto;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.OperatorDto;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import com.warehouse.organisationstructure.api.dto.OperatorStatusDto;
import com.warehouse.organisationstructure.api.dto.CommunicationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.CurrencyCodeDto;
import com.warehouse.organisationstructure.api.dto.CustomerLanguageDto;
import com.warehouse.organisationstructure.api.dto.FleetConfigurationDto;
import com.warehouse.organisationstructure.api.dto.LengthUnitDto;
import com.warehouse.organisationstructure.api.dto.RegionalConfigurationDto;
import com.warehouse.organisationstructure.api.dto.RegionalDateFormatDto;
import com.warehouse.organisationstructure.api.dto.WeightUnitDto;
import com.warehouse.organisationstructure.api.dto.DuplicateScanPolicyDto;
import com.warehouse.organisationstructure.api.dto.ScanErrorFeedbackDto;
import com.warehouse.organisationstructure.api.dto.SortingConfigurationDto;
import com.warehouse.organisationstructure.api.dto.UnresolvedDestinationPolicyDto;
import com.warehouse.organisationstructure.api.dto.DeliveryAreaModeDto;
import com.warehouse.organisationstructure.api.dto.DepartmentCapacityPolicyDto;
import com.warehouse.organisationstructure.api.dto.DepartmentConnectionPolicyDto;
import com.warehouse.organisationstructure.api.dto.DepartmentDeliveryAreaPolicyDto;
import com.warehouse.organisationstructure.api.dto.DepartmentLifecyclePolicyDto;
import com.warehouse.organisationstructure.api.dto.DepartmentWorkTransferModeDto;
import com.warehouse.organisationstructure.api.dto.UncoveredPostalCodePolicyDto;
import com.warehouse.organisationstructure.api.dto.DefaultPickupPointTypeDto;
import com.warehouse.organisationstructure.api.dto.FullPickupPointPolicyDto;
import com.warehouse.organisationstructure.api.dto.OversizedLockerShipmentPolicyDto;
import com.warehouse.organisationstructure.api.dto.PickupPointCapacityPolicyDto;
import com.warehouse.organisationstructure.api.dto.PickupPointCollectionPolicyDto;
import com.warehouse.organisationstructure.api.dto.PickupPointConfigurationDto;
import com.warehouse.organisationstructure.api.dto.PickupPointDefaultsPolicyDto;
import com.warehouse.organisationstructure.api.dto.PickupPointLifecyclePolicyDto;
import com.warehouse.organisationstructure.api.dto.PickupPointLockerPolicyDto;
import com.warehouse.organisationstructure.api.dto.PickupPointMaxShipmentSizeDto;
import com.warehouse.organisationstructure.api.dto.PickupPointPreferenceDto;
import com.warehouse.organisationstructure.api.dto.PickupPointSelectionPolicyDto;
import com.warehouse.organisationstructure.api.dto.RecipientVerificationMethodDto;
import com.warehouse.organisationstructure.api.dto.SuspendedPickupPointShipmentPolicyDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.CashOnDeliveryPaymentMethodDto;
import com.warehouse.organisationstructure.api.dto.ProofOfDeliveryModeDto;
import com.warehouse.organisationstructure.api.dto.ShipmentDeliveryConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentReturnConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentPaymentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentSlaConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentDataRetentionConfigurationDto;
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
import com.warehouse.organisationstructure.api.dto.TrackingNumberPrefixModeDto;
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
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CommunicationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CurrencyCode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CustomerLanguage;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.FleetConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.LengthUnit;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RegionalConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RegionalDateFormat;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.WeightUnit;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DuplicateScanPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ScanErrorFeedback;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.SortingConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.UnresolvedDestinationPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DeliveryAreaMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentCapacityPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConnectionPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentDeliveryAreaPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentLifecyclePolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentWorkTransferMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.UncoveredPostalCodePolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DefaultPickupPointType;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.FullPickupPointPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OversizedLockerShipmentPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointCapacityPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointCollectionPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointDefaultsPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointLifecyclePolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointLockerPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointMaxShipmentSize;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointPreference;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointSelectionPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RecipientVerificationMethod;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.SuspendedPickupPointShipmentPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CashOnDeliveryPaymentMethod;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ProofOfDeliveryMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentDeliveryConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentReturnConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentPaymentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentSlaConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentDataRetentionConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLabelConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLimits;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentNotificationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentValidationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentWorkflowConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShippingCapabilities;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberDateFormat;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberPrefixMode;
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

    public static FleetConfigurationDto toDtoFleetConfiguration(final FleetConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new FleetConfigurationDto(
                configuration.getVehicleInspectionReminderDays(),
                configuration.getVehicleInsuranceReminderDays(),
                configuration.getMaxDriverShiftHours(),
                configuration.getRequiredBreakMinutes(),
                configuration.isRequirePreTripChecklist(),
                configuration.getGpsPositionIntervalSeconds(),
                configuration.getDefaultVehiclePalletCapacity()
        );
    }

    public static FleetConfiguration toModelFleetConfiguration(final FleetConfigurationDto dto) {
        if (dto == null) {
            return null;
        }
        return new FleetConfiguration(
                dto.vehicleInspectionReminderDays(),
                dto.vehicleInsuranceReminderDays(),
                dto.maxDriverShiftHours(),
                dto.requiredBreakMinutes(),
                dto.requirePreTripChecklist(),
                dto.gpsPositionIntervalSeconds(),
                dto.defaultVehiclePalletCapacity()
        );
    }

    public static CommunicationConfigurationDto toDtoCommunicationConfiguration(final CommunicationConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new CommunicationConfigurationDto(
                configuration.getSenderName(),
                configuration.getReplyToEmail(),
                configuration.getEmailFooter(),
                configuration.getLogoUrl(),
                configuration.getAccentColor(),
                configuration.getDefaultCustomerLanguage() != null
                        ? CustomerLanguageDto.valueOf(configuration.getDefaultCustomerLanguage().name())
                        : null,
                configuration.getChatMessageRetentionDays(),
                configuration.isChatAttachmentsAllowed(),
                configuration.getChatMaxAttachmentSizeMb()
        );
    }

    public static CommunicationConfiguration toModelCommunicationConfiguration(final CommunicationConfigurationDto dto) {
        if (dto == null) {
            return null;
        }
        return new CommunicationConfiguration(
                dto.senderName(),
                dto.replyToEmail(),
                dto.emailFooter(),
                dto.logoUrl(),
                dto.accentColor(),
                dto.defaultCustomerLanguage() != null ? CustomerLanguage.valueOf(dto.defaultCustomerLanguage().name()) : null,
                dto.chatMessageRetentionDays(),
                dto.chatAttachmentsAllowed(),
                dto.chatMaxAttachmentSizeMb()
        );
    }

    public static RegionalConfigurationDto toDtoRegionalConfiguration(final RegionalConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new RegionalConfigurationDto(
                configuration.getTimeZone(),
                configuration.getDateFormat() != null
                        ? RegionalDateFormatDto.valueOf(configuration.getDateFormat().name())
                        : null,
                configuration.getCurrency() != null
                        ? CurrencyCodeDto.valueOf(configuration.getCurrency().name())
                        : null,
                configuration.getWeightUnit() != null
                        ? WeightUnitDto.valueOf(configuration.getWeightUnit().name())
                        : null,
                configuration.getLengthUnit() != null
                        ? LengthUnitDto.valueOf(configuration.getLengthUnit().name())
                        : null,
                configuration.getWorkingDayStart(),
                configuration.getWorkingDayEnd(),
                configuration.isSaturdayWorkingDay(),
                configuration.isSundayWorkingDay()
        );
    }

    public static RegionalConfiguration toModelRegionalConfiguration(final RegionalConfigurationDto dto) {
        if (dto == null) {
            return null;
        }
        return new RegionalConfiguration(
                dto.timeZone(),
                dto.dateFormat() != null ? RegionalDateFormat.valueOf(dto.dateFormat().name()) : null,
                dto.currency() != null ? CurrencyCode.valueOf(dto.currency().name()) : null,
                dto.weightUnit() != null ? WeightUnit.valueOf(dto.weightUnit().name()) : null,
                dto.lengthUnit() != null ? LengthUnit.valueOf(dto.lengthUnit().name()) : null,
                dto.workingDayStart(),
                dto.workingDayEnd(),
                dto.saturdayWorkingDay(),
                dto.sundayWorkingDay()
        );
    }

    public static SortingConfigurationDto toDtoSortingConfiguration(final SortingConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new SortingConfigurationDto(
                configuration.isRequireInboundScan(),
                configuration.isRequireSortScan(),
                configuration.isRequireOutboundScan(),
                configuration.getDuplicateScanPolicy() != null
                        ? DuplicateScanPolicyDto.valueOf(configuration.getDuplicateScanPolicy().name())
                        : null,
                configuration.getUnresolvedDestinationPolicy() != null
                        ? UnresolvedDestinationPolicyDto.valueOf(configuration.getUnresolvedDestinationPolicy().name())
                        : null,
                configuration.getExceptionRampCode(),
                configuration.isRequireDamagePhotoOnInbound(),
                configuration.getScanErrorFeedback() != null
                        ? ScanErrorFeedbackDto.valueOf(configuration.getScanErrorFeedback().name())
                        : null,
                configuration.getMissingInWarehouseAfterHours()
        );
    }

    public static SortingConfiguration toModelSortingConfiguration(final SortingConfigurationDto dto) {
        if (dto == null) {
            return null;
        }
        return new SortingConfiguration(
                dto.requireInboundScan(),
                dto.requireSortScan(),
                dto.requireOutboundScan(),
                dto.duplicateScanPolicy() != null ? DuplicateScanPolicy.valueOf(dto.duplicateScanPolicy().name()) : null,
                dto.unresolvedDestinationPolicy() != null ? UnresolvedDestinationPolicy.valueOf(dto.unresolvedDestinationPolicy().name()) : null,
                dto.exceptionRampCode(),
                dto.requireDamagePhotoOnInbound(),
                dto.scanErrorFeedback() != null ? ScanErrorFeedback.valueOf(dto.scanErrorFeedback().name()) : null,
                dto.missingInWarehouseAfterHours()
        );
    }

    public static DepartmentDeliveryAreaPolicyDto toDtoDepartmentDeliveryAreaPolicy(final DepartmentDeliveryAreaPolicy configuration) {
        if (configuration == null) {
            return null;
        }
        return new DepartmentDeliveryAreaPolicyDto(
                configuration.getDeliveryAreaMode() != null
                        ? DeliveryAreaModeDto.valueOf(configuration.getDeliveryAreaMode().name())
                        : null,
                configuration.getMaxDeliveryRadiusKm(),
                configuration.isAllowManualDeliveryAreaEdit(),
                configuration.isRecalculateAreasOnStatusChange(),
                configuration.getUncoveredPostalCodePolicy() != null
                        ? UncoveredPostalCodePolicyDto.valueOf(configuration.getUncoveredPostalCodePolicy().name())
                        : null
        );
    }

    public static DepartmentDeliveryAreaPolicy toModelDepartmentDeliveryAreaPolicy(final DepartmentDeliveryAreaPolicyDto dto) {
        if (dto == null) {
            return null;
        }
        return new DepartmentDeliveryAreaPolicy(
                dto.deliveryAreaMode() != null ? DeliveryAreaMode.valueOf(dto.deliveryAreaMode().name()) : null,
                dto.maxDeliveryRadiusKm(),
                dto.allowManualDeliveryAreaEdit(),
                dto.recalculateAreasOnStatusChange(),
                dto.uncoveredPostalCodePolicy() != null ? UncoveredPostalCodePolicy.valueOf(dto.uncoveredPostalCodePolicy().name()) : null
        );
    }

    public static DepartmentConnectionPolicyDto toDtoDepartmentConnectionPolicy(final DepartmentConnectionPolicy configuration) {
        if (configuration == null) {
            return null;
        }
        return new DepartmentConnectionPolicyDto(
                configuration.getSortingCenterConnectionCount(),
                configuration.getMaxAutoConnectionDistanceKm(),
                configuration.isRequireSortingConnectionForActivation(),
                configuration.isBidirectionalConnectionsByDefault()
        );
    }

    public static DepartmentConnectionPolicy toModelDepartmentConnectionPolicy(final DepartmentConnectionPolicyDto dto) {
        if (dto == null) {
            return null;
        }
        return new DepartmentConnectionPolicy(
                dto.sortingCenterConnectionCount(),
                dto.maxAutoConnectionDistanceKm(),
                dto.requireSortingConnectionForActivation(),
                dto.bidirectionalConnectionsByDefault()
        );
    }

    public static DepartmentLifecyclePolicyDto toDtoDepartmentLifecyclePolicy(final DepartmentLifecyclePolicy configuration) {
        if (configuration == null) {
            return null;
        }
        return new DepartmentLifecyclePolicyDto(
                configuration.isRequireStatusChangeReason(),
                configuration.isBlockDeactivationWithActiveWork(),
                configuration.getWorkTransferOnDeactivation() != null
                        ? DepartmentWorkTransferModeDto.valueOf(configuration.getWorkTransferOnDeactivation().name())
                        : null,
                configuration.getAutoArchiveInactiveAfterDays()
        );
    }

    public static DepartmentLifecyclePolicy toModelDepartmentLifecyclePolicy(final DepartmentLifecyclePolicyDto dto) {
        if (dto == null) {
            return null;
        }
        return new DepartmentLifecyclePolicy(
                dto.requireStatusChangeReason(),
                dto.blockDeactivationWithActiveWork(),
                dto.workTransferOnDeactivation() != null ? DepartmentWorkTransferMode.valueOf(dto.workTransferOnDeactivation().name()) : null,
                dto.autoArchiveInactiveAfterDays()
        );
    }

    public static DepartmentCapacityPolicyDto toDtoDepartmentCapacityPolicy(final DepartmentCapacityPolicy configuration) {
        if (configuration == null) {
            return null;
        }
        return new DepartmentCapacityPolicyDto(
                configuration.getDefaultDailyCapacity(),
                configuration.getCapacityWarningThresholdPercent(),
                configuration.isRedirectOverflowToNeighbour(),
                configuration.getMaxCouriersPerDepartment()
        );
    }

    public static DepartmentCapacityPolicy toModelDepartmentCapacityPolicy(final DepartmentCapacityPolicyDto dto) {
        if (dto == null) {
            return null;
        }
        return new DepartmentCapacityPolicy(
                dto.defaultDailyCapacity(),
                dto.capacityWarningThresholdPercent(),
                dto.redirectOverflowToNeighbour(),
                dto.maxCouriersPerDepartment()
        );
    }

    public static PickupPointConfigurationDto toDtoPickupPointConfiguration(
            final PickupPointConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new PickupPointConfigurationDto(
                toDtoPickupPointDefaultsPolicy(configuration.getDefaultsPolicy()),
                toDtoPickupPointCollectionPolicy(configuration.getCollectionPolicy()),
                toDtoPickupPointLockerPolicy(configuration.getLockerPolicy()),
                toDtoPickupPointSelectionPolicy(configuration.getSelectionPolicy()),
                toDtoPickupPointCapacityPolicy(configuration.getCapacityPolicy()),
                toDtoPickupPointLifecyclePolicy(configuration.getLifecyclePolicy())
        );
    }

    public static PickupPointConfiguration toModelPickupPointConfiguration(final PickupPointConfigurationDto dto) {
        if (dto == null) {
            return null;
        }
        return new PickupPointConfiguration(
                toModelPickupPointDefaultsPolicy(dto.defaultsPolicy()),
                toModelPickupPointCollectionPolicy(dto.collectionPolicy()),
                toModelPickupPointLockerPolicy(dto.lockerPolicy()),
                toModelPickupPointSelectionPolicy(dto.selectionPolicy()),
                toModelPickupPointCapacityPolicy(dto.capacityPolicy()),
                toModelPickupPointLifecyclePolicy(dto.lifecyclePolicy())
        );
    }

    public static PickupPointDefaultsPolicyDto toDtoPickupPointDefaultsPolicy(final PickupPointDefaultsPolicy configuration) {
        if (configuration == null) {
            return null;
        }
        return new PickupPointDefaultsPolicyDto(
                configuration.getDefaultType() != null
                        ? DefaultPickupPointTypeDto.valueOf(configuration.getDefaultType().name())
                        : null,
                configuration.isDefaultDropOffEnabled(),
                configuration.isDefaultCollectionEnabled(),
                configuration.getServicePointMaxShipmentSize() != null
                        ? PickupPointMaxShipmentSizeDto.valueOf(configuration.getServicePointMaxShipmentSize().name())
                        : null,
                configuration.getParcelLockerMaxShipmentSize() != null
                        ? PickupPointMaxShipmentSizeDto.valueOf(configuration.getParcelLockerMaxShipmentSize().name())
                        : null,
                configuration.isDefaultAcceptsDangerousGoods(),
                configuration.getWeekdayOpeningTime(),
                configuration.getWeekdayClosingTime(),
                configuration.isSaturdayOpen(),
                configuration.getSaturdayOpeningTime(),
                configuration.getSaturdayClosingTime(),
                configuration.isRequireCoordinatesForActivation()
        );
    }

    public static PickupPointDefaultsPolicy toModelPickupPointDefaultsPolicy(final PickupPointDefaultsPolicyDto dto) {
        if (dto == null) {
            return null;
        }
        return new PickupPointDefaultsPolicy(
                dto.defaultType() != null ? DefaultPickupPointType.valueOf(dto.defaultType().name()) : null,
                dto.defaultDropOffEnabled(),
                dto.defaultCollectionEnabled(),
                dto.servicePointMaxShipmentSize() != null ? PickupPointMaxShipmentSize.valueOf(dto.servicePointMaxShipmentSize().name()) : null,
                dto.parcelLockerMaxShipmentSize() != null ? PickupPointMaxShipmentSize.valueOf(dto.parcelLockerMaxShipmentSize().name()) : null,
                dto.defaultAcceptsDangerousGoods(),
                dto.weekdayOpeningTime(),
                dto.weekdayClosingTime(),
                dto.saturdayOpen(),
                dto.saturdayOpeningTime(),
                dto.saturdayClosingTime(),
                dto.requireCoordinatesForActivation()
        );
    }

    public static PickupPointCollectionPolicyDto toDtoPickupPointCollectionPolicy(final PickupPointCollectionPolicy configuration) {
        if (configuration == null) {
            return null;
        }
        return new PickupPointCollectionPolicyDto(
                configuration.getVerificationMethod() != null
                        ? RecipientVerificationMethodDto.valueOf(configuration.getVerificationMethod().name())
                        : null,
                configuration.getPickupCodeLength(),
                configuration.getPickupCodeValidityHours(),
                configuration.getMaxInvalidCodeAttempts(),
                configuration.isAllowAuthorizedPerson(),
                configuration.getReminderAfterDays(),
                configuration.isAllowStorageExtension(),
                configuration.getStorageExtensionDays()
        );
    }

    public static PickupPointCollectionPolicy toModelPickupPointCollectionPolicy(final PickupPointCollectionPolicyDto dto) {
        if (dto == null) {
            return null;
        }
        return new PickupPointCollectionPolicy(
                dto.verificationMethod() != null ? RecipientVerificationMethod.valueOf(dto.verificationMethod().name()) : null,
                dto.pickupCodeLength(),
                dto.pickupCodeValidityHours(),
                dto.maxInvalidCodeAttempts(),
                dto.allowAuthorizedPerson(),
                dto.reminderAfterDays(),
                dto.allowStorageExtension(),
                dto.storageExtensionDays()
        );
    }

    public static PickupPointLockerPolicyDto toDtoPickupPointLockerPolicy(final PickupPointLockerPolicy configuration) {
        if (configuration == null) {
            return null;
        }
        return new PickupPointLockerPolicyDto(
                configuration.getCourierCompartmentHoldMinutes(),
                configuration.getCompartmentReservationHours(),
                configuration.getOversizedShipmentPolicy() != null
                        ? OversizedLockerShipmentPolicyDto.valueOf(configuration.getOversizedShipmentPolicy().name())
                        : null,
                configuration.getSuspendAfterCompartmentFailures()
        );
    }

    public static PickupPointLockerPolicy toModelPickupPointLockerPolicy(final PickupPointLockerPolicyDto dto) {
        if (dto == null) {
            return null;
        }
        return new PickupPointLockerPolicy(
                dto.courierCompartmentHoldMinutes(),
                dto.compartmentReservationHours(),
                dto.oversizedShipmentPolicy() != null ? OversizedLockerShipmentPolicy.valueOf(dto.oversizedShipmentPolicy().name()) : null,
                dto.suspendAfterCompartmentFailures()
        );
    }

    public static PickupPointSelectionPolicyDto toDtoPickupPointSelectionPolicy(final PickupPointSelectionPolicy configuration) {
        if (configuration == null) {
            return null;
        }
        return new PickupPointSelectionPolicyDto(
                configuration.getMaxDistanceKm(),
                configuration.getPreference() != null
                        ? PickupPointPreferenceDto.valueOf(configuration.getPreference().name())
                        : null,
                configuration.getAlternativeCount(),
                configuration.getFullPointPolicy() != null
                        ? FullPickupPointPolicyDto.valueOf(configuration.getFullPointPolicy().name())
                        : null,
                configuration.isSameDepartmentOnly()
        );
    }

    public static PickupPointSelectionPolicy toModelPickupPointSelectionPolicy(final PickupPointSelectionPolicyDto dto) {
        if (dto == null) {
            return null;
        }
        return new PickupPointSelectionPolicy(
                dto.maxDistanceKm(),
                dto.preference() != null ? PickupPointPreference.valueOf(dto.preference().name()) : null,
                dto.alternativeCount(),
                dto.fullPointPolicy() != null ? FullPickupPointPolicy.valueOf(dto.fullPointPolicy().name()) : null,
                dto.sameDepartmentOnly()
        );
    }

    public static PickupPointCapacityPolicyDto toDtoPickupPointCapacityPolicy(final PickupPointCapacityPolicy configuration) {
        if (configuration == null) {
            return null;
        }
        return new PickupPointCapacityPolicyDto(
                configuration.getDefaultCapacity(),
                configuration.getCapacityWarningThresholdPercent(),
                configuration.isBlockIntakeWhenFull()
        );
    }

    public static PickupPointCapacityPolicy toModelPickupPointCapacityPolicy(final PickupPointCapacityPolicyDto dto) {
        if (dto == null) {
            return null;
        }
        return new PickupPointCapacityPolicy(
                dto.defaultCapacity(),
                dto.capacityWarningThresholdPercent(),
                dto.blockIntakeWhenFull()
        );
    }

    public static PickupPointLifecyclePolicyDto toDtoPickupPointLifecyclePolicy(final PickupPointLifecyclePolicy configuration) {
        if (configuration == null) {
            return null;
        }
        return new PickupPointLifecyclePolicyDto(
                configuration.isRequireStatusChangeReason(),
                configuration.getSuspendedPointShipmentPolicy() != null
                        ? SuspendedPickupPointShipmentPolicyDto.valueOf(configuration.getSuspendedPointShipmentPolicy().name())
                        : null,
                configuration.isBlockClosingWithPendingShipments(),
                configuration.getAutoResumeSuspendedAfterDays()
        );
    }

    public static PickupPointLifecyclePolicy toModelPickupPointLifecyclePolicy(final PickupPointLifecyclePolicyDto dto) {
        if (dto == null) {
            return null;
        }
        return new PickupPointLifecyclePolicy(
                dto.requireStatusChangeReason(),
                dto.suspendedPointShipmentPolicy() != null ? SuspendedPickupPointShipmentPolicy.valueOf(dto.suspendedPointShipmentPolicy().name()) : null,
                dto.blockClosingWithPendingShipments(),
                dto.autoResumeSuspendedAfterDays()
        );
    }

    public static DepartmentConfigurationDto toDtoDepartmentConfiguration(final DepartmentConfiguration configuration) {
        return new DepartmentConfigurationDto(
                configuration.isAutoAssignDeliveryAreas(),
                configuration.isAutoConnectDepartments(),
                configuration.isPreventDepartmentDeletion(),
                configuration.isPreventDepartmentArchiving(),
                toDtoDepartmentDeliveryAreaPolicy(configuration.getDeliveryAreaPolicy()),
                toDtoDepartmentConnectionPolicy(configuration.getConnectionPolicy()),
                toDtoDepartmentLifecyclePolicy(configuration.getLifecyclePolicy()),
                toDtoDepartmentCapacityPolicy(configuration.getCapacityPolicy())
        );
    }

    public static DepartmentConfiguration toModelDepartmentConfiguration(final DepartmentConfigurationDto dto) {
        return new DepartmentConfiguration(
                dto.autoAssignDeliveryAreas(),
                dto.autoConnectDepartments(),
                dto.preventDepartmentDeletion(),
                dto.preventDepartmentArchiving(),
                toModelDepartmentDeliveryAreaPolicy(dto.deliveryAreaPolicy()),
                toModelDepartmentConnectionPolicy(dto.connectionPolicy()),
                toModelDepartmentLifecyclePolicy(dto.lifecyclePolicy()),
                toModelDepartmentCapacityPolicy(dto.capacityPolicy())
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

    public static ShipmentLimits toModelShipmentLimits(
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
                toModelShipmentNotificationConfiguration(shipmentConfiguration.notificationConfiguration()),
                toModelShipmentDeliveryConfiguration(shipmentConfiguration.deliveryConfiguration()),
                toModelShipmentReturnConfiguration(shipmentConfiguration.returnConfiguration()),
                toModelShipmentPaymentConfiguration(shipmentConfiguration.paymentConfiguration()),
                toModelShipmentSlaConfiguration(shipmentConfiguration.slaConfiguration()),
                toModelShipmentDataRetentionConfiguration(shipmentConfiguration.dataRetentionConfiguration())
        );
    }

    public static ShipmentValidationConfiguration toModelShipmentValidationConfiguration(
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

    public static ShipmentLabelConfiguration toModelShipmentLabelConfiguration(
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

    public static ShipmentWorkflowConfiguration toModelShipmentWorkflowConfiguration(
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

    public static TrackingNumberRule toModelTrackingNumberRule(
            final TrackingNumberRuleDto trackingNumberRule) {
        if (trackingNumberRule == null) {
            return null;
        }
        return new TrackingNumberRule(
                trackingNumberRule.prefixMode() != null
                        ? TrackingNumberPrefixMode.valueOf(trackingNumberRule.prefixMode().name())
                        : null,
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

    public static ShipmentNotificationConfiguration toModelShipmentNotificationConfiguration(
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
                toDtoShipmentNotificationConfiguration(configuration.getNotificationConfiguration()),
                toDtoShipmentDeliveryConfiguration(configuration.getDeliveryConfiguration()),
                toDtoShipmentReturnConfiguration(configuration.getReturnConfiguration()),
                toDtoShipmentPaymentConfiguration(configuration.getPaymentConfiguration()),
                toDtoShipmentSlaConfiguration(configuration.getSlaConfiguration()),
                toDtoShipmentDataRetentionConfiguration(configuration.getDataRetentionConfiguration())
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
                rule.getPrefixMode() != null
                        ? TrackingNumberPrefixModeDto.valueOf(rule.getPrefixMode().name())
                        : null,
                rule.getKey(),
                rule.getSeparator(),
                rule.getSource() != null ? TrackingNumberSourceDto.valueOf(rule.getSource().name()) : null,
                rule.getRandomLength(),
                rule.isIncludeDate(),
                rule.getDateFormat() != null ? TrackingNumberDateFormatDto.valueOf(rule.getDateFormat().name()) : null,
                rule.isUppercase()
        );
    }

    public static ShipmentDeliveryConfiguration toModelShipmentDeliveryConfiguration(final ShipmentDeliveryConfigurationDto configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentDeliveryConfiguration(
                configuration.requireDeliveryToken(),
                configuration.proofOfDelivery() != null
                        ? ProofOfDeliveryMode.valueOf(configuration.proofOfDelivery().name())
                        : null,
                configuration.maxDeliveryAttempts(),
                configuration.allowNeighbourDelivery(),
                configuration.allowSafePlaceDelivery(),
                configuration.redirectToPickupPointAfterFailure()
        );
    }

    private static ShipmentDeliveryConfigurationDto toDtoShipmentDeliveryConfiguration(final ShipmentDeliveryConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentDeliveryConfigurationDto(
                configuration.isRequireDeliveryToken(),
                configuration.getProofOfDelivery() != null
                        ? ProofOfDeliveryModeDto.valueOf(configuration.getProofOfDelivery().name())
                        : null,
                configuration.getMaxDeliveryAttempts(),
                configuration.isAllowNeighbourDelivery(),
                configuration.isAllowSafePlaceDelivery(),
                configuration.isRedirectToPickupPointAfterFailure()
        );
    }

    public static ShipmentReturnConfiguration toModelShipmentReturnConfiguration(final ShipmentReturnConfigurationDto configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentReturnConfiguration(
                configuration.allowReturns(),
                configuration.returnWindowDays(),
                configuration.autoReturnAfterFailedDelivery(),
                configuration.pickupPointStorageDays(),
                configuration.requireReturnReason()
        );
    }

    private static ShipmentReturnConfigurationDto toDtoShipmentReturnConfiguration(final ShipmentReturnConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentReturnConfigurationDto(
                configuration.isAllowReturns(),
                configuration.getReturnWindowDays(),
                configuration.isAutoReturnAfterFailedDelivery(),
                configuration.getPickupPointStorageDays(),
                configuration.isRequireReturnReason()
        );
    }

    public static ShipmentPaymentConfiguration toModelShipmentPaymentConfiguration(final ShipmentPaymentConfigurationDto configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentPaymentConfiguration(
                configuration.allowCashOnDelivery(),
                configuration.maxCashOnDeliveryAmount(),
                configuration.cashOnDeliveryPaymentMethod() != null
                        ? CashOnDeliveryPaymentMethod.valueOf(configuration.cashOnDeliveryPaymentMethod().name())
                        : null,
                configuration.requireInsuranceAboveValue()
        );
    }

    private static ShipmentPaymentConfigurationDto toDtoShipmentPaymentConfiguration(final ShipmentPaymentConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentPaymentConfigurationDto(
                configuration.isAllowCashOnDelivery(),
                configuration.getMaxCashOnDeliveryAmount(),
                configuration.getCashOnDeliveryPaymentMethod() != null
                        ? CashOnDeliveryPaymentMethodDto.valueOf(configuration.getCashOnDeliveryPaymentMethod().name())
                        : null,
                configuration.getRequireInsuranceAboveValue()
        );
    }

    public static ShipmentSlaConfiguration toModelShipmentSlaConfiguration(final ShipmentSlaConfigurationDto configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentSlaConfiguration(
                configuration.economySlaHours(),
                configuration.standardSlaHours(),
                configuration.expressSlaHours(),
                configuration.flagDelayedAfterHours(),
                configuration.requireExceptionNote(),
                configuration.escalateDelaysToDepartmentManager()
        );
    }

    private static ShipmentSlaConfigurationDto toDtoShipmentSlaConfiguration(final ShipmentSlaConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentSlaConfigurationDto(
                configuration.getEconomySlaHours(),
                configuration.getStandardSlaHours(),
                configuration.getExpressSlaHours(),
                configuration.getFlagDelayedAfterHours(),
                configuration.isRequireExceptionNote(),
                configuration.isEscalateDelaysToDepartmentManager()
        );
    }

    public static ShipmentDataRetentionConfiguration toModelShipmentDataRetentionConfiguration(final ShipmentDataRetentionConfigurationDto configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentDataRetentionConfiguration(
                configuration.anonymizeRecipientDataAfterDays(),
                configuration.archiveClosedShipmentsAfterDays(),
                configuration.deliveryPhotoRetentionDays()
        );
    }

    private static ShipmentDataRetentionConfigurationDto toDtoShipmentDataRetentionConfiguration(final ShipmentDataRetentionConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new ShipmentDataRetentionConfigurationDto(
                configuration.getAnonymizeRecipientDataAfterDays(),
                configuration.getArchiveClosedShipmentsAfterDays(),
                configuration.getDeliveryPhotoRetentionDays()
        );
    }

    public static ShipmentNotificationConfigurationDto mapShipmentNotificationConfiguration(final ShipmentNotificationConfiguration configuration) {
        return toDtoShipmentNotificationConfiguration(configuration);
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
