package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.billing.api.dto.DeliveryOptionDto;
import com.warehouse.billing.api.dto.PriceQuoteDto;
import com.warehouse.billing.api.dto.PriceQuoteRequestDto;
import com.warehouse.billing.api.dto.ServiceLevelDto;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import com.warehouse.shipment.domain.vo.PricedShipment;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;

import java.math.BigDecimal;

public class ShipmentPricingRequestMapper {

    public PriceQuoteRequestDto map(final ShipmentCreateCommand command, final ShipmentServiceLevel serviceLevel) {
        final Dimensions dimensions = command.getDimensions();
        final Money declaredValue = command.getDeclaredValue();
        return new PriceQuoteRequestDto(
                toCentimeters(dimensions.length(), dimensions.unit()),
                toCentimeters(dimensions.width(), dimensions.unit()),
                toCentimeters(dimensions.height(), dimensions.unit()),
                toKilograms(command.getWeight()),
                ServiceLevelDto.valueOf(serviceLevel.name()),
                DeliveryOptionDto.valueOf(command.getDeliveryMethod().name()),
                declaredValue == null ? null : declaredValue.getAmount(),
                declaredValue == null ? null : declaredValue.getCurrency());
    }

    public PricedShipment map(final PriceQuoteDto quote) {
        return new PricedShipment(new PriceQuoteId(quote.quoteId().value()),
                new Money(quote.gross(), quote.currency()));
    }

    private BigDecimal toCentimeters(final BigDecimal value, final LengthUnit unit) {
        return switch (unit) {
            case MM -> value.movePointLeft(1);
            case CM -> value;
            case M -> value.movePointRight(2);
        };
    }

    private BigDecimal toKilograms(final Weight weight) {
        return switch (weight.unit()) {
            case G -> weight.value().movePointLeft(3);
            case KG -> weight.value();
        };
    }
}
