package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DangerousGoodSnapshot(
        String unNumber,
        String properShippingName,
        String description,
        String hazardClass,
        String hazardDivision,
        String subsidiaryRisk,
        String packingGroup,
        BigDecimal quantity,
        String quantityUnit,
        Integer packageCount,
        String packagingType,
        boolean limitedQuantity,
        boolean exceptedQuantity,
        boolean environmentallyHazardous,
        boolean marinePollutant,
        String transportCategory,
        String tunnelRestrictionCode,
        BigDecimal flashPoint,
        String emergencyContact,
        String emergencyContact24h,
        String safetyDataSheetReference,
        String declarationDocumentReference,
        String regulationType,
        String transportMode,
        boolean flammable,
        boolean corrosive,
        boolean toxic,
        String hazardSymbols,
        String storageRequirements,
        String handlingInstructions,
        CountryCode countryOfOrigin
) {
}
