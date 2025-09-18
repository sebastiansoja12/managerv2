package com.warehouse.dangerousgood.domain.model;

import java.time.Instant;
import java.util.List;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Weight;
import com.warehouse.dangerousgood.domain.enumeration.ClassificationCode;
import com.warehouse.dangerousgood.domain.enumeration.Packaging;
import com.warehouse.dangerousgood.domain.enumeration.StorageRequirement;
import com.warehouse.dangerousgood.domain.event.GoodPackagingChanged;
import com.warehouse.dangerousgood.domain.event.GoodWeightChanged;
import com.warehouse.dangerousgood.domain.registry.DomainRegistry;
import com.warehouse.dangerousgood.domain.vo.DangerousGoodId;
import com.warehouse.dangerousgood.domain.vo.GoodSnapshot;

public class DangerousGood {

    private DangerousGoodId dangerousGoodId;
    private ShipmentId shipmentId;
    private String name;
    private String description;
    private ClassificationCode classificationCode;
    private String hazardSymbols;
    private StorageRequirement storageRequirement;
    private String handlingInstructions;
    private Weight weight;
    private Packaging packaging;
    private boolean flammable;
    private boolean corrosive;
    private boolean toxic;
    private String emergencyContact;
    private Country countryOfOrigin;
    private String safetyDataSheet;
    private Instant createdAt;
    private Instant modifiedAt;

    public DangerousGood() {
    }

	public DangerousGood(final DangerousGoodId dangerousGoodId, final ShipmentId shipmentId, final String name,
                         final String description, final ClassificationCode classificationCode, final List<String> hazardSymbols,
                         final StorageRequirement storageRequirement, final String handlingInstructions, final Weight weight,
                         final Packaging packaging, final boolean flammable, final boolean isCorrosive, final boolean toxic,
                         final String emergencyContact, final Country countryOfOrigin, final String safetyDataSheet) {
        this.dangerousGoodId = dangerousGoodId;
        this.shipmentId = shipmentId;
        this.name = name;
        this.description = description;
        this.classificationCode = classificationCode;
        this.hazardSymbols = String.join(" ", hazardSymbols);
        this.storageRequirement = storageRequirement;
        this.handlingInstructions = handlingInstructions;
        this.weight = weight;
        this.packaging = packaging;
        this.flammable = flammable;
        this.corrosive = isCorrosive;
        this.toxic = toxic;
        this.emergencyContact = emergencyContact;
        this.countryOfOrigin = countryOfOrigin;
        this.safetyDataSheet = safetyDataSheet;
        this.createdAt = Instant.now();
        this.modifiedAt = Instant.now();
    }

    public DangerousGoodId getDangerousGoodId() {
        return dangerousGoodId;
    }

    public void setDangerousGoodId(final DangerousGoodId dangerousGoodId) {
        this.dangerousGoodId = dangerousGoodId;
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

    public void setClassificationCode(final ClassificationCode classificationCode) {
        this.classificationCode = classificationCode;
    }

    public void setHazardSymbols(final String hazardSymbols) {
        this.hazardSymbols = hazardSymbols;
    }

    public void setStorageRequirement(final StorageRequirement storageRequirement) {
        this.storageRequirement = storageRequirement;
    }

    public void setHandlingInstructions(final String handlingInstructions) {
        this.handlingInstructions = handlingInstructions;
    }

    public void setWeight(final Weight weight) {
        this.weight = weight;
    }

    public void setPackaging(final Packaging packaging) {
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

    public ClassificationCode getClassificationCode() {
        return classificationCode;
    }

    public String getHazardSymbols() {
        return hazardSymbols;
    }

    public StorageRequirement getStorageRequirement() {
        return storageRequirement;
    }

    public String getHandlingInstructions() {
        return handlingInstructions;
    }

    public Weight getWeight() {
        return weight;
    }

    public Packaging getPackaging() {
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(final Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void changeWeight(final Weight weight) {
        this.weight = weight;
        markAsModified();
        DomainRegistry.publish(new GoodWeightChanged(this.snapshot(), this.modifiedAt));
    }

    public void changePackaging(final Packaging packaging) {
        this.packaging = packaging;
        markAsModified();
        DomainRegistry.publish(new GoodPackagingChanged(this.snapshot(), this.modifiedAt));
    }

    public void changeFlammable(final boolean flammable) {
        this.flammable = flammable;
    }

    private void markAsModified() {
        this.modifiedAt = Instant.now();
    }
    
    private GoodSnapshot snapshot() {
		return new GoodSnapshot(dangerousGoodId, shipmentId, name, description, classificationCode, hazardSymbols,
				storageRequirement, handlingInstructions, weight, packaging, flammable, corrosive, toxic,
				emergencyContact, countryOfOrigin, safetyDataSheet);
    }

    public GoodSnapshot toSnapshot() {
        return snapshot();
    }
}

