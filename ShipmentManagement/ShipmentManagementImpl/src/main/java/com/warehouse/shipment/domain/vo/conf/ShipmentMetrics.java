package com.warehouse.shipment.domain.vo.conf;

import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.ShipmentCreateCommand;

public record ShipmentMetrics(double maxWeight,
                              double minWeight,
                              double maxLength,
                              double maxWidth,
                              double maxHeight,
                              double maxShipmentValue) {

    public static ShipmentMetrics from(final ShipmentCreateCommand createCommand) {
        final ShipmentSize shipmentSize = createCommand.getShipmentSize();

        return switch (shipmentSize) {
            case SMALL -> new ShipmentMetrics(20.0, 1.0, 20.0, 20.0, 20.0, 1.0);
            case TINY -> new ShipmentMetrics(40.0, 1.0, 40.0, 40.0, 40.0, 2);
            case MEDIUM -> new ShipmentMetrics(60.0, 1.0, 60.0, 60.0, 60.0, 3);
            case AVERAGE -> new ShipmentMetrics(80.0, 1.0, 80.0, 80.0, 80.0, 4);
            case BIG -> new ShipmentMetrics(100, 50, 100, 100, 100, 5);
            case CUSTOM, TEST -> throw new UnsupportedOperationException("Not supported");
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
