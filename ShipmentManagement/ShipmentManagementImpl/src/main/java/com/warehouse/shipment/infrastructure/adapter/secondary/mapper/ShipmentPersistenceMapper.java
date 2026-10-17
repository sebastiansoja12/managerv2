package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import java.util.UUID;

import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.domain.vo.CustomerReference;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.DimensionsEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.PartyEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentReadEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.WeightEntity;

public class ShipmentPersistenceMapper {

    public Shipment toDomain(final ShipmentEntity entity) {
        return Shipment.rehydrate(
                entity.getShipmentId(),
                party(entity.getSender()),
                party(entity.getRecipient()),
                entity.getShipmentStatus(),
                entity.getShipmentType(),
                entity.getShipmentRelatedId(),
                entity.getPrice(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getLocked(),
                entity.getTargetDepartmentId(),
                entity.getOriginDepartmentId(),
                entity.getSignatureRequired(),
                entity.getShipmentPriority(),
                entity.getTrackingNumber(),
                entity.getPickupMethod(),
                entity.getDeliveryMethod(),
                entity.getPickupPointId(),
                entity.getDeliveryPickupPointId(),
                new ExternalId<>(UUID.fromString(entity.getExternalId().value())),
                entity.getAcceptedAt(),
                entity.getCancelledAt(),
                entity.getCancellationReason(),
                entity.getDimensions() == null ? null : entity.getDimensions().toDomain(),
                entity.getWeight() == null ? null : entity.getWeight().toDomain(),
                customerReference(entity.getCustomerReference()),
                entity.getContentDescription(),
                entity.getDeclaredValue(),
                entity.getServiceLevel(),
                entity.getPackagingType()
        );
    }

    public Shipment toDomain(final ShipmentReadEntity entity) {
        return Shipment.rehydrate(
                entity.getShipmentId(),
                party(entity.getSender()),
                party(entity.getRecipient()),
                entity.getShipmentStatus(),
                entity.getShipmentType(),
                entity.getShipmentRelatedId(),
                entity.getPrice(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getLocked(),
                entity.getTargetDepartmentId(),
                entity.getOriginDepartmentId(),
                entity.getSignatureRequired(),
                entity.getShipmentPriority(),
                entity.getTrackingNumber(),
                entity.getPickupMethod(),
                entity.getDeliveryMethod(),
                entity.getPickupPointId(),
                entity.getDeliveryPickupPointId(),
                new ExternalId<>(UUID.fromString(entity.getExternalId().value())),
                entity.getAcceptedAt(),
                entity.getCancelledAt(),
                entity.getCancellationReason(),
                entity.getDimensions() == null ? null : entity.getDimensions().toDomain(),
                entity.getWeight() == null ? null : entity.getWeight().toDomain(),
                customerReference(entity.getCustomerReference()),
                entity.getContentDescription(),
                entity.getDeclaredValue(),
                entity.getServiceLevel(),
                entity.getPackagingType()
        );
    }

    public ShipmentEntity toEntity(final Shipment shipment) {
        final ShipmentEntity entity = ShipmentEntity.builder()
                .shipmentId(shipment.getShipmentId())
                .sender(partyEntity(shipment.getSender()))
                .recipient(partyEntity(shipment.getRecipient()))
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
                .dimensions(DimensionsEntity.from(shipment.getDimensions()))
                .weight(WeightEntity.from(shipment.getWeight()))
                .customerReference(shipment.getCustomerReference() == null ? null : shipment.getCustomerReference().value())
                .contentDescription(shipment.getContentDescription())
                .declaredValue(shipment.getDeclaredValue())
                .acceptedAt(shipment.getAcceptedAt())
                .cancelledAt(shipment.getCancelledAt())
                .cancellationReason(shipment.getCancellationReason())
                .locked(shipment.getLocked())
                .shipmentPriority(shipment.getShipmentPriority())
                .serviceLevel(shipment.getServiceLevel())
                .packagingType(shipment.getPackagingType())
                .price(shipment.getPrice())
                .signatureRequired(shipment.getSignatureRequired())
                .externalId(new ExternalId<>(shipment.getExternalShipmentId().value().toString()))
                .trackingNumber(shipment.getTrackingNumber())
                .build();
        return entity;
    }

    public ShipmentReadEntity toReadEntity(final ShipmentSnapshot snapshot, final SignatureId signatureId) {
        return ShipmentReadEntity.builder()
                .shipmentId(snapshot.shipmentId())
                .sender(partyEntity(snapshot.sender()))
                .recipient(partyEntity(snapshot.recipient()))
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
                .dimensions(DimensionsEntity.from(snapshot.dimensions()))
                .weight(WeightEntity.from(snapshot.weight()))
                .customerReference(snapshot.customerReference() == null ? null : snapshot.customerReference().value())
                .contentDescription(snapshot.contentDescription())
                .declaredValue(snapshot.declaredValue())
                .acceptedAt(snapshot.acceptedAt())
                .cancelledAt(snapshot.cancelledAt())
                .cancellationReason(snapshot.cancellationReason())
                .locked(snapshot.locked())
                .shipmentPriority(snapshot.shipmentPriority())
                .serviceLevel(snapshot.serviceLevel())
                .packagingType(snapshot.packagingType())
                .price(snapshot.price())
                .signatureId(signatureId)
                .signatureRequired(snapshot.signatureRequired())
                .externalId(new ExternalId<>(snapshot.externalShipmentId().value().toString()))
                .trackingNumber(snapshot.trackingNumber())
                .build();
    }

    private Party party(final PartyEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Party(entity.getFirstName(), entity.getLastName(), entity.getEmail(),
                entity.getTelephoneNumber(), entity.getCity(), entity.getPostalCode(), entity.getStreet(),
                entity.getCountryCode());
    }

    private PartyEntity partyEntity(final Party party) {
        if (party == null) {
            return null;
        }
        return PartyEntity.builder()
                .firstName(party.getFirstName())
                .lastName(party.getLastName())
                .email(party.getEmail())
                .telephoneNumber(party.getTelephoneNumber())
                .city(party.getCity())
                .street(party.getStreet())
                .postalCode(party.getPostalCode())
                .countryCode(party.getCountryCode())
                .build();
    }

    private CustomerReference customerReference(final String value) {
        return value == null ? null : new CustomerReference(value);
    }
}
