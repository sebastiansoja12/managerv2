package com.warehouse.paypal.infrastructure.adapter.secondary.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "payment")
@Entity(name = "payment.PaypalEntity")
public class PaypalEntity {

    @Id
    @GeneratedValue()
    private Long id;

    private double amount;

    private String paymentId;

    private String parcelStatus;

    private String paymentUrl;

    private Long parcelId;
}