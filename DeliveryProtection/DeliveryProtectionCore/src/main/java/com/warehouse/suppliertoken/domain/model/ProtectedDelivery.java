package com.warehouse.suppliertoken.domain.model;

import lombok.Value;

@Value
public class ProtectedDelivery {
    String protectionToken;
    String deliveryId;
}
