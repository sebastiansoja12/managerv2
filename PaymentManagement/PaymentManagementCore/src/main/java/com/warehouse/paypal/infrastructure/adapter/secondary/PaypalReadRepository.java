package com.warehouse.paypal.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.paypal.infrastructure.adapter.secondary.entity.PaypalEntity;

@Repository
public interface PaypalReadRepository extends JpaRepository<PaypalEntity, Long> {

    Optional<PaypalEntity> findByPaymentId(String paymentId);
}
