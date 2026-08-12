package com.warehouse.shipment.infrastructure.adapter.primary.api;

public record ShipmentConfigurationApi(boolean forceUpdate, boolean publishInReturnManager,
                                       boolean customRerouteDepartment) {
}
