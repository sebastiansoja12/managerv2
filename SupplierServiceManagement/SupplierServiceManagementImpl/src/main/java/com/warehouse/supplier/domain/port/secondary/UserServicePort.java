package com.warehouse.supplier.domain.port.secondary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;

public interface UserServicePort {
    UserId createUserForSupplier(final SupplierSnapshot snapshot);
}
