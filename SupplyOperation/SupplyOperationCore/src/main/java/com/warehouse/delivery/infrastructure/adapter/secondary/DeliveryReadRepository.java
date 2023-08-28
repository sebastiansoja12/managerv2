package com.warehouse.delivery.infrastructure.adapter.secondary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.delivery.infrastructure.adapter.secondary.entity.DeliveryEntity;

@Repository
public interface DeliveryReadRepository extends JpaRepository<DeliveryEntity, String> {
    List<DeliveryEntity> findBySupplierCode(String supplierCode);
}
