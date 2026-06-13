package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import java.util.UUID;

import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.DangerousGoodEmbeddable;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentReadEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.SignatureEntity;

public class ShipmentPersistenceMapper {

    public Shipment toDomain(final ShipmentEntity entity) {
        return Shipment.rehydrate(
                entity.getShipmentId(),
                sender(entity),
                recipient(entity),
                entity.getShipmentSize(),
                entity.getShipmentStatus(),
                entity.getShipmentType(),
                entity.getShipmentRelatedId(),
                entity.getPrice(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getLocked(),
                entity.getOriginCountry(),
                entity.getDestinationCountry(),
                entity.getDestination(),
                entity.getOriginDepartmentId(),
                signature(entity.getSignature()),
                entity.getSignature() != null,
                entity.getShipmentPriority(),
                dangerousGood(entity.getDangerousGood()),
                entity.getTrackingNumber(),
                new ExternalId<>(UUID.fromString(entity.getExternalId().value()))
        );
    }

    public Shipment toDomain(final ShipmentReadEntity entity) {
        return Shipment.rehydrate(
                entity.getShipmentId(),
                new Sender(entity.getFirstName(), entity.getLastName(), entity.getSenderEmail(),
                        entity.getSenderTelephone(), entity.getSenderCity(), entity.getSenderPostalCode(),
                        entity.getSenderStreet()),
                new Recipient(entity.getRecipientFirstName(), entity.getRecipientLastName(),
                        entity.getRecipientEmail(), entity.getRecipientTelephone(), entity.getRecipientCity(),
                        entity.getRecipientPostalCode(), entity.getRecipientStreet()),
                entity.getShipmentSize(),
                entity.getShipmentStatus(),
                entity.getShipmentType(),
                entity.getShipmentRelatedId(),
                entity.getPrice(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getLocked(),
                entity.getOriginCountry(),
                entity.getDestinationCountry(),
                entity.getDestination(),
                entity.getOriginDepartmentId(),
                signature(entity.getSignature()),
                entity.getSignature() != null,
                entity.getShipmentPriority(),
                dangerousGood(entity.getDangerousGood()),
                entity.getTrackingNumber(),
                new ExternalId<>(UUID.fromString(entity.getExternalId().value()))
        );
    }

    public ShipmentEntity toEntity(final Shipment shipment) {
        return ShipmentEntity.builder()
                .shipmentId(shipment.getShipmentId())
                .firstName(shipment.getSender().getFirstName())
                .lastName(shipment.getSender().getLastName())
                .senderTelephone(shipment.getSender().getTelephoneNumber())
                .senderEmail(shipment.getSender().getEmail())
                .senderCity(shipment.getSender().getCity())
                .senderStreet(shipment.getSender().getStreet())
                .senderPostalCode(shipment.getSender().getPostalCode())
                .recipientEmail(shipment.getRecipient().getEmail())
                .recipientTelephone(shipment.getRecipient().getTelephoneNumber())
                .recipientFirstName(shipment.getRecipient().getFirstName())
                .recipientLastName(shipment.getRecipient().getLastName())
                .recipientCity(shipment.getRecipient().getCity())
                .recipientStreet(shipment.getRecipient().getStreet())
                .recipientPostalCode(shipment.getRecipient().getPostalCode())
                .shipmentSize(shipment.getShipmentSize())
                .destination(shipment.getDestination())
                .originDepartmentId(shipment.getOriginDepartmentId())
                .shipmentStatus(shipment.getShipmentStatus())
                .shipmentType(shipment.getShipmentType())
                .shipmentRelatedId(shipment.getShipmentRelatedId())
                .createdAt(shipment.getCreatedAt())
                .updatedAt(shipment.getUpdatedAt())
                .locked(shipment.getLocked())
                .originCountry(shipment.getOriginCountry())
                .destinationCountry(shipment.getDestinationCountry())
                .shipmentPriority(shipment.getShipmentPriority())
                .dangerousGood(DangerousGoodEmbeddable.from(shipment.getDangerousGood()))
                .price(shipment.getPrice())
                .signature(signatureEntity(shipment.getSignature()))
                .externalId(new ExternalId<>(shipment.getExternalShipmentId().value().toString()))
                .trackingNumber(shipment.getTrackingNumber())
                .build();
    }

    public ShipmentReadEntity toReadEntity(final ShipmentSnapshot snapshot) {
        return ShipmentReadEntity.builder()
                .shipmentId(snapshot.shipmentId())
                .firstName(snapshot.sender().getFirstName())
                .lastName(snapshot.sender().getLastName())
                .senderTelephone(snapshot.sender().getTelephoneNumber())
                .senderEmail(snapshot.sender().getEmail())
                .senderCity(snapshot.sender().getCity())
                .senderStreet(snapshot.sender().getStreet())
                .senderPostalCode(snapshot.sender().getPostalCode())
                .recipientEmail(snapshot.recipient().getEmail())
                .recipientTelephone(snapshot.recipient().getTelephoneNumber())
                .recipientFirstName(snapshot.recipient().getFirstName())
                .recipientLastName(snapshot.recipient().getLastName())
                .recipientCity(snapshot.recipient().getCity())
                .recipientStreet(snapshot.recipient().getStreet())
                .recipientPostalCode(snapshot.recipient().getPostalCode())
                .shipmentSize(snapshot.shipmentSize())
                .destination(snapshot.destination())
                .originDepartmentId(snapshot.originDepartmentId())
                .shipmentStatus(snapshot.shipmentStatus())
                .shipmentType(snapshot.shipmentType())
                .shipmentRelatedId(snapshot.shipmentRelatedId())
                .createdAt(snapshot.createdAt())
                .updatedAt(snapshot.updatedAt())
                .locked(snapshot.locked())
                .originCountry(snapshot.originCountry())
                .destinationCountry(snapshot.destinationCountry())
                .shipmentPriority(snapshot.shipmentPriority())
                .dangerousGood(DangerousGoodEmbeddable.from(snapshot.dangerousGood()))
                .price(snapshot.price())
                .externalId(new ExternalId<>(snapshot.externalShipmentId().value().toString()))
                .trackingNumber(snapshot.trackingNumber())
                .build();
    }

    private Sender sender(final ShipmentEntity entity) {
        return new Sender(entity.getFirstName(), entity.getLastName(), entity.getSenderEmail(),
                entity.getSenderTelephone(), entity.getSenderCity(), entity.getSenderPostalCode(),
                entity.getSenderStreet());
    }

    private Recipient recipient(final ShipmentEntity entity) {
        return new Recipient(entity.getRecipientFirstName(), entity.getRecipientLastName(),
                entity.getRecipientEmail(), entity.getRecipientTelephone(), entity.getRecipientCity(),
                entity.getRecipientPostalCode(), entity.getRecipientStreet());
    }

    private Signature signature(final SignatureEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Signature(entity.getSignerName(), entity.getSignedAt(), entity.getSignatureMethod(),
                entity.getDocumentReference(), entity.getShipmentId(), entity.getSignature());
    }

    private SignatureEntity signatureEntity(final Signature signature) {
        if (signature == null) {
            return null;
        }
        return new SignatureEntity(signature.getSignerName(), signature.getSignedAt(), signature.getSignatureMethod(),
                signature.getDocumentReference(), signature.getShipmentId(), signature.getSignature());
    }

    private DangerousGood dangerousGood(final DangerousGoodEmbeddable embeddable) {
        return embeddable == null ? null : embeddable.toDomain();
    }
}
