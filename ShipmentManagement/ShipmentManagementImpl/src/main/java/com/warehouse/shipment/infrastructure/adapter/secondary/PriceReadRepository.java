package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.PriceEntity;

public interface PriceReadRepository extends JpaRepository<PriceEntity, Long> {
    @Query("SELECT p FROM PriceEntity p WHERE p.shipmentSize = :shipmentSize AND p.price.currency = :currency")
    Optional<PriceEntity> findByShipmentSize(@Param("shipmentSize") final ShipmentSize shipmentSize,
                                             @Param("currency") final Currency currency);
}
