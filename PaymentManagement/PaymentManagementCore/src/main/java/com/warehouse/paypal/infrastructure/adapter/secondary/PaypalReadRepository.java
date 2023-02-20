package com.warehouse.paypal.infrastructure.adapter.secondary;

import com.warehouse.paypal.infrastructure.adapter.secondary.entity.PaypalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaypalReadRepository extends JpaRepository<PaypalEntity, Long> {
    PaypalEntity findByPaymentId(String paymentId);
}
