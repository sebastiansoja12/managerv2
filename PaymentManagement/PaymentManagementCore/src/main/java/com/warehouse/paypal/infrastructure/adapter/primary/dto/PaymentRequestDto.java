package com.warehouse.paypal.infrastructure.adapter.primary.dto;


import lombok.Data;

@Data
public class PaymentRequestDto {
    private ParcelId parcelId;

    private PriceDto price;
}
