package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.infrastructure.adapter.secondary.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("delivery.supplierReadRepository")
public interface SupplierReadRepository extends JpaRepository<SupplierEntity, String> {
    Optional<SupplierEntity> findBySupplierCode(final String supplierCode);
}
