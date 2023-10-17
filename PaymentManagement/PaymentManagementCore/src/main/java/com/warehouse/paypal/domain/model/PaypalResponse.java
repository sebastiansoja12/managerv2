package com.warehouse.paypal.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaypalResponse {
	String id;
	String intent;
	Payer payer;
	String state;
	List<Links> links;
	List<Transaction> transactions;
	String createTime;
	String paymentMethod;
	String failureReason;
}
