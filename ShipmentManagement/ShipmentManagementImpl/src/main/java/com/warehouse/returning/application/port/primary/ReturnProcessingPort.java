package com.warehouse.returning.application.port.primary;

import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.application.port.primary.command.ApplyReturnProcessingStartedCommand;
import com.warehouse.returning.application.port.primary.command.ApplyReturnCancelledCommand;

public interface ReturnProcessingPort {

    void applyCancelled(final ApplyReturnCancelledCommand command);

    void applyProcessingStarted(final ApplyReturnProcessingStartedCommand command);
    void applyStatusChanged(final ShipmentId shipmentId,
                            final ReturnStatus status);
}
