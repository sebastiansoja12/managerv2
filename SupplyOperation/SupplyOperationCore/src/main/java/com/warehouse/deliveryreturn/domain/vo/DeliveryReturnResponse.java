package com.warehouse.deliveryreturn.domain.vo;


import java.util.List;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class DeliveryReturnResponse {
    List<DeliveryReturnResponseDetails> deliveryReturnResponses;
}
