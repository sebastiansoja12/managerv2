package com.warehouse.paypal.infrastructure.adapter.secondary.entity;

import com.warehouse.paypal.infrastructure.adapter.secondary.enumeration.PaymentMethod;
import com.warehouse.paypal.infrastructure.adapter.secondary.enumeration.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment")
@Builder
@Entity(name = "payment.PaypalEntity")
public class PaypalEntity {

	@Id
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "amount", nullable = false)
	private double amount;

	@Column(name = "payment_status", nullable = false)
	private PaymentStatus paymentStatus;

	@Column(name = "payment_method", nullable = false)
	private PaymentMethod paymentMethod;

	@Column(name = "paymentUrl", nullable = false)
	private String paymentUrl;

	@Column(name = "parcelId", nullable = false)
	private Long parcelId;
}