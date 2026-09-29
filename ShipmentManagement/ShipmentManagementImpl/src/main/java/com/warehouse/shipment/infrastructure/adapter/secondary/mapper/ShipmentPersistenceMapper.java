package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import java.util.UUID;
import java.math.BigDecimal;

import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;
import com.warehouse.shipment.domain.vo.CustomerReference;
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
                entity.getTargetDepartmentId(),
                entity.getOriginDepartmentId(),
                signature(entity.getSignature()),
                entity.getSignature() != null,
                entity.getShipmentPriority(),
                dangerousGood(entity.getDangerousGood()),
                entity.getTrackingNumber(),
                entity.getPickupMethod(),
                entity.getDeliveryMethod(),
                entity.getPickupPointId(),
                entity.getDeliveryPickupPointId(),
                new ExternalId<>(UUID.fromString(entity.getExternalId().value())),
                entity.getAcceptedAt(),
                entity.getCancelledAt(),
                entity.getCancellationReason(),
                dimensions(entity.getLength(), entity.getWidth(), entity.getHeight(), entity.getLengthUnit()),
                weight(entity.getWeightValue(), entity.getWeightUnit()),
                customerReference(entity.getCustomerReference()),
                entity.getContentDescription(),
                entity.getDeclaredValue()
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
                entity.getTargetDepartmentId(),
                entity.getOriginDepartmentId(),
                signature(entity.getSignature()),
                entity.getSignature() != null,
                entity.getShipmentPriority(),
                dangerousGood(entity.getDangerousGood()),
                entity.getTrackingNumber(),
                entity.getPickupMethod(),
                entity.getDeliveryMethod(),
                entity.getPickupPointId(),
                entity.getDeliveryPickupPointId(),
                new ExternalId<>(UUID.fromString(entity.getExternalId().value())),
                entity.getAcceptedAt(),
                entity.getCancelledAt(),
                entity.getCancellationReason(),
                dimensions(entity.getLength(), entity.getWidth(), entity.getHeight(), entity.getLengthUnit()),
                weight(entity.getWeightValue(), entity.getWeightUnit()),
                customerReference(entity.getCustomerReference()),
                entity.getContentDescription(),
                entity.getDeclaredValue()
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
                .targetDepartmentId(shipment.getTargetDepartmentId())
                .originDepartmentId(shipment.getOriginDepartmentId())
                .pickupPointId(shipment.getPickupPointId())
                .deliveryPickupPointId(shipment.getDeliveryPickupPointId())
                .pickupMethod(shipment.getPickupMethod())
                .deliveryMethod(shipment.getDeliveryMethod())
                .shipmentStatus(shipment.getShipmentStatus())
                .shipmentType(shipment.getShipmentType())
                .shipmentRelatedId(shipment.getShipmentRelatedId())
                .createdAt(shipment.getCreatedAt())
                .updatedAt(shipment.getUpdatedAt())
                .length(shipment.getDimensions() == null ? null : shipment.getDimensions().length())
                .width(shipment.getDimensions() == null ? null : shipment.getDimensions().width())
                .height(shipment.getDimensions() == null ? null : shipment.getDimensions().height())
                .lengthUnit(shipment.getDimensions() == null ? null : shipment.getDimensions().unit())
                .weightValue(shipment.getWeight() == null ? null : shipment.getWeight().value())
                .weightUnit(shipment.getWeight() == null ? null : shipment.getWeight().unit())
                .customerReference(shipment.getCustomerReference() == null ? null : shipment.getCustomerReference().value())
                .contentDescription(shipment.getContentDescription())
                .declaredValue(shipment.getDeclaredValue())
                .acceptedAt(shipment.getAcceptedAt())
                .cancelledAt(shipment.getCancelledAt())
                .cancellationReason(shipment.getCancellationReason())
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
                .targetDepartmentId(snapshot.destinationDepartmentId())
                .originDepartmentId(snapshot.originDepartmentId())
                .pickupPointId(snapshot.pickupPointId())
                .deliveryPickupPointId(snapshot.deliveryPickupPointId())
                .pickupMethod(snapshot.pickupMethod())
                .deliveryMethod(snapshot.deliveryMethod())
                .shipmentStatus(snapshot.shipmentStatus())
                .shipmentType(snapshot.shipmentType())
                .shipmentRelatedId(snapshot.shipmentRelatedId())
                .createdAt(snapshot.createdAt())
                .updatedAt(snapshot.updatedAt())
                .length(snapshot.dimensions() == null ? null : snapshot.dimensions().length())
                .width(snapshot.dimensions() == null ? null : snapshot.dimensions().width())
                .height(snapshot.dimensions() == null ? null : snapshot.dimensions().height())
                .lengthUnit(snapshot.dimensions() == null ? null : snapshot.dimensions().unit())
                .weightValue(snapshot.weight() == null ? null : snapshot.weight().value())
                .weightUnit(snapshot.weight() == null ? null : snapshot.weight().unit())
                .customerReference(snapshot.customerReference() == null ? null : snapshot.customerReference().value())
                .contentDescription(snapshot.contentDescription())
                .declaredValue(snapshot.declaredValue())
                .acceptedAt(snapshot.acceptedAt())
                .cancelledAt(snapshot.cancelledAt())
                .cancellationReason(snapshot.cancellationReason())
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

    private Dimensions dimensions(final BigDecimal length, final BigDecimal width, final BigDecimal height,
                                  final LengthUnit unit) {
        if (length == null && width == null && height == null && unit == null) {
            return null;
        }
        return new Dimensions(length, width, height, unit);
    }

    private Weight weight(final BigDecimal value, final WeightUnit unit) {
        if (value == null && unit == null) {
            return null;
        }
        return new Weight(value, unit);
    }

    private CustomerReference customerReference(final String value) {
        return value == null ? null : new CustomerReference(value);
    }
}
