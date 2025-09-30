package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.enumeration.Country;

import java.util.List;

public record DangerousGoodApi(String name, String description, String classificationCode,
                               List<String> hazardSymbols, String storageRequirements,
                               String handlingInstructions, WeightDto weight,
                               String packaging, boolean flammable, boolean corosive,
                               boolean toxic, String emergencyContact, Country countryOfOrigin,
                               String safetyDataSheet) {
}
