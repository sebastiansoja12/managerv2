package com.warehouse.returning.application.port.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.application.port.primary.command.ApplyReturnProcessingStartedCommand;
import com.warehouse.returning.application.port.primary.command.ApplyReturnCancelledCommand;
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
                    if (!returnConsumedEventServicePort.lockAndCheckCancelled(command.returnPackageId())
                            && returnConsumedEventServicePort.tryConsume(command.eventId())) {
                        shipmentServicePort.markReturned(command.shipmentId());
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
                    cancelShipmentReturn(command.shipmentId());
                    returnConsumedEventServicePort.markCancelled(command.returnPackageId());
                }));
    }

    @Override
    @Transactional
    public void applyStatusChanged(final ShipmentId shipmentId, final ReturnStatus status) {
        switch (status) {
            case CANCELLED -> cancelShipmentReturn(shipmentId);
            case COMPLETED -> shipmentServicePort.completeReturn(shipmentId);
            case CREATED, PROCESSING -> { }
        }
    }
    private void cancelShipmentReturn(final ShipmentId shipmentId) {
        final ReturnableShipment shipment = shipmentServicePort.getShipment(shipmentId);
        if (shipment.status() == ShipmentStatus.RETURN) {
            if (shipment.relatedShipmentId() != null) {
                shipmentServicePort.cancelReturnShipment(shipment.relatedShipmentId());
            }
            shipmentServicePort.restoreAfterReturnCancellation(shipmentId);
        }
    }

}
