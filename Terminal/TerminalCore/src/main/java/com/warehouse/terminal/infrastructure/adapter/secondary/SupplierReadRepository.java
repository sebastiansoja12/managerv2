package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.terminal.infrastructure.adapter.secondary.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierReadRepository extends JpaRepository<SupplierEntity, Long> {

    Optional<SupplierEntity> findBySupplierCode(final String supplierCode);
}
