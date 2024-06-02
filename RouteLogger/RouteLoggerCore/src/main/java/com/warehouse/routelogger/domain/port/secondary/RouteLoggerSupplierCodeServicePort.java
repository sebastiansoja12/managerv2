package com.warehouse.routelogger.domain.port.secondary;

import com.warehouse.routelogger.domain.model.SupplierCodeRequest;

public interface RouteLoggerSupplierCodeServicePort {
    void logSupplierCode(SupplierCodeRequest supplierCodeRequest);
}
