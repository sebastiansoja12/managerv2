package com.warehouse.deliveryreturn.domain.model;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class DeliveryReturnTokenRequest {
    private List<DeliveryPackageRequest> requests;
    private Supplier supplier;
}
