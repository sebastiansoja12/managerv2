package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.warehouse.commonassets.enumeration.CountryCode;

public record DangerousGoodApi(
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
        @JsonAlias("corosive") boolean corrosive,
        boolean toxic,
        String hazardSymbols,
        String storageRequirements,
        String handlingInstructions,
        CountryCode countryOfOrigin
) {
}
