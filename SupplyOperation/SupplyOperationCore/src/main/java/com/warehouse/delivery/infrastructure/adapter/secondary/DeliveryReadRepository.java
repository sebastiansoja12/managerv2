package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.infrastructure.adapter.secondary.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryReadRepository extends JpaRepository<DeliveryEntity, UUID> {
}
