package com.warehouse.deliverytoken.domain.vo;

import lombok.Value;

@Value
public class ProtectedDelivery {
    String protectionToken;
    String deliveryId;
}
