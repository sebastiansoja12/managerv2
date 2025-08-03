package com.warehouse.shipment.domain.model;

import java.util.List;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Weight;

public class DangerousGood {

    private ShipmentId shipmentId;
    private String name;
    private String description;
    private String classificationCode;
    private String hazardSymbols;
    private String storageRequirements;
    private String handlingInstructions;
    private Weight weight;
    private String packaging;
    private boolean flammable;
    private boolean corrosive;
    private boolean toxic;
    private String emergencyContact;
    private Country countryOfOrigin;
    private String safetyDataSheet;

    public DangerousGood(final ShipmentId shipmentId, final String name, final String description,
                         final String classificationCode, final List<String> hazardSymbols,
                         final String storageRequirements, final String handlingInstructions, final Weight weight,
                         final String packaging, final boolean flammable, final boolean isCorrosive,
                         final boolean toxic, final String emergencyContact,
                         final Country countryOfOrigin, final String safetyDataSheet) {
        this.shipmentId = shipmentId;
        this.name = name;
        this.description = description;
        this.classificationCode = classificationCode;
        this.hazardSymbols = String.join(" ", hazardSymbols);
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

    public static DangerousGood from(final DangerousGoodCreateRequest request) {
        return new DangerousGood(
                request.getShipmentId(),
                request.getName(),
                request.getDescription(),
                request.getClassificationCode(),
                request.getHazardSymbols(),
                request.getStorageRequirements(),
                request.getHandlingInstructions(),
                request.getWeight(),
                request.getPackaging(),
                request.isFlammable(),
                request.isCorrosive(),
                request.isToxic(),
                request.getEmergencyContact(),
                request.getCountryOfOrigin(),
                request.getSafetyDataSheet()
        );
    }


    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setClassificationCode(final String classificationCode) {
        this.classificationCode = classificationCode;
    }

    public void setHazardSymbols(final String hazardSymbols) {
        this.hazardSymbols = hazardSymbols;
    }

    public void setStorageRequirements(final String storageRequirements) {
        this.storageRequirements = storageRequirements;
    }

    public void setHandlingInstructions(final String handlingInstructions) {
        this.handlingInstructions = handlingInstructions;
    }

    public void setWeight(final Weight weight) {
        this.weight = weight;
    }

    public void setPackaging(final String packaging) {
        this.packaging = packaging;
    }

    public void setFlammable(final boolean flammable) {
        this.flammable = flammable;
    }

    public void setCorrosive(final boolean corrosive) {
        this.corrosive = corrosive;
    }

    public void setToxic(final boolean toxic) {
        this.toxic = toxic;
    }

    public void setEmergencyContact(final String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public void setCountryOfOrigin(final Country countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public void setSafetyDataSheet(final String safetyDataSheet) {
        this.safetyDataSheet = safetyDataSheet;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getClassificationCode() {
        return classificationCode;
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

    public Weight getWeight() {
        return weight;
    }

    public String getPackaging() {
        return packaging;
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

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public Country getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public String getSafetyDataSheet() {
        return safetyDataSheet;
    }
}

