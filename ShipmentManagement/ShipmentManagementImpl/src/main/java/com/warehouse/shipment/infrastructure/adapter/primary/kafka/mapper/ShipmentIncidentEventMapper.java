package com.warehouse.shipment.infrastructure.adapter.primary.kafka.mapper;

import com.warehouse.shipment.api.event.IncidentChanged;
import com.warehouse.shipment.api.event.IncidentResolved;
import com.warehouse.shipment.api.event.snapshot.ShipmentIncidentData;
import com.warehouse.shipment.api.event.snapshot.ShipmentRepackagingData;
import com.warehouse.shipment.application.port.primary.command.ShipmentIncidentNoteCommand;
import com.warehouse.shipment.application.port.primary.command.ShipmentIncidentResolutionCommand;
import com.warehouse.shipment.domain.enumeration.IncidentNoteType;
import com.warehouse.shipment.domain.enumeration.IncidentShipmentAction;
import com.warehouse.shipment.domain.enumeration.PackagingType;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import com.warehouse.shipment.domain.vo.ShipmentNoteIncidentSource;
import com.warehouse.shipment.domain.vo.ShipmentRepackaging;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;

public class ShipmentIncidentEventMapper {

    public ShipmentIncidentNoteCommand map(final IncidentChanged event, final IncidentNoteType type) {
        final ShipmentIncidentData payload = event.payload();
        return new ShipmentIncidentNoteCommand(payload.shipmentId(),
                new ShipmentNoteIncidentSource(payload.incidentId(), type, payload.reason()),
                payload.text(), payload.authorUserId(), payload.occurredAt());
    }

    public ShipmentIncidentResolutionCommand map(final IncidentResolved event) {
        return new ShipmentIncidentResolutionCommand(map(event, IncidentNoteType.RESOLVED),
                event.action() == null ? null : IncidentShipmentAction.valueOf(event.action().name()),
                map(event.repackaging()));
    }

    private ShipmentRepackaging map(final ShipmentRepackagingData repackaging) {
        if (repackaging == null) {
            return null;
        }
        final boolean dimensionsProvided = repackaging.lengthInCentimeters() != null
                && repackaging.widthInCentimeters() != null && repackaging.heightInCentimeters() != null;
        return new ShipmentRepackaging(
                repackaging.packagingType() == null ? null : PackagingType.valueOf(repackaging.packagingType()),
                repackaging.weightInKilograms() == null ? null
                        : new Weight(repackaging.weightInKilograms(), WeightUnit.KG),
                dimensionsProvided ? new Dimensions(repackaging.lengthInCentimeters(),
                        repackaging.widthInCentimeters(), repackaging.heightInCentimeters(), LengthUnit.CM) : null);
    }
}
