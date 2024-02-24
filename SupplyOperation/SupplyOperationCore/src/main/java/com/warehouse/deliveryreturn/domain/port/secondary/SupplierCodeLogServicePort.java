package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.deliveryreturn.domain.vo.SupplierCodeRequest;

public interface SupplierCodeLogServicePort {
    void saveSupplierCode(SupplierCodeRequest request);
}
