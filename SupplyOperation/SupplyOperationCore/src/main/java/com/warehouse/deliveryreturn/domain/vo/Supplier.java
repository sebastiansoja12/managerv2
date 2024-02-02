package com.warehouse.deliveryreturn.domain.vo;

import lombok.Value;

@Value
public class Supplier {
    String supplierCode;
    Boolean active;

    public boolean isActive() {
        return active;
    }
}
