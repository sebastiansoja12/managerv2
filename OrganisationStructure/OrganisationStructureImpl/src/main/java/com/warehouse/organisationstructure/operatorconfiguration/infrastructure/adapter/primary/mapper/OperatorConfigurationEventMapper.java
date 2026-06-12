package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary.mapper;

import com.warehouse.organisationstructure.api.dto.DeliveryTimeConfigurationDto;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLimitsDto;
import com.warehouse.organisationstructure.api.dto.ShippingCapabilitiesDto;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

public final class OperatorConfigurationEventMapper {

    private OperatorConfigurationEventMapper() {
    }

    public static OperatorConfiguration toModel(final OperatorConfigurationDto configuration) {
        if (configuration == null) {
            return null;
        }
        return new OperatorConfiguration(
                toModel(configuration.shippingCapabilities()),
                toModel(configuration.shipmentLimits()),
                toModel(configuration.deliveryTimeConfiguration())
        );
    }

    private static OperatorConfiguration.ShippingCapabilities toModel(
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

    private static OperatorConfiguration.ShipmentLimits toModel(final ShipmentLimitsDto shipmentLimits) {
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

    private static OperatorConfiguration.DeliveryTimeConfiguration toModel(
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
}
