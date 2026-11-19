package com.warehouse.supplier.application.port.secondary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;

public interface UserServicePort {
    UserId createUserForSupplier(final SupplierSnapshot snapshot);
}
