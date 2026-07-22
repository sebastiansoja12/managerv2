package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.commonassets.model.Money;
import com.warehouse.commonassets.model.Weight;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.vo.DangerousGoodId;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipment_rd")
@Entity(name = "shipment.ShipmentReadEntity")
public class ShipmentReadEntity extends BelongsToOperator {

    @Column(name = "shipment_id")
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id"))
    private ShipmentId shipmentId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "sender_telephone", nullable = false)
    private String senderTelephone;

    @Valid
    @Column(name = "sender_email", nullable = false)
    private String senderEmail;

    @Valid
    @Column(name = "sender_city", nullable = false)
    private String senderCity;

    @Valid
    @Column(name = "sender_street", nullable = false)
    private String senderStreet;

    @Valid
    @Column(name = "sender_postal_code", nullable = false)
    private String senderPostalCode;

    @Valid
    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Valid
    @Column(name = "recipient_telephone", nullable = false)
    private String recipientTelephone;

    @Valid
    @Column(name = "recipient_first_name", nullable = false)
    private String recipientFirstName;

    @Valid
    @Column(name = "recipient_last_name", nullable = false)
    private String recipientLastName;

    @Valid
    @Column(name = "recipient_city", nullable = false)
    private String recipientCity;

    @Valid
    @Column(name = "recipient_street", nullable = false)
    private String recipientStreet;

    @Valid
    @Column(name = "recipient_postal_code", nullable = false)
    private String recipientPostalCode;

    @Column(name = "parcel_size", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentSize shipmentSize;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentType shipmentType;

    @Column(name = "shipment_related_id")
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "shipment_related_id"))
    private ShipmentId shipmentRelatedId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "locked", nullable = false)
    private Boolean locked;

    @Column(name = "origin_country", nullable = false)
    @Enumerated(EnumType.STRING)
    private CountryCode originCountry;

    @Column(name = "destination_country", nullable = false)
    @Enumerated(EnumType.STRING)
    private CountryCode destinationCountry;

    @Column(name = "shipment_priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentPriority shipmentPriority;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "dangerous_good_id"))
    private DangerousGoodId dangerousGoodId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "dangerous_good_shipment_id"))
    private ShipmentId dangerousGoodShipmentId;

    @Column(name = "dangerous_good_name")
    private String dangerousGoodName;

    @Column(name = "dangerous_good_description", length = 1000)
    private String dangerousGoodDescription;

    @Column(name = "dangerous_good_classification_code")
    private String dangerousGoodClassificationCode;

    @Column(name = "dangerous_good_hazard_symbols")
    private String dangerousGoodHazardSymbols;

    @Column(name = "dangerous_good_storage_requirements")
    private String dangerousGoodStorageRequirements;

    @Column(name = "dangerous_good_handling_instructions", length = 2000)
    private String dangerousGoodHandlingInstructions;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "weight", column = @Column(name = "dangerous_good_weight")),
            @AttributeOverride(name = "unit", column = @Column(name = "dangerous_good_weight_unit")),
    })
    private Weight dangerousGoodWeight;

    @Column(name = "dangerous_good_packaging")
    private String dangerousGoodPackaging;

    @Column(name = "dangerous_good_flammable")
    private Boolean dangerousGoodFlammable;

    @Column(name = "dangerous_good_corrosive")
    private Boolean dangerousGoodCorrosive;

    @Column(name = "dangerous_good_toxic")
    private Boolean dangerousGoodToxic;

    @Column(name = "dangerous_good_emergency_contact")
    private String dangerousGoodEmergencyContact;

    @Column(name = "dangerous_good_country_origin")
    @Enumerated(EnumType.STRING)
    private CountryCode dangerousGoodCountryOfOrigin;

    @Column(name = "dangerous_good_safety_data_sheet", length = 2000)
    private String dangerousGoodSafetyDataSheet;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

    @OneToOne
    @JoinColumn(name = "shipment_id", referencedColumnName = "shipment_id", insertable = false, updatable = false)
    private SignatureEntity signature;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "external_route_id"))
    private ExternalId<String> externalRouteId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "external_return_id"))
    private ExternalId<Long> externalReturnId;

    @Column(name = "external_id", nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "external_id"))
    private ExternalId<String> externalId;

    @Column(name = "tracking_number", nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "tracking_number"))
    private TrackingNumber trackingNumber;

    public static ShipmentReadEntity from(final ShipmentSnapshot snapshot) {
        return ShipmentReadEntity.builder()
                .shipmentId(snapshot.shipmentId())
                .firstName(snapshot.sender().getFirstName())
                .lastName(snapshot.sender().getLastName())
                .senderEmail(snapshot.sender().getEmail())
                .senderCity(snapshot.sender().getCity())
                .senderStreet(snapshot.sender().getStreet())
                .senderPostalCode(snapshot.sender().getPostalCode())
                .senderTelephone(snapshot.sender().getTelephoneNumber())
                .recipientFirstName(snapshot.recipient().getFirstName())
                .recipientLastName(snapshot.recipient().getLastName())
                .recipientEmail(snapshot.recipient().getEmail())
                .recipientCity(snapshot.recipient().getCity())
                .recipientStreet(snapshot.recipient().getStreet())
                .recipientPostalCode(snapshot.recipient().getPostalCode())
                .recipientTelephone(snapshot.recipient().getTelephoneNumber())
                .shipmentSize(snapshot.shipmentSize())
                .destination(snapshot.destination())
                .shipmentStatus(snapshot.shipmentStatus())
                .shipmentType(snapshot.shipmentType())
                .shipmentRelatedId(snapshot.shipmentRelatedId())
                .createdAt(snapshot.createdAt())
                .updatedAt(snapshot.updatedAt())
                .locked(snapshot.locked())
                .originCountry(snapshot.originCountry())
                .destinationCountry(snapshot.destinationCountry())
                .shipmentPriority(snapshot.shipmentPriority())
                .price(snapshot.price())
                .dangerousGoodId(dangerousGoodId(snapshot.dangerousGood()))
                .dangerousGoodShipmentId(dangerousGoodShipmentId(snapshot.dangerousGood()))
                .dangerousGoodName(dangerousGoodName(snapshot.dangerousGood()))
                .dangerousGoodDescription(dangerousGoodDescription(snapshot.dangerousGood()))
                .dangerousGoodClassificationCode(dangerousGoodClassificationCode(snapshot.dangerousGood()))
                .dangerousGoodHazardSymbols(dangerousGoodHazardSymbols(snapshot.dangerousGood()))
                .dangerousGoodStorageRequirements(dangerousGoodStorageRequirements(snapshot.dangerousGood()))
                .dangerousGoodHandlingInstructions(dangerousGoodHandlingInstructions(snapshot.dangerousGood()))
                .dangerousGoodWeight(dangerousGoodWeight(snapshot.dangerousGood()))
                .dangerousGoodPackaging(dangerousGoodPackaging(snapshot.dangerousGood()))
                .dangerousGoodFlammable(dangerousGoodFlammable(snapshot.dangerousGood()))
                .dangerousGoodCorrosive(dangerousGoodCorrosive(snapshot.dangerousGood()))
                .dangerousGoodToxic(dangerousGoodToxic(snapshot.dangerousGood()))
                .dangerousGoodEmergencyContact(dangerousGoodEmergencyContact(snapshot.dangerousGood()))
                .dangerousGoodCountryOfOrigin(dangerousGoodCountryOfOrigin(snapshot.dangerousGood()))
                .dangerousGoodSafetyDataSheet(dangerousGoodSafetyDataSheet(snapshot.dangerousGood()))
                .externalRouteId(snapshot.routeExternalId())
                .externalReturnId(snapshot.returnExternalId())
                .externalId(new ExternalId<>(snapshot.externalShipmentId().value().toString()))
                .trackingNumber(snapshot.trackingNumber())
                .build();
    }

    public DangerousGood dangerousGood() {
        if (dangerousGoodId == null) {
            return null;
        }
        final List<String> hazardSymbols = dangerousGoodHazardSymbols == null
                ? Collections.emptyList()
                : Collections.singletonList(dangerousGoodHazardSymbols);
        return new DangerousGood(dangerousGoodId, dangerousGoodShipmentId, dangerousGoodName,
                dangerousGoodDescription, dangerousGoodClassificationCode, hazardSymbols,
                dangerousGoodStorageRequirements, dangerousGoodHandlingInstructions, dangerousGoodWeight,
                dangerousGoodPackaging, Boolean.TRUE.equals(dangerousGoodFlammable),
                Boolean.TRUE.equals(dangerousGoodCorrosive), Boolean.TRUE.equals(dangerousGoodToxic),
                dangerousGoodEmergencyContact, dangerousGoodCountryOfOrigin, dangerousGoodSafetyDataSheet);
    }

    private static DangerousGoodId dangerousGoodId(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.getDangerousGoodId() : null;
    }

    private static ShipmentId dangerousGoodShipmentId(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.getShipmentId() : null;
    }

    private static String dangerousGoodName(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.getName() : null;
    }

    private static String dangerousGoodDescription(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.getDescription() : null;
    }

    private static String dangerousGoodClassificationCode(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.getClassificationCode() : null;
    }

    private static String dangerousGoodHazardSymbols(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.getHazardSymbols() : null;
    }

    private static String dangerousGoodStorageRequirements(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.getStorageRequirements() : null;
    }

    private static String dangerousGoodHandlingInstructions(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.getHandlingInstructions() : null;
    }

    private static Weight dangerousGoodWeight(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.getWeight() : null;
    }

    private static String dangerousGoodPackaging(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.getPackaging() : null;
    }

    private static Boolean dangerousGoodFlammable(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.isFlammable() : null;
    }

    private static Boolean dangerousGoodCorrosive(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.isCorrosive() : null;
    }

    private static Boolean dangerousGoodToxic(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.isToxic() : null;
    }

    private static String dangerousGoodEmergencyContact(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.getEmergencyContact() : null;
    }

    private static CountryCode dangerousGoodCountryOfOrigin(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.getCountryOfOrigin() : null;
    }

    private static String dangerousGoodSafetyDataSheet(final DangerousGood dangerousGood) {
        return dangerousGood != null ? dangerousGood.getSafetyDataSheet() : null;
    }
}
