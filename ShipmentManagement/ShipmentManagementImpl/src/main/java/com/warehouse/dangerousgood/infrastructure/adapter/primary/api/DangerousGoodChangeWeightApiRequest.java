package com.warehouse.dangerousgood.infrastructure.adapter.primary.api;

public record DangerousGoodChangeWeightApiRequest(ShipmentIdDto shipmentId,
                                                  DangerousGoodIdApi dangerousGoodId,
                                                  WeightApi weight) {
}
