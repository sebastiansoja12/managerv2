package com.warehouse.logisticsdelivery.application.port.primary.command;

public record SearchDeliveriesCommand(int page, int size, DeliveryReadFilter filter, DeliveryReadSort sort) {

    public SearchDeliveriesCommand {
        filter = filter == null ? DeliveryReadFilter.byShipments(null) : filter;
        sort = sort == null ? DeliveryReadSort.newestFirst() : sort;
    }
}
