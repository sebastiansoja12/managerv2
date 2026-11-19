package com.warehouse.supplier.domain.vo;

import java.util.Set;

public record DeliveryArea(String areaName,
                           String city,
                           String district,
                           String municipality,
                           String region,
                           String country,
                           Set<String> postalCodes) {

    public boolean coversPostalCode(final String postalCode) {
        return postalCodes != null && postalCodes.contains(postalCode);
    }
}
