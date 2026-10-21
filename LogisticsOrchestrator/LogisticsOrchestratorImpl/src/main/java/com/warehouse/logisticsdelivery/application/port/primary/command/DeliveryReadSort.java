package com.warehouse.logisticsdelivery.application.port.primary.command;

public record DeliveryReadSort(DeliveryReadSortField field, boolean ascending) {

    public static DeliveryReadSort newestFirst() {
        return new DeliveryReadSort(DeliveryReadSortField.CREATED, false);
    }

    public DeliveryReadSort {
        field = field == null ? DeliveryReadSortField.CREATED : field;
    }
}
