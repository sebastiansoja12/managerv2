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
import com.warehouse.returning.application.service.ReturnDepartmentAccessService;

import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

public class ReturnPortImpl implements ReturnPort {

    private final ReturningTrackManagerServicePort returningTrackManagerServicePort;
    private final ShipmentServicePort shipmentServicePort;
    private final DepartmentServicePort departmentServicePort;
    private final TransactionTemplate transactionTemplate;
    private final ReturnDepartmentAccessService returnDepartmentAccessService;

    public ReturnPortImpl(final ReturningTrackManagerServicePort returningTrackManagerServicePort,
                          final ShipmentServicePort shipmentServicePort,
                          final DepartmentServicePort departmentServicePort,
                          final TransactionTemplate transactionTemplate,
                          final ReturnDepartmentAccessService returnDepartmentAccessService) {
        this.returningTrackManagerServicePort = returningTrackManagerServicePort;
        this.shipmentServicePort = shipmentServicePort;
        this.departmentServicePort = departmentServicePort;
        this.transactionTemplate = transactionTemplate;
        this.returnDepartmentAccessService = returnDepartmentAccessService;
    }

    @Override
    public List<CreatedReturn> create(final CreateReturnPackageCommand command) {
        final ReturnableShipment shipment = shipmentServicePort.getShipment(command.getTrackingNumber());
        shipment.validateReturnRequest();
        if (!departmentServicePort.exists(command.getDepartmentId())) {
            throw new IllegalArgumentException("Return department does not exist");
        }
        returnDepartmentAccessService.requireAccess(command.getDepartmentId());
        return transactionTemplate.execute(status -> {
            final ShipmentId returnShipmentId = shipmentServicePort.createReturnShipmentDraft(shipment.shipmentId());
            final CreateReturnRequest request = new CreateReturnRequest(
                    shipment.shipmentId(), returnShipmentId, command.getReason(), command.getDepartmentId(),
                    command.getUserId(), command.getReasonCode());
            final List<CreatedReturn> createdReturns = returningTrackManagerServicePort.create(request);
            if (createdReturns.stream().anyMatch(created -> !returnShipmentId.equals(created.returnShipmentId()))) {
                throw new IllegalArgumentException("Return for shipment already exists");
            }
            return createdReturns;
        });
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
        requireAccess(returnId);
        returningTrackManagerServicePort.startProcessing(returnId);
    }

    @Override
    public void complete(final ReturnPackageId returnId) {
        requireAccess(returnId);
        returningTrackManagerServicePort.complete(returnId);
    }

    @Override
    public void cancel(final ReturnPackageId returnId) {
        requireAccess(returnId);
        returningTrackManagerServicePort.cancel(returnId);
    }

    @Override
    public void changeReasonCode(final ReturnPackageId returnId, final ReasonCode reasonCode) {
        requireAccess(returnId);
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

    private void requireAccess(final ReturnPackageId returnId) {
        final ReturnState returnState = returningTrackManagerServicePort.get(returnId);
        returnDepartmentAccessService.requireAccess(returnState.assignedDepartmentId(),
                returnState.returnedDepartmentId());
    }
}
