package com.warehouse.supplierconfiguration.application.port.secondary;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationRevision;

import java.util.List;

public interface SupplierConfigurationHistoryRepository {
    void append(final SupplierConfigurationRevision revision);
    List<SupplierConfigurationRevision> findBySupplierId(final SupplierId supplierId);
}
