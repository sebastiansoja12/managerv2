package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.util.List;

import com.warehouse.commonassets.enumeration.CountryCode;

public record DangerousGoodCreateRequestApi(ShipmentIdDto shipmentId,
                                            String name, String description, String classificationCode,
                                            List<String> hazardSymbols, String storageRequirements,
                                            String handlingInstructions, WeightDto weight,
                                            String packaging, boolean flammable, boolean corosive,
                                            boolean toxic, String emergencyContact, CountryCode countryOfOrigin,
                                            String safetyDataSheet) {
}
