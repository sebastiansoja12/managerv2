package com.warehouse.billing.payment.infrastructure.adapter.secondary;

import com.warehouse.billing.payment.application.port.secondary.PaymentQuoteLookupServicePort;
import com.warehouse.billing.payment.domain.vo.PaymentQuoteReference;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.api.event.snapshot.PaymentStateDto;
import com.warehouse.shipment.infrastructure.ShipmentApiService;

import java.util.Optional;

public class PaymentQuoteLookupServiceAdapter implements PaymentQuoteLookupServicePort {

    private final ShipmentApiService shipmentApiService;

    public PaymentQuoteLookupServiceAdapter(final ShipmentApiService shipmentApiService) {
        this.shipmentApiService = shipmentApiService;
    }

    @Override
    public Optional<PaymentQuoteReference> find(final TrackingNumber trackingNumber) {
        return this.shipmentApiService.findPaymentReference(trackingNumber)
                .map(shipment -> new PaymentQuoteReference(shipment.operatorId(),
                        shipment.paymentState() != null
                                && !PaymentStateDto.NOT_REQUIRED.equals(shipment.paymentState()),
                        shipment.priceQuoteId(), shipment.billingCustomerId()));
    }
}
