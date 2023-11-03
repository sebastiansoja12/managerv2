package com.warehouse.deliveryreturn.domain.model;


import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnInformation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryPackageRequest {
    DeliveryReturnInformation delivery;
}
