package com.warehouse.supplier.infrastructure.adapter.primary.dto;

import java.util.Set;

public record DeliveryAreaApi(
        String areaName,
        String city,
        String district,
        String municipality,
        String region,
        String country,
        Set<String> postalCodes
) {
}
