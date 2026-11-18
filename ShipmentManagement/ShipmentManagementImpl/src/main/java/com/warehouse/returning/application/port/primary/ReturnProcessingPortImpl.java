package com.warehouse.returning.application.port.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.application.port.primary.command.ApplyReturnProcessingStartedCommand;
import com.warehouse.returning.application.port.primary.command.ApplyReturnCancelledCommand;
import com.warehouse.returning.application.port.primary.command.ApplyReturnCompletedCommand;
import com.warehouse.returning.domain.model.ReturnableShipment;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.returning.application.port.secondary.ReturnConsumedEventServicePort;
import com.warehouse.returning.application.port.secondary.ShipmentServicePort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

public class ReturnProcessingPortImpl implements ReturnProcessingPort {

    private final ShipmentServicePort shipmentServicePort;
    private final ReturnConsumedEventServicePort returnConsumedEventServicePort;
    private final OperatorContext operatorContext;
    private final TransactionTemplate transactionTemplate;

    public ReturnProcessingPortImpl(final ShipmentServicePort shipmentServicePort,
                                    final ReturnConsumedEventServicePort returnConsumedEventServicePort,
                                    final OperatorContext operatorContext,
                                    final TransactionTemplate transactionTemplate) {
        this.shipmentServicePort = shipmentServicePort;
        this.returnConsumedEventServicePort = returnConsumedEventServicePort;
        this.operatorContext = operatorContext;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void applyProcessingStarted(final ApplyReturnProcessingStartedCommand command) {
        operatorContext.runAs(command.operatorId(), command.userId(), command.departmentId(),
                () -> transactionTemplate.executeWithoutResult(status -> {
                    if (returnConsumedEventServicePort.lockAndCheckCancelled(command.returnPackageId())
                            || !returnConsumedEventServicePort.tryConsume(command.eventId())) {
                        return;
                    }
                    if (alreadyReturned(command.shipmentId())) {
                        return;
                    }
                    if (command.returnShipmentId() == null) {
                        shipmentServicePort.markReturned(command.shipmentId());
                    } else {
                        shipmentServicePort.markReturned(command.shipmentId(), command.returnShipmentId());
                    }
                }));
    }

    @Override
    public void applyCancelled(final ApplyReturnCancelledCommand command) {
        operatorContext.runAs(command.operatorId(), command.userId(), command.departmentId(),
                () -> transactionTemplate.executeWithoutResult(status -> {
                    if (returnConsumedEventServicePort.lockAndCheckCancelled(command.returnPackageId())
                            || !returnConsumedEventServicePort.tryConsume(command.eventId())) {
                        return;
                    }
                    cancelShipmentReturn(command.shipmentId(), command.returnShipmentId());
                    returnConsumedEventServicePort.markCancelled(command.returnPackageId());
                }));
    }

    @Override
    public void applyCompleted(final ApplyReturnCompletedCommand command) {
        operatorContext.runAs(command.operatorId(), command.userId(), command.departmentId(),
                () -> transactionTemplate.executeWithoutResult(status -> {
                    if (returnConsumedEventServicePort.lockAndCheckCancelled(command.returnPackageId())
                            || !returnConsumedEventServicePort.tryConsume(command.eventId())) {
                        return;
                    }
                    completeShipmentReturn(command.shipmentId(), command.returnShipmentId());
                }));
    }

    @Override
    @Transactional
    public void applyStatusChanged(final ShipmentId shipmentId, final ReturnStatus status) {
        switch (status) {
            case CANCELLED -> cancelShipmentReturn(shipmentId, null);
            case COMPLETED -> completeShipmentReturn(shipmentId, null);
            case CREATED, PROCESSING -> { }
        }
    }

    private boolean alreadyReturned(final ShipmentId shipmentId) {
        return shipmentServicePort.getShipment(shipmentId).status() == ShipmentStatus.RETURN;
    }

    private void completeShipmentReturn(final ShipmentId shipmentId, final ShipmentId returnShipmentId) {
        final ReturnableShipment shipment = shipmentServicePort.getShipment(shipmentId);
        if (shipment.status() != ShipmentStatus.RETURN) {
            return;
        }
        final ShipmentId deliveredShipmentId = resolveReturnShipmentId(shipment, returnShipmentId);
        if (deliveredShipmentId != null) {
            shipmentServicePort.markDelivered(deliveredShipmentId);
        }
        shipmentServicePort.completeReturn(shipmentId);
    }

    private void cancelShipmentReturn(final ShipmentId shipmentId, final ShipmentId returnShipmentId) {
        final ReturnableShipment shipment = shipmentServicePort.getShipment(shipmentId);
        final ShipmentId cancelledShipmentId = resolveReturnShipmentId(shipment, returnShipmentId);
        if (cancelledShipmentId != null) {
            shipmentServicePort.cancelReturnShipment(cancelledShipmentId);
        }
        if (shipment.status() == ShipmentStatus.RETURN) {
            shipmentServicePort.restoreAfterReturnCancellation(shipmentId);
        }
    }

    private ShipmentId resolveReturnShipmentId(final ReturnableShipment shipment, final ShipmentId returnShipmentId) {
        if (returnShipmentId != null) {
            return returnShipmentId;
        }
        return shipment.status() == ShipmentStatus.RETURN ? shipment.relatedShipmentId() : null;
    }
}
