package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;

@Repository
public interface ShipmentReadRepository extends JpaRepository<ParcelEntity, Long> {

    Optional<ParcelEntity> findParcelEntityById(Long id);
}
