package com.warehouse.paypal.infrastructure.adapter.secondary;

import com.warehouse.paypal.infrastructure.adapter.secondary.entity.PaypalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaypalReadRepository extends JpaRepository<PaypalEntity, String> {
    Optional<PaypalEntity> findById(String id);
}
