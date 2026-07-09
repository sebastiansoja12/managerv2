package com.warehouse.dangerousgood.infrastructure.adapter.primary.api;

import java.util.List;

import com.warehouse.commonassets.enumeration.CountryCode;

public record DangerousGoodCreateApiRequest(ShipmentIdDto shipmentId,
                                            String name, String description, String classificationCode,
                                            List<String> hazardSymbols, String storageRequirements,
                                            String handlingInstructions, WeightApi weight,
                                            String packaging, boolean flammable, boolean corosive,
                                            boolean toxic, String emergencyContact, CountryCode countryOfOrigin,
                                            String safetyDataSheet) {
}
