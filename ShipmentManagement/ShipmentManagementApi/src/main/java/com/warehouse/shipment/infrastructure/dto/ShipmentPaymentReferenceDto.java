package com.warehouse.shipment.infrastructure.dto;

import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.shipment.api.event.snapshot.PaymentStateDto;

public record ShipmentPaymentReferenceDto(OperatorId operatorId,
                                          PaymentStateDto paymentState,
                                          PriceQuoteId priceQuoteId,
                                          BillingCustomerId billingCustomerId) {
}
