package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.vo.CustomerReference;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.ShipmentFieldChange;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.domain.vo.Weight;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class ShipmentChangeDetector {

    private static final String SHIPMENT_STATUS = "shipmentStatus";

    private static final List<ShipmentField> FIELDS = Stream.of(
            partyFields("sender", ShipmentSnapshot::sender),
            partyFields("recipient", ShipmentSnapshot::recipient),
            Stream.of(
                    new ShipmentField(SHIPMENT_STATUS, ShipmentSnapshot::shipmentStatus),
                    new ShipmentField("shipmentType", ShipmentSnapshot::shipmentType),
                    new ShipmentField("shipmentRelatedId", ShipmentSnapshot::shipmentRelatedId),
                    new ShipmentField("price", ShipmentSnapshot::price),
                    new ShipmentField("locked", ShipmentSnapshot::locked),
                    new ShipmentField("signatureRequired", ShipmentSnapshot::signatureRequired),
                    new ShipmentField("shipmentPriority", ShipmentSnapshot::shipmentPriority),
                    new ShipmentField("cancellationReason", ShipmentSnapshot::cancellationReason),
                    new ShipmentField("dimensions", ShipmentSnapshot::dimensions),
                    new ShipmentField("weight", ShipmentSnapshot::weight),
                    new ShipmentField("customerReference", ShipmentSnapshot::customerReference),
                    new ShipmentField("contentDescription", ShipmentSnapshot::contentDescription),
                    new ShipmentField("declaredValue", ShipmentSnapshot::declaredValue),
                    new ShipmentField("serviceLevel", ShipmentSnapshot::serviceLevel),
                    new ShipmentField("packagingType", ShipmentSnapshot::packagingType)))
            .flatMap(Function.identity())
            .toList();

    public List<ShipmentFieldChange> detect(final ShipmentSnapshot previousSnapshot,
                                            final ShipmentSnapshot currentSnapshot) {
        return FIELDS.stream()
                .map(field -> new ShipmentFieldChange(field.name(), field.valueOf(previousSnapshot),
                        field.valueOf(currentSnapshot)))
                .filter(ShipmentFieldChange::changed)
                .toList();
    }

    public List<ShipmentFieldChange> currentStatus(final ShipmentSnapshot currentSnapshot) {
        return List.of(new ShipmentFieldChange(SHIPMENT_STATUS, null,
                format(currentSnapshot.shipmentStatus())));
    }

    private static Stream<ShipmentField> partyFields(final String prefix,
                                                     final Function<ShipmentSnapshot, Party> party) {
        return Stream.of(
                new ShipmentField(prefix + ".firstName", snapshot -> party.apply(snapshot).getFirstName()),
                new ShipmentField(prefix + ".lastName", snapshot -> party.apply(snapshot).getLastName()),
                new ShipmentField(prefix + ".email", snapshot -> party.apply(snapshot).getEmail()),
                new ShipmentField(prefix + ".telephoneNumber",
                        snapshot -> party.apply(snapshot).getTelephoneNumber()),
                new ShipmentField(prefix + ".street", snapshot -> party.apply(snapshot).getStreet()),
                new ShipmentField(prefix + ".postalCode", snapshot -> party.apply(snapshot).getPostalCode()),
                new ShipmentField(prefix + ".city", snapshot -> party.apply(snapshot).getCity()),
                new ShipmentField(prefix + ".countryCode", snapshot -> party.apply(snapshot).getCountryCode()));
    }

    private static String format(final Object value) {
        return switch (value) {
            case null -> null;
            case final Money money -> money.getAmount() + " " + money.getCurrency();
            case final Dimensions dimensions -> dimensions.length() + "x" + dimensions.width() + "x"
                    + dimensions.height() + " " + dimensions.unit();
            case final Weight weight -> weight.value() + " " + weight.unit();
            case final CustomerReference customerReference -> customerReference.value();
            case final ShipmentId shipmentId -> String.valueOf(shipmentId.getValue());
            default -> String.valueOf(value);
        };
    }

    private record ShipmentField(String name, Function<ShipmentSnapshot, Object> extractor) {

        private String valueOf(final ShipmentSnapshot snapshot) {
            return format(this.extractor.apply(snapshot));
        }
    }
}
