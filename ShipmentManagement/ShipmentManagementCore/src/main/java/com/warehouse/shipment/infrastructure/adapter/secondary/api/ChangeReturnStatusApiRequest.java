package com.warehouse.shipment.infrastructure.adapter.secondary.api;

public record ChangeReturnStatusApiRequest(ReturnIdDto returnPackageId, String returnStatus) {
}
