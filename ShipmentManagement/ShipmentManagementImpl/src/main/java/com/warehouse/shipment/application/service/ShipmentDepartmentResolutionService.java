package com.warehouse.shipment.application.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;
import com.warehouse.shipment.application.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.application.port.secondary.PickupPointServicePort;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.VoronoiResponse;

public class ShipmentDepartmentResolutionService {

    private final PickupPointServicePort pickupPointServicePort;
    private final DepartmentServicePort departmentServicePort;
    private final PathFinderServicePort pathFinderServicePort;
    private final OperatorContextProvider operatorContextProvider;

    public ShipmentDepartmentResolutionService(final PickupPointServicePort pickupPointServicePort,
                                               final DepartmentServicePort departmentServicePort,
                                               final PathFinderServicePort pathFinderServicePort,
                                               final OperatorContextProvider operatorContextProvider) {
        this.pickupPointServicePort = pickupPointServicePort;
        this.departmentServicePort = departmentServicePort;
        this.pathFinderServicePort = pathFinderServicePort;
        this.operatorContextProvider = operatorContextProvider;
    }

    public Result<DepartmentId, ErrorCode> resolveTargetDepartmentId(
            final ShipmentCreateCommand command, final Party recipient) {
        if (command.getDeliveryMethod().isPickupPointBased()) {
            return resolvePickupPointDepartmentId(
                    command.getDeliveryPickupPointId(), ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE);
        }

        final Result<VoronoiResponse, ErrorCode> voronoiResponse =
                pathFinderServicePort.determineDeliveryDepartment(Address.from(recipient));
        if (voronoiResponse.isFailure()) {
            return Result.failure(voronoiResponse.getFailure());
        }
        final DepartmentId departmentId = departmentServicePort.getDepartmentId(
                voronoiResponse.getSuccess().getDepartmentCodeResult());
        return validateDepartmentId(departmentId, ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE);
    }

    public Result<DepartmentId, ErrorCode> resolveOriginDepartmentId(final ShipmentCreateCommand command) {
        if (command.getPickupMethod().isPickupPointBased()) {
            return resolvePickupPointDepartmentId(command.getPickupPointId(), ErrorCode.ORIGIN_DEPARTMENT_NOT_AVAILABLE);
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
