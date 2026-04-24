package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.infrastructure.adapter.secondary.entity.TrackingSequenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingSequenceReadRepository extends JpaRepository<TrackingSequenceEntity, String> {
}
