package com.warehouse.mcp.application.port.secondary;

import java.util.List;
import java.util.Optional;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.mcp.domain.vo.Courier;
import com.warehouse.mcp.domain.vo.CourierChange;
import com.warehouse.mcp.domain.vo.CourierDraft;

public interface CourierServicePort {
    List<Courier> findCouriers();
    Optional<SupplierId> findCourierId(final SupplierCode courierCode);
    SupplierCode create(final CourierDraft courier);
    void update(final CourierChange change);
    void activate(final SupplierCode courierCode);
    void deactivate(final SupplierCode courierCode);
}
