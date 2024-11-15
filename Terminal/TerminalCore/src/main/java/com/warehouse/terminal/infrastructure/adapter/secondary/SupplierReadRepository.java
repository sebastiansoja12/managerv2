package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.terminal.infrastructure.adapter.secondary.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("device.supplierReadRepository")
public interface SupplierReadRepository extends JpaRepository<SupplierEntity, Long> {

    Optional<SupplierEntity> findBySupplierCode(final String supplierCode);
}
