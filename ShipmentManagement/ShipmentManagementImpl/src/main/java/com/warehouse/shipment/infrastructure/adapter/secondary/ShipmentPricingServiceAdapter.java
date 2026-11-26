package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.billing.api.PriceQuoteApiService;
import com.warehouse.billing.api.exception.PriceListNotFoundApiException;
import com.warehouse.billing.api.exception.ShipmentNotPriceableApiException;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.secondary.ShipmentPricingServicePort;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.PricedShipment;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentPricingRequestMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentPricingServiceAdapter implements ShipmentPricingServicePort {

    private final PriceQuoteApiService priceQuoteApiService;

    private final ShipmentPricingRequestMapper shipmentPricingRequestMapper;

    public ShipmentPricingServiceAdapter(final PriceQuoteApiService priceQuoteApiService,
                                         final ShipmentPricingRequestMapper shipmentPricingRequestMapper) {
        this.priceQuoteApiService = priceQuoteApiService;
        this.shipmentPricingRequestMapper = shipmentPricingRequestMapper;
    }

    @Override
    public Result<PricedShipment, ErrorCode> quote(final ShipmentCreateCommand command,
                                                   final ShipmentServiceLevel serviceLevel) {
        try {
            return Result.success(this.shipmentPricingRequestMapper.map(
                    this.priceQuoteApiService.quote(this.shipmentPricingRequestMapper.map(command, serviceLevel))));
        } catch (final ShipmentNotPriceableApiException exception) {
            return Result.failure(ErrorCode.SHIPMENT_NOT_PRICEABLE);
        } catch (final PriceListNotFoundApiException exception) {
            log.warn("Shipment cannot be priced because the current operator has no price list");
            return Result.failure(ErrorCode.SHIPMENT_NOT_PRICEABLE);
        }
    }
}
