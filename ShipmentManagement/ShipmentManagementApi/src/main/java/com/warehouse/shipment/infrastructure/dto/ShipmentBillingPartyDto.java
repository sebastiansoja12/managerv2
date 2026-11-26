package com.warehouse.shipment.infrastructure.dto;

import com.warehouse.commonassets.identificator.BillingCustomerId;

public record ShipmentBillingPartyDto(ShipmentPartyDto sender,
                                      BillingCustomerId billingCustomerId) {
}
