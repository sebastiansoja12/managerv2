package com.warehouse.supplier.api.dto;

import java.util.Set;

public record SupplierDeliveryAreaDto(String areaName,
                                      String city,
                                      String district,
                                      String municipality,
                                      String region,
                                      String country,
                                      Set<String> postalCodes) {
}
