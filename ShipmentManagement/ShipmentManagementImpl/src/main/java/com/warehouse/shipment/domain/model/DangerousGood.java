package com.warehouse.shipment.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import com.warehouse.commonassets.enumeration.CountryCode;

public final class DangerousGood {

    private static final Pattern UN_NUMBER_PATTERN = Pattern.compile("UN\\d{4}");
    private static final Set<String> PACKING_GROUPS = Set.of("I", "II", "III");
    private static final Set<String> QUANTITY_UNITS =
            Set.of("MILLIGRAM", "GRAM", "KILOGRAM", "TONNE", "OUNCE", "POUND", "LITRE");
    private static final Set<String> REGULATION_TYPES = Set.of("ADR", "IATA", "IMDG", "RID");
    private static final Set<String> TRANSPORT_MODES = Set.of("ROAD", "AIR", "SEA", "RAIL");
    private static final Set<String> TRANSPORT_MODES_REQUIRING_24H_CONTACT = Set.of("AIR", "SEA");
    private static final Set<String> REGULATIONS_REQUIRING_24H_CONTACT = Set.of("IATA", "IMDG");

    private final String unNumber;
    private final String properShippingName;
    private final String description;
    private final String hazardClass;
    private final String hazardDivision;
    private final String subsidiaryRisk;
    private final String packingGroup;
    private final BigDecimal quantity;
    private final String quantityUnit;
    private final Integer packageCount;
    private final String packagingType;
    private final boolean limitedQuantity;
    private final boolean exceptedQuantity;
    private final boolean environmentallyHazardous;
    private final boolean marinePollutant;
    private final String transportCategory;
    private final String tunnelRestrictionCode;
    private final BigDecimal flashPoint;
    private final String emergencyContact;
    private final String emergencyContact24h;
    private final String safetyDataSheetReference;
    private final String declarationDocumentReference;
    private final String regulationType;
    private final String transportMode;
    private final boolean flammable;
    private final boolean corrosive;
    private final boolean toxic;
    private final String hazardSymbols;
    private final String storageRequirements;
    private final String handlingInstructions;
    private final CountryCode countryOfOrigin;

    public DangerousGood(
            final String unNumber,
            final String properShippingName,
            final String description,
            final String hazardClass,
            final String hazardDivision,
            final String subsidiaryRisk,
            final String packingGroup,
            final BigDecimal quantity,
            final String quantityUnit,
            final Integer packageCount,
            final String packagingType,
            final boolean limitedQuantity,
            final boolean exceptedQuantity,
            final boolean environmentallyHazardous,
            final boolean marinePollutant,
            final String transportCategory,
            final String tunnelRestrictionCode,
            final BigDecimal flashPoint,
            final String emergencyContact,
            final String emergencyContact24h,
            final String safetyDataSheetReference,
            final String declarationDocumentReference,
            final String regulationType,
            final String transportMode,
            final boolean flammable,
            final boolean corrosive,
            final boolean toxic,
            final String hazardSymbols,
            final String storageRequirements,
            final String handlingInstructions,
            final CountryCode countryOfOrigin
    ) {
        this.unNumber = normalize(unNumber);
        this.properShippingName = normalize(properShippingName);
        this.description = normalize(description);
        this.hazardClass = normalize(hazardClass);
        this.hazardDivision = normalize(hazardDivision);
        this.subsidiaryRisk = normalize(subsidiaryRisk);
        this.packingGroup = normalizeUppercase(packingGroup);
        this.quantity = quantity;
        this.quantityUnit = normalizeUppercase(quantityUnit);
        this.packageCount = packageCount;
        this.packagingType = normalize(packagingType);
        this.limitedQuantity = limitedQuantity;
        this.exceptedQuantity = exceptedQuantity;
        this.environmentallyHazardous = environmentallyHazardous;
        this.marinePollutant = marinePollutant;
        this.transportCategory = normalize(transportCategory);
        this.tunnelRestrictionCode = normalize(tunnelRestrictionCode);
        this.flashPoint = flashPoint;
        this.emergencyContact = normalize(emergencyContact);
        this.emergencyContact24h = normalize(emergencyContact24h);
        this.safetyDataSheetReference = normalize(safetyDataSheetReference);
        this.declarationDocumentReference = normalize(declarationDocumentReference);
        this.regulationType = normalizeUppercase(regulationType);
        this.transportMode = normalizeUppercase(transportMode);
        this.flammable = flammable;
        this.corrosive = corrosive;
        this.toxic = toxic;
        this.hazardSymbols = normalize(hazardSymbols);
        this.storageRequirements = normalize(storageRequirements);
        this.handlingInstructions = normalize(handlingInstructions);
        this.countryOfOrigin = countryOfOrigin;
        validate();
    }

    private void validate() {
        final List<String> errors = new ArrayList<>();
        if (unNumber == null || !UN_NUMBER_PATTERN.matcher(unNumber).matches()) {
            errors.add("UN number must use the UN followed by four digits format, for example UN1203");
        }
        if (properShippingName == null) {
            errors.add("Proper shipping name is required");
        }
        if (hazardClass == null) {
            errors.add("Hazard class is required");
        }
        if (quantity == null || quantity.signum() <= 0) {
            errors.add("Dangerous goods quantity must be greater than zero");
        }
        if (quantityUnit == null) {
            errors.add("Dangerous goods quantity unit is required");
        } else if (!QUANTITY_UNITS.contains(quantityUnit.toUpperCase())) {
            errors.add("Dangerous goods quantity unit is not supported");
        }
        if (packageCount == null || packageCount <= 0) {
            errors.add("Dangerous goods package count must be greater than zero");
        }
        if (packagingType == null) {
            errors.add("Dangerous goods packaging type is required");
        }
        if (packingGroup != null && !PACKING_GROUPS.contains(packingGroup)) {
            errors.add("Packing group must be one of I, II or III");
        }
        if (regulationType == null) {
            errors.add("Dangerous goods regulation type is required");
        } else if (!REGULATION_TYPES.contains(regulationType)) {
            errors.add("Dangerous goods regulation type is not supported");
        }
        if (transportMode == null) {
            errors.add("Dangerous goods transport mode is required");
        } else if (!TRANSPORT_MODES.contains(transportMode)) {
            errors.add("Dangerous goods transport mode is not supported");
        }
        if (requires24HourContact() && emergencyContact24h == null) {
            errors.add("24-hour emergency contact is required for the selected transport mode or regulation");
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }
    }

    private boolean requires24HourContact() {
        return TRANSPORT_MODES_REQUIRING_24H_CONTACT.contains(transportMode)
                || REGULATIONS_REQUIRING_24H_CONTACT.contains(regulationType);
    }

    private static String normalize(final String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private static String normalizeUppercase(final String value) {
        final String normalized = normalize(value);
        return normalized == null ? null : normalized.toUpperCase();
    }

    public String getUnNumber() {
        return unNumber;
    }

    public String getProperShippingName() {
        return properShippingName;
    }

    public String getDescription() {
        return description;
    }

    public String getHazardClass() {
        return hazardClass;
    }

    public String getHazardDivision() {
        return hazardDivision;
    }

    public String getSubsidiaryRisk() {
        return subsidiaryRisk;
    }

    public String getPackingGroup() {
        return packingGroup;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public Integer getPackageCount() {
        return packageCount;
    }

    public String getPackagingType() {
        return packagingType;
    }

    public boolean isLimitedQuantity() {
        return limitedQuantity;
    }

    public boolean isExceptedQuantity() {
        return exceptedQuantity;
    }

    public boolean isEnvironmentallyHazardous() {
        return environmentallyHazardous;
    }

    public boolean isMarinePollutant() {
        return marinePollutant;
    }

    public String getTransportCategory() {
        return transportCategory;
    }

    public String getTunnelRestrictionCode() {
        return tunnelRestrictionCode;
    }

    public BigDecimal getFlashPoint() {
        return flashPoint;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public String getEmergencyContact24h() {
        return emergencyContact24h;
    }

    public String getSafetyDataSheetReference() {
        return safetyDataSheetReference;
    }

    public String getDeclarationDocumentReference() {
        return declarationDocumentReference;
    }

    public String getRegulationType() {
        return regulationType;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public boolean isFlammable() {
        return flammable;
    }

    public boolean isCorrosive() {
        return corrosive;
    }

    public boolean isToxic() {
        return toxic;
    }

    public String getHazardSymbols() {
        return hazardSymbols;
    }

    public String getStorageRequirements() {
        return storageRequirements;
    }

    public String getHandlingInstructions() {
        return handlingInstructions;
    }

    public CountryCode getCountryOfOrigin() {
        return countryOfOrigin;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DangerousGood that)) {
            return false;
        }
        return limitedQuantity == that.limitedQuantity
                && exceptedQuantity == that.exceptedQuantity
                && environmentallyHazardous == that.environmentallyHazardous
                && marinePollutant == that.marinePollutant
                && flammable == that.flammable
                && corrosive == that.corrosive
                && toxic == that.toxic
                && Objects.equals(unNumber, that.unNumber)
                && Objects.equals(properShippingName, that.properShippingName)
                && Objects.equals(description, that.description)
                && Objects.equals(hazardClass, that.hazardClass)
                && Objects.equals(hazardDivision, that.hazardDivision)
                && Objects.equals(subsidiaryRisk, that.subsidiaryRisk)
                && Objects.equals(packingGroup, that.packingGroup)
                && Objects.equals(quantity, that.quantity)
                && Objects.equals(quantityUnit, that.quantityUnit)
                && Objects.equals(packageCount, that.packageCount)
                && Objects.equals(packagingType, that.packagingType)
                && Objects.equals(transportCategory, that.transportCategory)
                && Objects.equals(tunnelRestrictionCode, that.tunnelRestrictionCode)
                && Objects.equals(flashPoint, that.flashPoint)
                && Objects.equals(emergencyContact, that.emergencyContact)
                && Objects.equals(emergencyContact24h, that.emergencyContact24h)
                && Objects.equals(safetyDataSheetReference, that.safetyDataSheetReference)
                && Objects.equals(declarationDocumentReference, that.declarationDocumentReference)
                && Objects.equals(regulationType, that.regulationType)
                && Objects.equals(transportMode, that.transportMode)
                && Objects.equals(hazardSymbols, that.hazardSymbols)
                && Objects.equals(storageRequirements, that.storageRequirements)
                && Objects.equals(handlingInstructions, that.handlingInstructions)
                && countryOfOrigin == that.countryOfOrigin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                unNumber, properShippingName, description, hazardClass, hazardDivision, subsidiaryRisk,
                packingGroup, quantity, quantityUnit, packageCount, packagingType, limitedQuantity,
                exceptedQuantity, environmentallyHazardous, marinePollutant, transportCategory,
                tunnelRestrictionCode, flashPoint, emergencyContact, emergencyContact24h,
                safetyDataSheetReference, declarationDocumentReference, regulationType, transportMode,
                flammable, corrosive, toxic, hazardSymbols, storageRequirements, handlingInstructions,
                countryOfOrigin
        );
    }
}
