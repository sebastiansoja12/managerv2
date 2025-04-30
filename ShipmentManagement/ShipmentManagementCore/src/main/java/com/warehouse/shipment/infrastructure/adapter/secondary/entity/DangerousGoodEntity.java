package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import java.util.List;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Weight;

import jakarta.persistence.*;


@Entity
@Table(name = "dangerous_good")
public class DangerousGoodEntity {

    @Id
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id", nullable = false))
    private ShipmentId shipmentId;
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "classification_code", nullable = false)
    private String classificationCode;
    @Column(name = "hazard_symbols", nullable = false)
    private String hazardSymbols;
    @Column(name = "storage_requirements", nullable = false)
    private String storageRequirements;
    @Column(name = "handling_instructions", nullable = false)
    private String handlingInstructions;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "weight", column = @Column(name = "weight", nullable = false)),
            @AttributeOverride(name = "unit", column = @Column(name = "unit", nullable = false)),
    })
    private Weight weight;
    @Column(name = "packaging", nullable = false)
    private String packaging;
    @Column(name = "flammable", nullable = false)
    private boolean flammable;
    @Column(name = "corosive", nullable = false)
    private boolean corrosive;
    @Column(name = "toxic", nullable = false)
    private boolean toxic;
    @Column(name = "emergency_contact", nullable = false)
    private String emergencyContact;
    @Enumerated(EnumType.STRING)
    @Column(name = "country_origin", nullable = false)
    private Country countryOfOrigin;
    @Column(name = "safety_data_sheet", nullable = false)
    private String safetyDataSheet;

    public DangerousGoodEntity() {
    }

    public DangerousGoodEntity(final ShipmentId shipmentId, final String name, final String description,
                               final String classificationCode, final List<String> hazardSymbols,
                               final String storageRequirements, final String handlingInstructions, final Weight weight,
                               final String packaging, final boolean flammable, final boolean isCorrosive,
                               final boolean toxic, final String emergencyContact,
                               final Country countryOfOrigin, final String safetyDataSheet) {
        this.shipmentId = shipmentId;
        this.name = name;
        this.description = description;
        this.classificationCode = classificationCode;
        this.hazardSymbols = String.join(",", hazardSymbols);
        this.storageRequirements = storageRequirements;
        this.handlingInstructions = handlingInstructions;
        this.weight = weight;
        this.packaging = packaging;
        this.flammable = flammable;
        this.corrosive = isCorrosive;
        this.toxic = toxic;
        this.emergencyContact = emergencyContact;
        this.countryOfOrigin = countryOfOrigin;
        this.safetyDataSheet = safetyDataSheet;
    }

    public String getClassificationCode() {
        return classificationCode;
    }

    public boolean isCorrosive() {
        return corrosive;
    }

    public Country getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public String getDescription() {
        return description;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public boolean isFlammable() {
        return flammable;
    }

    public String getHandlingInstructions() {
        return handlingInstructions;
    }

    public String getHazardSymbols() {
        return hazardSymbols;
    }

    public String getName() {
        return name;
    }

    public String getPackaging() {
        return packaging;
    }

    public String getSafetyDataSheet() {
        return safetyDataSheet;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public String getStorageRequirements() {
        return storageRequirements;
    }

    public boolean isToxic() {
        return toxic;
    }

    public Weight getWeight() {
        return weight;
    }
}
