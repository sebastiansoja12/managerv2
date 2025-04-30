package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.enumeration.WeightUnit;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Weight;
import com.warehouse.shipment.infrastructure.adapter.primary.api.DangerousGoodCreateRequestDto;

import java.util.List;

public class DangerousGoodCreateRequest {

    private ShipmentId shipmentId;
    private String name;
    private String description;
    private String classificationCode;
    private List<String> hazardSymbols;
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

    public DangerousGoodCreateRequest(final ShipmentId shipmentId,
                                      final String name,
                                      final String description,
                                      final String classificationCode,
                                      final List<String> hazardSymbols,
                                      final String storageRequirements,
                                      final String handlingInstructions,
                                      final Weight weight,
                                      final String packaging,
                                      final boolean flammable,
                                      final boolean corrosive,
                                      final boolean toxic,
                                      final String emergencyContact,
                                      final Country countryOfOrigin,
                                      final String safetyDataSheet) {
        this.shipmentId = shipmentId;
        this.name = name;
        this.description = description;
        this.classificationCode = classificationCode;
        this.hazardSymbols = hazardSymbols;
        this.storageRequirements = storageRequirements;
        this.handlingInstructions = handlingInstructions;
        this.weight = weight;
        this.packaging = packaging;
        this.flammable = flammable;
        this.corrosive = corrosive;
        this.toxic = toxic;
        this.emergencyContact = emergencyContact;
        this.countryOfOrigin = countryOfOrigin;
        this.safetyDataSheet = safetyDataSheet;
    }


    public static DangerousGoodCreateRequest from(final DangerousGoodCreateRequestDto request) {
        final ShipmentId shipmentId = new ShipmentId(request.shipmentId().getValue());
        final Weight weight = new Weight(request.weight().value(), WeightUnit.valueOf(request.weight().unit()));

        return new DangerousGoodCreateRequest(
                shipmentId,
                request.name(),
                request.description(),
                request.classificationCode(),
                request.hazardSymbols(),
                request.storageRequirements(),
                request.handlingInstructions(),
                weight,
                request.packaging(),
                request.flammable(),
                request.corosive(),
                request.toxic(),
                request.emergencyContact(),
                request.countryOfOrigin(),
                request.safetyDataSheet()
        );
    }



    public String getClassificationCode() {
        return classificationCode;
    }

    public void setClassificationCode(final String classificationCode) {
        this.classificationCode = classificationCode;
    }

    public boolean isCorrosive() {
        return corrosive;
    }

    public void setCorrosive(final boolean corrosive) {
        this.corrosive = corrosive;
    }

    public Country getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(final Country countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(final String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public boolean isFlammable() {
        return flammable;
    }

    public void setFlammable(final boolean flammable) {
        this.flammable = flammable;
    }

    public String getHandlingInstructions() {
        return handlingInstructions;
    }

    public void setHandlingInstructions(final String handlingInstructions) {
        this.handlingInstructions = handlingInstructions;
    }

    public List<String> getHazardSymbols() {
        return hazardSymbols;
    }

    public void setHazardSymbols(final List<String> hazardSymbols) {
        this.hazardSymbols = hazardSymbols;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(final String packaging) {
        this.packaging = packaging;
    }

    public String getSafetyDataSheet() {
        return safetyDataSheet;
    }

    public void setSafetyDataSheet(final String safetyDataSheet) {
        this.safetyDataSheet = safetyDataSheet;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getStorageRequirements() {
        return storageRequirements;
    }

    public void setStorageRequirements(final String storageRequirements) {
        this.storageRequirements = storageRequirements;
    }

    public boolean isToxic() {
        return toxic;
    }

    public void setToxic(final boolean toxic) {
        this.toxic = toxic;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(final Weight weight) {
        this.weight = weight;
    }
}
