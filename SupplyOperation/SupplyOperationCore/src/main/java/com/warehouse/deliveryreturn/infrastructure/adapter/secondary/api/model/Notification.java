package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.model;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Notification {
    String subject;
    String recipient;
    String body;
}
