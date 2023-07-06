package com.warehouse.paypal.infrastructure.adapter.secondary.entity;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double amount;

    private String paymentId;

    private String parcelStatus;

    private String paymentUrl;

    private Long parcelId;
}