package com.warehouse.logisticsdelivery.application.port.primary.result;

import java.util.List;

public record DeliveryReadPage(List<DeliveryReadItem> items, int page, int size, boolean hasNext) {

    public DeliveryReadPage {
        items = List.copyOf(items);
    }
}
