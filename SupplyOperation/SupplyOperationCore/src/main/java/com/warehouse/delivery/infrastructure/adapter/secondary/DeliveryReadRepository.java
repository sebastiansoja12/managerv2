package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.infrastructure.adapter.secondary.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryReadRepository extends JpaRepository<DeliveryEntity, UUID> {
}
