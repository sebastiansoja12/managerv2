package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.OperatorDto;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import com.warehouse.organisationstructure.api.dto.OperatorStatusDto;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.domain.model.OperatorStatus;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.entity.OperatorEntity;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

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
                toModelShipmentLimits(dto.shipmentLimits()),
                toModelDeliveryTimeConfiguration(dto.deliveryTimeConfiguration())
        );
    }

    private static OperatorConfigurationDto toDtoConfiguration(final OperatorConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new OperatorConfigurationDto(
                toDtoShippingCapabilities(configuration.getShippingCapabilities()),
                toDtoShipmentLimits(configuration.getShipmentLimits()),
                toDtoDeliveryTimeConfiguration(configuration.getDeliveryTimeConfiguration())
        );
    }

    private static OperatorConfiguration.ShippingCapabilities toModelShippingCapabilities(
            final OperatorConfigurationDto.ShippingCapabilitiesDto dto) {
        if (dto == null) {
            return null;
        }
        return new OperatorConfiguration.ShippingCapabilities(
                dto.supportsDomesticShipping(),
                dto.supportsInternationalShipping(),
                dto.supportsExpressShipping(),
                dto.supportsSameDayDelivery(),
                dto.supportsCashOnDelivery(),
                dto.supportsParcelLockers(),
                dto.supportsPickupPoints(),
                dto.supportsHomeDelivery(),
                dto.supportsSaturdayDelivery(),
                dto.supportsSundayDelivery(),
                dto.supportsReturnShipments(),
                dto.providesTracking(),
                dto.providesInsurance()
        );
    }

    private static OperatorConfiguration.ShipmentLimits toModelShipmentLimits(
            final OperatorConfigurationDto.ShipmentLimitsDto dto) {
        if (dto == null) {
            return null;
        }
        return new OperatorConfiguration.ShipmentLimits(
                dto.maxWeight(),
                dto.minWeight(),
                dto.maxLength(),
                dto.maxWidth(),
                dto.maxHeight(),
                dto.maxShipmentValue()
        );
    }

    private static OperatorConfiguration.DeliveryTimeConfiguration toModelDeliveryTimeConfiguration(
            final OperatorConfigurationDto.DeliveryTimeConfigurationDto dto) {
        if (dto == null) {
            return null;
        }
        return new OperatorConfiguration.DeliveryTimeConfiguration(
                dto.minDeliveryDays(),
                dto.maxDeliveryDays(),
                dto.expressDeliveryDays(),
                dto.sameDayDeliveryHours(),
                dto.internationalDeliveryDays()
        );
    }

    private static OperatorConfigurationDto.ShippingCapabilitiesDto toDtoShippingCapabilities(
            final OperatorConfiguration.ShippingCapabilities capabilities) {
        if (capabilities == null) {
            return null;
        }
        return new OperatorConfigurationDto.ShippingCapabilitiesDto(
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

    private static OperatorConfigurationDto.ShipmentLimitsDto toDtoShipmentLimits(
            final OperatorConfiguration.ShipmentLimits limits) {
        if (limits == null) {
            return null;
        }
        return new OperatorConfigurationDto.ShipmentLimitsDto(
                limits.getMaxWeight(),
                limits.getMinWeight(),
                limits.getMaxLength(),
                limits.getMaxWidth(),
                limits.getMaxHeight(),
                limits.getMaxShipmentValue()
        );
    }

    private static OperatorConfigurationDto.DeliveryTimeConfigurationDto toDtoDeliveryTimeConfiguration(
            final OperatorConfiguration.DeliveryTimeConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        return new OperatorConfigurationDto.DeliveryTimeConfigurationDto(
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
