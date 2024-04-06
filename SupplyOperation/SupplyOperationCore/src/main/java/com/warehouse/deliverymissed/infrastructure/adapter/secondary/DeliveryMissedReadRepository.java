package com.warehouse.deliverymissed.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.DeliveryMissedEntity;

@Repository
public interface DeliveryMissedReadRepository extends JpaRepository<DeliveryMissedEntity, String> {
}
