package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.SupplierId;
import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;

public interface SupplierReadRepository extends JpaRepository<SupplierEntity, SupplierId> {
}

