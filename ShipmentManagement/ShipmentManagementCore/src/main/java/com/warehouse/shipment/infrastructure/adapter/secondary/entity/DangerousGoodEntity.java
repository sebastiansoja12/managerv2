package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Weight;
import com.warehouse.dangerousgood.domain.enumeration.ClassificationCode;
import com.warehouse.dangerousgood.domain.enumeration.Packaging;
import com.warehouse.dangerousgood.domain.enumeration.StorageRequirement;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.vo.DangerousGoodId;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.util.List;


@Entity(name = "shipment.DangerousGoodEntity")
@Table(name = "dangerous_good")
@Audited
public class DangerousGoodEntity {


    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "dangerous_good_id", nullable = false))
    private DangerousGoodId dangerousGoodId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id", nullable = false))
    private ShipmentId shipmentId;
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "classification_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private ClassificationCode classificationCode;
    @Column(name = "hazard_symbols", nullable = false)
    private String hazardSymbols;
    @Column(name = "storage_requirements", nullable = false)
    @Enumerated(EnumType.STRING)
    private StorageRequirement storageRequirements;
    @Column(name = "handling_instructions", nullable = false)
    private String handlingInstructions;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "weight", column = @Column(name = "weight", nullable = false)),
            @AttributeOverride(name = "unit", column = @Column(name = "unit", nullable = false)),
    })
    private Weight weight;
    @Column(name = "packaging", nullable = false)
    @Enumerated(EnumType.STRING)
    private Packaging packaging;
    @Column(name = "flammable", nullable = false)
    private boolean flammable;
    @Column(name = "corrosive", nullable = false)
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

    public DangerousGoodEntity(final DangerousGoodId dangerousGoodId) {
        this.dangerousGoodId = dangerousGoodId;
    }

    public DangerousGoodEntity() {
    }

	public DangerousGoodEntity(final DangerousGoodId dangerousGoodId, final ShipmentId shipmentId, final String name,
			final String description, final ClassificationCode classificationCode, final List<String> hazardSymbols,
			final StorageRequirement storageRequirements, final String handlingInstructions, final Weight weight,
			final Packaging packaging, final boolean flammable, final boolean isCorrosive, final boolean toxic,
			final String emergencyContact, final Country countryOfOrigin, final String safetyDataSheet) {
        this.dangerousGoodId = dangerousGoodId;
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

    public static DangerousGoodEntity from(final DangerousGood dangerousGood) {
        if (dangerousGood == null) {
            return null;
        }
        return new DangerousGoodEntity(dangerousGood.getDangerousGoodId());
    }

    public DangerousGoodId getDangerousGoodId() {
        return dangerousGoodId;
    }

    public ClassificationCode getClassificationCode() {
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

    public Packaging getPackaging() {
        return packaging;
    }

    public String getSafetyDataSheet() {
        return safetyDataSheet;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public StorageRequirement getStorageRequirements() {
        return storageRequirements;
    }

    public boolean isToxic() {
        return toxic;
    }

    public Weight getWeight() {
        return weight;
    }
}
