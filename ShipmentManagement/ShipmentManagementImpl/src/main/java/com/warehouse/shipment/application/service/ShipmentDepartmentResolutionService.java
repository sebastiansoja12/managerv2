package com.warehouse.shipment.application.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.secondary.PickupPointServicePort;
import com.warehouse.shipment.domain.enumeration.ShipmentSource;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;

public class ShipmentDepartmentResolutionService {

    private final PickupPointServicePort pickupPointServicePort;
    private final OperatorContextProvider operatorContextProvider;

    public ShipmentDepartmentResolutionService(final PickupPointServicePort pickupPointServicePort,
                                               final OperatorContextProvider operatorContextProvider) {
        this.pickupPointServicePort = pickupPointServicePort;
        this.operatorContextProvider = operatorContextProvider;
    }

    public Result<DepartmentId, ErrorCode> resolveTargetDepartmentId(final ShipmentCreateCommand command) {
        if (command.getDeliveryMethod().isPickupPointBased()) {
            return resolvePickupPointDepartmentId(
                    command.getDeliveryPickupPointId(), ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE);
        }
        // the delivery department for an address is resolved by the delivery once the shipment is created
        return Result.success(null);
    }

    public Result<DepartmentId, ErrorCode> resolveOriginDepartmentId(final ShipmentCreateCommand command) {
        if (command.getPickupMethod().isPickupPointBased()) {
            return resolvePickupPointDepartmentId(command.getPickupPointId(), ErrorCode.ORIGIN_DEPARTMENT_NOT_AVAILABLE);
        }
        if (command.getSource() == ShipmentSource.EXTERNAL) {
            // origin department of an external shipment is unknown until it is handed over to a department
            return Result.success(null);
        }
        return validateDepartmentId(operatorContextProvider.currentDepartmentId(),
                ErrorCode.ORIGIN_DEPARTMENT_NOT_AVAILABLE);
    }

    private Result<DepartmentId, ErrorCode> resolvePickupPointDepartmentId(
            final PickupPointId pickupPointId, final ErrorCode missingDepartmentError) {
        if (pickupPointId == null) {
            return Result.failure(missingDepartmentError);
        }
        return validateDepartmentId(pickupPointServicePort.findDepartmentId(pickupPointId).orElse(null),
                missingDepartmentError);
    }

    private Result<DepartmentId, ErrorCode> validateDepartmentId(
            final DepartmentId departmentId, final ErrorCode missingDepartmentError) {
        if (departmentId == null || departmentId.getValue() == null) {
            return Result.failure(missingDepartmentError);
        }
        return Result.success(departmentId);
    }
}
