package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;

@Entity(name = "organisationstructure.OperatorConfigurationEntity")
@Table(name = "operator_configurations")
@Audited
public class OperatorConfigurationEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "operator_id", nullable = false))
    private OperatorId operatorId;

    @Column(name = "supports_domestic_shipping")
    private boolean supportsDomesticShipping;

    @Column(name = "supports_international_shipping")
    private boolean supportsInternationalShipping;

    @Column(name = "supports_express_shipping")
    private boolean supportsExpressShipping;

    @Column(name = "supports_same_day_delivery")
    private boolean supportsSameDayDelivery;

    @Column(name = "supports_cash_on_delivery")
    private boolean supportsCashOnDelivery;

    @Column(name = "supports_parcel_lockers")
    private boolean supportsParcelLockers;

    @Column(name = "supports_pickup_points")
    private boolean supportsPickupPoints;

    @Column(name = "supports_home_delivery")
    private boolean supportsHomeDelivery;

    @Column(name = "supports_saturday_delivery")
    private boolean supportsSaturdayDelivery;

    @Column(name = "supports_sunday_delivery")
    private boolean supportsSundayDelivery;

    @Column(name = "supports_return_shipments")
    private boolean supportsReturnShipments;

    @Column(name = "provides_tracking")
    private boolean providesTracking;

    @Column(name = "provides_insurance")
    private boolean providesInsurance;

    @Column(name = "max_weight")
    private double maxWeight;

    @Column(name = "min_weight")
    private double minWeight;

    @Column(name = "max_length")
    private double maxLength;

    @Column(name = "max_width")
    private double maxWidth;

    @Column(name = "max_height")
    private double maxHeight;

    @Column(name = "max_shipment_value")
    private double maxShipmentValue;

    @Column(name = "min_delivery_days")
    private int minDeliveryDays;

    @Column(name = "max_delivery_days")
    private int maxDeliveryDays;

    @Column(name = "express_delivery_days")
    private int expressDeliveryDays;

    @Column(name = "same_day_delivery_hours")
    private int sameDayDeliveryHours;

    @Column(name = "international_delivery_days")
    private int internationalDeliveryDays;

    public OperatorConfigurationEntity() {
    }

    public static OperatorConfigurationEntity fromModel(final OperatorId operatorId,
                                                        final OperatorConfiguration configuration) {
        final OperatorConfiguration source = configuration != null
                ? configuration
                : OperatorConfiguration.defaultFor(false, false, false);
        return fromConfiguration(operatorId, source);
    }

    public static OperatorConfigurationEntity defaultFor(final OperatorId operatorId,
                                                         final boolean supportsInternationalShipping,
                                                         final boolean supportsCashOnDelivery,
                                                         final boolean supportsLockers) {
        return fromConfiguration(operatorId, OperatorConfiguration.defaultFor(
                supportsInternationalShipping,
                supportsCashOnDelivery,
                supportsLockers
        ));
    }

    private static OperatorConfigurationEntity fromConfiguration(final OperatorId operatorId,
                                                                final OperatorConfiguration source) {
        final OperatorConfigurationEntity entity = new OperatorConfigurationEntity();
        entity.operatorId = operatorId;

        final OperatorConfiguration.ShippingCapabilities capabilities = source.getShippingCapabilities();
        if (capabilities != null) {
            entity.supportsDomesticShipping = capabilities.isSupportsDomesticShipping();
            entity.supportsInternationalShipping = capabilities.isSupportsInternationalShipping();
            entity.supportsExpressShipping = capabilities.isSupportsExpressShipping();
            entity.supportsSameDayDelivery = capabilities.isSupportsSameDayDelivery();
            entity.supportsCashOnDelivery = capabilities.isSupportsCashOnDelivery();
            entity.supportsParcelLockers = capabilities.isSupportsParcelLockers();
            entity.supportsPickupPoints = capabilities.isSupportsPickupPoints();
            entity.supportsHomeDelivery = capabilities.isSupportsHomeDelivery();
            entity.supportsSaturdayDelivery = capabilities.isSupportsSaturdayDelivery();
            entity.supportsSundayDelivery = capabilities.isSupportsSundayDelivery();
            entity.supportsReturnShipments = capabilities.isSupportsReturnShipments();
            entity.providesTracking = capabilities.isProvidesTracking();
            entity.providesInsurance = capabilities.isProvidesInsurance();
        }

        final OperatorConfiguration.ShipmentLimits limits = source.getShipmentLimits();
        if (limits != null) {
            entity.maxWeight = limits.getMaxWeight();
            entity.minWeight = limits.getMinWeight();
            entity.maxLength = limits.getMaxLength();
            entity.maxWidth = limits.getMaxWidth();
            entity.maxHeight = limits.getMaxHeight();
            entity.maxShipmentValue = limits.getMaxShipmentValue();
        }

        final OperatorConfiguration.DeliveryTimeConfiguration deliveryTime = source.getDeliveryTimeConfiguration();
        if (deliveryTime != null) {
            entity.minDeliveryDays = deliveryTime.getMinDeliveryDays();
            entity.maxDeliveryDays = deliveryTime.getMaxDeliveryDays();
            entity.expressDeliveryDays = deliveryTime.getExpressDeliveryDays();
            entity.sameDayDeliveryHours = deliveryTime.getSameDayDeliveryHours();
            entity.internationalDeliveryDays = deliveryTime.getInternationalDeliveryDays();
        }

        return entity;
    }

    public OperatorConfiguration toModel() {
        return new OperatorConfiguration(
                new OperatorConfiguration.ShippingCapabilities(
                        supportsDomesticShipping,
                        supportsInternationalShipping,
                        supportsExpressShipping,
                        supportsSameDayDelivery,
                        supportsCashOnDelivery,
                        supportsParcelLockers,
                        supportsPickupPoints,
                        supportsHomeDelivery,
                        supportsSaturdayDelivery,
                        supportsSundayDelivery,
                        supportsReturnShipments,
                        providesTracking,
                        providesInsurance
                ),
                new OperatorConfiguration.ShipmentLimits(
                        maxWeight,
                        minWeight,
                        maxLength,
                        maxWidth,
                        maxHeight,
                        maxShipmentValue
                ),
                new OperatorConfiguration.DeliveryTimeConfiguration(
                        minDeliveryDays,
                        maxDeliveryDays,
                        expressDeliveryDays,
                        sameDayDeliveryHours,
                        internationalDeliveryDays
                )
        );
    }
}
