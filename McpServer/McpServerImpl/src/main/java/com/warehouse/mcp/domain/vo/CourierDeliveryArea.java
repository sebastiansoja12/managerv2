package com.warehouse.mcp.domain.vo;

import java.util.Set;

public record CourierDeliveryArea(String areaName,
                                  String city,
                                  String district,
                                  String municipality,
                                  String region,
                                  String country,
                                  Set<String> postalCodes) {
}
