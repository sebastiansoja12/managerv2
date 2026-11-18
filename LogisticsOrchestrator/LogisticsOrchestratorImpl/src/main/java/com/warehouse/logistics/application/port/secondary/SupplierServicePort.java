package com.warehouse.logistics.application.port.secondary;

import java.util.Optional;

import com.warehouse.logistics.domain.vo.SupplierAssignment;

public interface SupplierServicePort {

    Optional<SupplierAssignment> findCurrentSupplier();
}
