package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.entity.DeliveryReturnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryReturnReadRepository extends JpaRepository<DeliveryReturnEntity, String> {
    Optional<DeliveryReturnEntity> findById(String id);
}
