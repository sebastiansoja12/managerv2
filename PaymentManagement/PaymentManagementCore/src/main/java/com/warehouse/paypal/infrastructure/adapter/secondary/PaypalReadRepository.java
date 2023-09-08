package com.warehouse.paypal.infrastructure.adapter.secondary;

import com.warehouse.paypal.infrastructure.adapter.secondary.entity.PaypalEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaypalReadRepository extends MongoRepository<PaypalEntity, Long> {
    Optional<PaypalEntity> findById(Long id);
}
