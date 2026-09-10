package com.warehouse.shipment.application.event.snapshot;

import java.math.BigDecimal;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.domain.model.DangerousGood;

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

    public static DangerousGoodSnapshot from(final DangerousGood dangerousGood) {
        if (dangerousGood == null) {
            return null;
        }
        return new DangerousGoodSnapshot(
                dangerousGood.getUnNumber(),
                dangerousGood.getProperShippingName(),
                dangerousGood.getDescription(),
                dangerousGood.getHazardClass(),
                dangerousGood.getHazardDivision(),
                dangerousGood.getSubsidiaryRisk(),
                dangerousGood.getPackingGroup(),
                dangerousGood.getQuantity(),
                dangerousGood.getQuantityUnit(),
                dangerousGood.getPackageCount(),
                dangerousGood.getPackagingType(),
                dangerousGood.isLimitedQuantity(),
                dangerousGood.isExceptedQuantity(),
                dangerousGood.isEnvironmentallyHazardous(),
                dangerousGood.isMarinePollutant(),
                dangerousGood.getTransportCategory(),
                dangerousGood.getTunnelRestrictionCode(),
                dangerousGood.getFlashPoint(),
                dangerousGood.getEmergencyContact(),
                dangerousGood.getEmergencyContact24h(),
                dangerousGood.getSafetyDataSheetReference(),
                dangerousGood.getDeclarationDocumentReference(),
                dangerousGood.getRegulationType(),
                dangerousGood.getTransportMode(),
                dangerousGood.isFlammable(),
                dangerousGood.isCorrosive(),
                dangerousGood.isToxic(),
                dangerousGood.getHazardSymbols(),
                dangerousGood.getStorageRequirements(),
                dangerousGood.getHandlingInstructions(),
                dangerousGood.getCountryOfOrigin()
        );
    }
}
