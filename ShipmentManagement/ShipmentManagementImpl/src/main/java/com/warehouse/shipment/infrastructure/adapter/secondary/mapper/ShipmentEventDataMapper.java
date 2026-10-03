package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.api.event.snapshot.MoneySnapshot;
import com.warehouse.shipment.api.event.snapshot.PartySnapshot;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;
import com.warehouse.shipment.application.port.secondary.ShipmentEventDataMapperPort;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import org.springframework.stereotype.Component;

@Component
public class ShipmentEventDataMapper implements ShipmentEventDataMapperPort {

    @Override
    public ShipmentEventData map(final ShipmentSnapshot shipment) {
        return new ShipmentEventData(
                shipment.shipmentId(),
                partySnapshot(shipment.sender()),
                partySnapshot(shipment.recipient()),
                shipment.destinationDepartmentId(),
                shipment.originDepartmentId(),
                shipment.shipmentStatus(),
                shipment.shipmentType(),
                shipment.shipmentRelatedId(),
                moneySnapshot(shipment.price()),
                shipment.createdAt(),
                shipment.updatedAt(),
                shipment.locked(),
                shipment.signatureRequired(),
                shipment.shipmentPriority(),
                shipment.trackingNumber(),
                shipment.pickupMethod() == null ? null
                        : ShipmentEventData.PickupMethod.valueOf(shipment.pickupMethod().name()),
                shipment.deliveryMethod() == null ? null
                        : ShipmentEventData.DeliveryMethod.valueOf(shipment.deliveryMethod().name()),
                shipment.pickupPointId(),
                shipment.deliveryPickupPointId(),
                shipment.externalShipmentId(),
                shipment.serviceLevel() == null ? null
                        : ShipmentEventData.ShipmentServiceLevel.valueOf(shipment.serviceLevel().name()),
                shipment.packagingType() == null ? null : shipment.packagingType().name()
        );
    }

    private PartySnapshot partySnapshot(final Party party) {
        if (party == null) {
            return null;
        }
        return new PartySnapshot(
                party.getFirstName(),
                party.getLastName(),
                party.getEmail(),
                party.getTelephoneNumber(),
                party.getCity(),
                party.getPostalCode(),
                party.getStreet(),
                party.getAddress().getCountryCode() == null ? null : party.getAddress().getCountryCode().name()
        );
    }

    private MoneySnapshot moneySnapshot(final Money money) {
        return money == null ? null : new MoneySnapshot(money.getAmount(), money.getCurrency());
    }
}
