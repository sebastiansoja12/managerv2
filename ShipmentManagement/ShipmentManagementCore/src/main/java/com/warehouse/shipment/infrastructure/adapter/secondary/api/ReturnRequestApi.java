package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import java.util.List;

public record ReturnRequestApi(List<ReturnPackageRequestApi> requests) {
}
