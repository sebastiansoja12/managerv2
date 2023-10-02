package com.warehouse.paypal.infrastructure.adapter.primary.dto;

import lombok.Data;

@Data
public class PaymentUpdateRequestDto {
    private ParcelId parcelId;
    private AmountDto amount;
    private PaypalIdDto paypalId;
    private PaypalUrlDto paypalUrl;
    private PaymentStatusDto parcelStatus;
    private PayerIdDto payerId;
    private PaymentIdDto paymentId;
}
