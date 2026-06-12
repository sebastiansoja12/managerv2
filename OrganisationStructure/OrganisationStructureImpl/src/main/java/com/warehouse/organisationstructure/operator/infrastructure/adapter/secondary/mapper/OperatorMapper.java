package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.dto.DeliveryTimeConfigurationDto;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.OperatorDto;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import com.warehouse.organisationstructure.api.dto.OperatorStatusDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLimitsDto;
import com.warehouse.organisationstructure.api.dto.ShippingCapabilitiesDto;
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

    public static OperatorConfigurationDto toDtoConfiguration(final OperatorConfiguration configuration) {
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
            final ShippingCapabilitiesDto shippingCapabilities) {
        if (shippingCapabilities == null) {
            return null;
        }
        return new OperatorConfiguration.ShippingCapabilities(
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

    private static OperatorConfiguration.ShipmentLimits toModelShipmentLimits(
            final ShipmentLimitsDto shipmentLimits) {
        if (shipmentLimits == null) {
            return null;
        }
        return new OperatorConfiguration.ShipmentLimits(
                shipmentLimits.maxWeight(),
                shipmentLimits.minWeight(),
                shipmentLimits.maxLength(),
                shipmentLimits.maxWidth(),
                shipmentLimits.maxHeight(),
                shipmentLimits.maxShipmentValue()
        );
    }

    private static OperatorConfiguration.DeliveryTimeConfiguration toModelDeliveryTimeConfiguration(
            final DeliveryTimeConfigurationDto deliveryTimeConfiguration) {
        if (deliveryTimeConfiguration == null) {
            return null;
        }
        return new OperatorConfiguration.DeliveryTimeConfiguration(
                deliveryTimeConfiguration.minDeliveryDays(),
                deliveryTimeConfiguration.maxDeliveryDays(),
                deliveryTimeConfiguration.expressDeliveryDays(),
                deliveryTimeConfiguration.sameDayDeliveryHours(),
                deliveryTimeConfiguration.internationalDeliveryDays()
        );
    }

    private static ShippingCapabilitiesDto toDtoShippingCapabilities(
            final OperatorConfiguration.ShippingCapabilities capabilities) {
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
            final OperatorConfiguration.ShipmentLimits limits) {
        if (limits == null) {
            return null;
        }
        return new ShipmentLimitsDto(
                limits.getMaxWeight(),
                limits.getMinWeight(),
                limits.getMaxLength(),
                limits.getMaxWidth(),
                limits.getMaxHeight(),
                limits.getMaxShipmentValue()
        );
    }

    private static DeliveryTimeConfigurationDto toDtoDeliveryTimeConfiguration(
            final OperatorConfiguration.DeliveryTimeConfiguration configuration) {
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
