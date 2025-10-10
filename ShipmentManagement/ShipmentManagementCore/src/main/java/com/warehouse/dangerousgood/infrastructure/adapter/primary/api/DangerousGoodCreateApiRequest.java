package com.warehouse.dangerousgood.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.enumeration.Country;

import java.util.List;

public record DangerousGoodCreateApiRequest(ShipmentIdDto shipmentId,
                                            String name, String description, String classificationCode,
                                            List<String> hazardSymbols, String storageRequirements,
                                            String handlingInstructions, WeightApi weight,
                                            String packaging, boolean flammable, boolean corosive,
                                            boolean toxic, String emergencyContact, Country countryOfOrigin,
                                            String safetyDataSheet) {
}
