package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import java.math.BigDecimal;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.domain.model.DangerousGood;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class DangerousGoodEmbeddable {

    @Column(name = "dangerous_good_un_number")
    private String unNumber;
    @Column(name = "dangerous_good_proper_shipping_name")
    private String properShippingName;
    @Column(name = "dangerous_good_description", length = 1000)
    private String description;
    @Column(name = "dangerous_good_hazard_class")
    private String hazardClass;
    @Column(name = "dangerous_good_hazard_division")
    private String hazardDivision;
    @Column(name = "dangerous_good_subsidiary_risk")
    private String subsidiaryRisk;
    @Column(name = "dangerous_good_packing_group")
    private String packingGroup;
    @Column(name = "dangerous_good_quantity", precision = 19, scale = 3)
    private BigDecimal quantity;
    @Column(name = "dangerous_good_quantity_unit")
    private String quantityUnit;
    @Column(name = "dangerous_good_package_count")
    private Integer packageCount;
    @Column(name = "dangerous_good_packaging_type")
    private String packagingType;
    @Column(name = "dangerous_good_limited_quantity")
    private Boolean limitedQuantity;
    @Column(name = "dangerous_good_excepted_quantity")
    private Boolean exceptedQuantity;
    @Column(name = "dangerous_good_environmentally_hazardous")
    private Boolean environmentallyHazardous;
    @Column(name = "dangerous_good_marine_pollutant")
    private Boolean marinePollutant;
    @Column(name = "dangerous_good_transport_category")
    private String transportCategory;
    @Column(name = "dangerous_good_tunnel_restriction_code")
    private String tunnelRestrictionCode;
    @Column(name = "dangerous_good_flash_point", precision = 10, scale = 3)
    private BigDecimal flashPoint;
    @Column(name = "dangerous_good_emergency_contact")
    private String emergencyContact;
    @Column(name = "dangerous_good_emergency_contact_24h")
    private String emergencyContact24h;
    @Column(name = "dangerous_good_safety_data_sheet_reference", length = 2000)
    private String safetyDataSheetReference;
    @Column(name = "dangerous_good_declaration_document_reference", length = 2000)
    private String declarationDocumentReference;
    @Column(name = "dangerous_good_regulation_type")
    private String regulationType;
    @Column(name = "dangerous_good_transport_mode")
    private String transportMode;
    @Column(name = "dangerous_good_flammable")
    private Boolean flammable;
    @Column(name = "dangerous_good_corrosive")
    private Boolean corrosive;
    @Column(name = "dangerous_good_toxic")
    private Boolean toxic;
    @Column(name = "dangerous_good_hazard_symbols")
    private String hazardSymbols;
    @Column(name = "dangerous_good_storage_requirements")
    private String storageRequirements;
    @Column(name = "dangerous_good_handling_instructions", length = 2000)
    private String handlingInstructions;
    @Enumerated(EnumType.STRING)
    @Column(name = "dangerous_good_country_origin")
    private CountryCode countryOfOrigin;

    public DangerousGoodEmbeddable() {
    }

    public static DangerousGoodEmbeddable from(final DangerousGood dangerousGood) {
        if (dangerousGood == null) {
            return null;
        }
        final DangerousGoodEmbeddable embeddable = new DangerousGoodEmbeddable();
        embeddable.unNumber = dangerousGood.getUnNumber();
        embeddable.properShippingName = dangerousGood.getProperShippingName();
        embeddable.description = dangerousGood.getDescription();
        embeddable.hazardClass = dangerousGood.getHazardClass();
        embeddable.hazardDivision = dangerousGood.getHazardDivision();
        embeddable.subsidiaryRisk = dangerousGood.getSubsidiaryRisk();
        embeddable.packingGroup = dangerousGood.getPackingGroup();
        embeddable.quantity = dangerousGood.getQuantity();
        embeddable.quantityUnit = dangerousGood.getQuantityUnit();
        embeddable.packageCount = dangerousGood.getPackageCount();
        embeddable.packagingType = dangerousGood.getPackagingType();
        embeddable.limitedQuantity = dangerousGood.isLimitedQuantity();
        embeddable.exceptedQuantity = dangerousGood.isExceptedQuantity();
        embeddable.environmentallyHazardous = dangerousGood.isEnvironmentallyHazardous();
        embeddable.marinePollutant = dangerousGood.isMarinePollutant();
        embeddable.transportCategory = dangerousGood.getTransportCategory();
        embeddable.tunnelRestrictionCode = dangerousGood.getTunnelRestrictionCode();
        embeddable.flashPoint = dangerousGood.getFlashPoint();
        embeddable.emergencyContact = dangerousGood.getEmergencyContact();
        embeddable.emergencyContact24h = dangerousGood.getEmergencyContact24h();
        embeddable.safetyDataSheetReference = dangerousGood.getSafetyDataSheetReference();
        embeddable.declarationDocumentReference = dangerousGood.getDeclarationDocumentReference();
        embeddable.regulationType = dangerousGood.getRegulationType();
        embeddable.transportMode = dangerousGood.getTransportMode();
        embeddable.flammable = dangerousGood.isFlammable();
        embeddable.corrosive = dangerousGood.isCorrosive();
        embeddable.toxic = dangerousGood.isToxic();
        embeddable.hazardSymbols = dangerousGood.getHazardSymbols();
        embeddable.storageRequirements = dangerousGood.getStorageRequirements();
        embeddable.handlingInstructions = dangerousGood.getHandlingInstructions();
        embeddable.countryOfOrigin = dangerousGood.getCountryOfOrigin();
        return embeddable;
    }

    public DangerousGood toDomain() {
        return new DangerousGood(
                unNumber, properShippingName, description, hazardClass, hazardDivision, subsidiaryRisk,
                packingGroup, quantity, quantityUnit, packageCount, packagingType,
                Boolean.TRUE.equals(limitedQuantity), Boolean.TRUE.equals(exceptedQuantity),
                Boolean.TRUE.equals(environmentallyHazardous), Boolean.TRUE.equals(marinePollutant),
                transportCategory, tunnelRestrictionCode, flashPoint, emergencyContact, emergencyContact24h,
                safetyDataSheetReference, declarationDocumentReference, regulationType, transportMode,
                Boolean.TRUE.equals(flammable), Boolean.TRUE.equals(corrosive), Boolean.TRUE.equals(toxic),
                hazardSymbols, storageRequirements, handlingInstructions, countryOfOrigin
        );
    }
}
