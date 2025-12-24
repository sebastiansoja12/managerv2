package com.warehouse.shipment.domain.vo;

public record ShipmentConfiguration(boolean forceUpdate, boolean publishInRouteTracker, boolean publishInReturnManager,
                                    boolean customRerouteDepartment) {
}
