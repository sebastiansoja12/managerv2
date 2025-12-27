package com.warehouse.shipment.infrastructure.adapter.primary.api;

public record ShipmentConfigurationApi(boolean forceUpdate, boolean publishInRouteTracker, boolean publishInReturnManager,
                                       boolean customRerouteDepartment) {
}
