package com.warehouse.paypal.domain.model;

import com.warehouse.paypal.domain.enumeration.ParcelStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInformation {

    private Long parcelId;

    private double amount;

    private String paypalId;

    private String paymentUrl;

    private ParcelStatus parcelStatus;

    private String payerId;

    private String paymentId;

}