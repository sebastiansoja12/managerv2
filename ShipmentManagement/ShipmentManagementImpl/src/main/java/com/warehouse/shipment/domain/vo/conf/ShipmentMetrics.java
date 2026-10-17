package com.warehouse.shipment.domain.vo.conf;

import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;

public record ShipmentMetrics(double maxWeight,
                              double minWeight,
                              double maxLength,
                              double maxWidth,
                              double maxHeight,
                              double maxShipmentValue) {

    public static ShipmentMetrics from(final Dimensions dimensions, final Weight weight,
                                       final Money declaredValue) {
        final double lengthCm = toCentimeters(dimensions.length().doubleValue(), dimensions.unit());
        final double widthCm = toCentimeters(dimensions.width().doubleValue(), dimensions.unit());
        final double heightCm = toCentimeters(dimensions.height().doubleValue(), dimensions.unit());
        final double weightKg = weight.unit() == WeightUnit.G
                ? weight.value().doubleValue() / 1000.0
                : weight.value().doubleValue();
        final double declaredAmount = declaredValue == null || declaredValue.getAmount() == null
                ? 0.0
                : declaredValue.getAmount().doubleValue();
        return new ShipmentMetrics(weightKg, weightKg, lengthCm, widthCm, heightCm, declaredAmount);
    }

    private static double toCentimeters(final double value, final LengthUnit unit) {
        return switch (unit) {
            case MM -> value / 10.0;
            case CM -> value;
            case M -> value * 100.0;
        };
    }

    public Result<Void, String> validateBasedOnLimits(final ShipmentLimits shipmentLimits) {
        if (maxWeight > shipmentLimits.maxWeight()) {
            return Result.failure("Max weight limit exceeded");
        }

        if (minWeight < shipmentLimits.minWeight()) {
            return Result.failure("Min weight limit exceeded");
        }

        if (maxLength > shipmentLimits.maxLength()) {
            return Result.failure("Max length limit exceeded");
        }

        if (maxWidth > shipmentLimits.maxWidth()) {
            return Result.failure("Max width limit exceeded");
        }

        if (maxHeight > shipmentLimits.maxHeight()) {
            return Result.failure("Max height limit exceeded");
        }

        if (maxShipmentValue > shipmentLimits.maxShipmentValue()) {
            return Result.failure("Max shipment value limit exceeded");
        }

        return Result.success();
    }
}
