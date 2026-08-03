package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import java.time.LocalDateTime;
import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.model.DangerousGood;
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
    @AttributeOverride(name = "value", column = @Column(name = "destination"))
    private DepartmentCode destination;

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
    private DangerousGoodEmbeddable dangerousGood;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

    @OneToOne
    @JoinColumn(name = "shipment_id", referencedColumnName = "shipment_id", insertable = false, updatable = false)
    private SignatureEntity signature;

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
                .dangerousGood(DangerousGoodEmbeddable.from(snapshot.dangerousGood()))
                .externalId(new ExternalId<>(snapshot.externalShipmentId().value().toString()))
                .trackingNumber(snapshot.trackingNumber())
                .build();
    }

    public DangerousGood dangerousGood() {
        return dangerousGood == null ? null : dangerousGood.toDomain();
    }
}
