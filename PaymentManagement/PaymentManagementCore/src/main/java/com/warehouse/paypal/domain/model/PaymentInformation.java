package com.warehouse.paypal.domain.model;

import java.math.BigDecimal;

import com.warehouse.paypal.infrastructure.adapter.secondary.enumeration.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInformation {

	private Long parcelId;
	private int amount;
	private String paypalId;
	private String paymentUrl;
	private PaymentStatus paymentStatus;
	private String payerId;
	private String paymentId;
	private BigDecimal price;
	private String createTime;
	private String paymentMethod;
	private String failureReason;

}