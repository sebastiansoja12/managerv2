package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.application.port.primary.command.ShipmentIncidentNoteCommand;
import com.warehouse.shipment.application.port.primary.command.ShipmentIncidentResolutionCommand;
import com.warehouse.shipment.domain.vo.ShipmentRepackaging;
import org.springframework.transaction.annotation.Transactional;

public class ShipmentIncidentPortImpl implements ShipmentIncidentPort {

    private final ShipmentPort shipmentPort;

    private final ShipmentReadModelSyncPort shipmentReadModelSyncPort;

    private final ShipmentContentPort shipmentContentPort;

    public ShipmentIncidentPortImpl(final ShipmentPort shipmentPort,
                                    final ShipmentReadModelSyncPort shipmentReadModelSyncPort,
                                    final ShipmentContentPort shipmentContentPort) {
        this.shipmentPort = shipmentPort;
        this.shipmentReadModelSyncPort = shipmentReadModelSyncPort;
        this.shipmentContentPort = shipmentContentPort;
    }

    @Override
    @Transactional
    public void reportIncident(final ShipmentIncidentNoteCommand command) {
        this.shipmentReadModelSyncPort.syncIncidentReported(command.shipmentId(), true);
        addNote(command);
    }

    @Override
    @Transactional
    public void commentIncident(final ShipmentIncidentNoteCommand command) {
        addNote(command);
    }

    @Override
    @Transactional
    public void resolveIncident(final ShipmentIncidentResolutionCommand command) {
        final ShipmentId shipmentId = command.note().shipmentId();
        if (command.action() != null) {
            switch (command.action()) {
                case MARK_LOST -> this.shipmentPort.markLost(shipmentId);
                case RETURN_TO_SENDER -> this.shipmentPort.returnAfterIncident(shipmentId);
                case CANCEL -> this.shipmentPort.cancelAfterIncident(shipmentId);
                case MARK_DELIVERED -> this.shipmentPort.markDelivered(shipmentId);
                case REPACK -> repack(shipmentId, command.repackaging());
            }
        }
        this.shipmentReadModelSyncPort.syncIncidentReported(shipmentId, false);
        addNote(command.note());
    }

    @Override
    @Transactional
    public void reopenIncident(final ShipmentIncidentNoteCommand command) {
        this.shipmentReadModelSyncPort.syncIncidentReported(command.shipmentId(), true);
        addNote(command);
    }

    private void repack(final ShipmentId shipmentId, final ShipmentRepackaging repackaging) {
        this.shipmentPort.repackAfterIncident(shipmentId, repackaging.packagingType(), repackaging.weight(),
                repackaging.dimensions());
    }

    private void addNote(final ShipmentIncidentNoteCommand command) {
        this.shipmentContentPort.addIncidentNote(command.shipmentId(), command.incidentSource(), command.text(),
                command.authorUserId(), command.occurredAt());
    }
}
