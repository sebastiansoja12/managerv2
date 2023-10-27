package com.warehouse.deliverytoken.domain.model;

import lombok.Value;

@Value
public class ProtectedDelivery {
    String protectionToken;
    String deliveryId;
}
