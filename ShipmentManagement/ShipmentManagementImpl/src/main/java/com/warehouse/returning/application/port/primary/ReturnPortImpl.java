package com.warehouse.returning.application.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.application.port.primary.command.ChangeReturnPackageStatusCommand;
import com.warehouse.returning.application.port.primary.command.CreateReturnPackageCommand;
import com.warehouse.returning.application.port.secondary.ReturningTrackManagerServicePort;
import com.warehouse.returning.application.port.secondary.ShipmentServicePort;
import com.warehouse.returning.domain.model.ReturnableShipment;
import com.warehouse.returning.domain.vo.CreateReturnRequest;
import com.warehouse.returning.domain.vo.CreatedReturn;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnState;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.application.port.secondary.DepartmentServicePort;

import java.util.List;

public class ReturnPortImpl implements ReturnPort {

    private final ReturningTrackManagerServicePort returningTrackManagerServicePort;
    private final ShipmentServicePort shipmentServicePort;
    private final DepartmentServicePort departmentServicePort;

    public ReturnPortImpl(final ReturningTrackManagerServicePort returningTrackManagerServicePort,
                          final ShipmentServicePort shipmentServicePort,
                          final DepartmentServicePort departmentServicePort) {
        this.returningTrackManagerServicePort = returningTrackManagerServicePort;
        this.shipmentServicePort = shipmentServicePort;
        this.departmentServicePort = departmentServicePort;
    }

    @Override
    public List<CreatedReturn> create(final CreateReturnPackageCommand command) {
        final ReturnableShipment shipment = shipmentServicePort.getShipment(command.getShipmentId());
        shipment.validateReturnRequest();
        if (!departmentServicePort.exists(command.getDepartmentId())) {
            throw new IllegalArgumentException("Return department does not exist");
        }
        final CreateReturnRequest request = new CreateReturnRequest(
                shipment.shipmentId(), command.getReason(), command.getDepartmentId(),
                command.getUserId(), command.getReasonCode());
        return returningTrackManagerServicePort.create(request);
    }

    @Override
    public void changeStatus(final ChangeReturnPackageStatusCommand command) {
        switch (command.getReturnStatus()) {
            case PROCESSING -> startProcessing(command.getReturnPackageId());
            case COMPLETED -> complete(command.getReturnPackageId());
            case CANCELLED -> cancel(command.getReturnPackageId());
            case CREATED -> throw new IllegalArgumentException("Use the create endpoint to register a return");
        }
    }

    @Override
    public void startProcessing(final ReturnPackageId returnId) {
        returningTrackManagerServicePort.startProcessing(returnId);
    }

    @Override
    public void complete(final ReturnPackageId returnId) {
        returningTrackManagerServicePort.complete(returnId);
    }

    @Override
    public void cancel(final ReturnPackageId returnId) {
        returningTrackManagerServicePort.cancel(returnId);
    }

    @Override
    public void changeReasonCode(final ReturnPackageId returnId, final ReasonCode reasonCode) {
        returningTrackManagerServicePort.changeReasonCode(returnId, reasonCode);
    }

    @Override
    public boolean validateToken(final ShipmentId shipmentId, final ReturnToken returnToken) {
        return returningTrackManagerServicePort.validateToken(shipmentId, returnToken);
    }

    @Override
    public ReturnState get(final ReturnPackageId returnId) {
        return returningTrackManagerServicePort.get(returnId);
    }

}
