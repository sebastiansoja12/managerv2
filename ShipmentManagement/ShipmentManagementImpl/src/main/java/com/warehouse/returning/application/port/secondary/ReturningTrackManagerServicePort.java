package com.warehouse.returning.application.port.secondary;

import com.warehouse.returning.api.dto.ReturnDetailsDto;
import com.warehouse.returning.api.dto.ReturnPageDto;
import com.warehouse.commonassets.identificator.DepartmentId;
import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.domain.vo.CreateReturnRequest;
import com.warehouse.returning.domain.vo.CreatedReturn;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnState;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.domain.enumeration.ReasonCode;

import java.util.List;
import java.util.UUID;

public interface ReturningTrackManagerServicePort {
    List<CreatedReturn> create(final CreateReturnRequest request);

    ReturnState get(final ReturnPackageId returnId);

    ReturnState getByShipmentId(final ShipmentId shipmentId);

    void startProcessing(final ReturnPackageId returnId);

    void complete(final ReturnPackageId returnId);

    void cancel(final ReturnPackageId returnId);

    void changeReasonCode(final ReturnPackageId returnId, final ReasonCode reasonCode);

    boolean validateToken(final ShipmentId shipmentId, final ReturnToken returnToken);

    ReturnState pickup(final ReturnPackageId returnId, final UUID pickupId, final DepartmentId scanDepartmentId);

    ReturnState linkReturnShipment(final ReturnPackageId returnId, final ShipmentId returnShipmentId);

    ReturnDetailsDto getDetails(final ReturnPackageId returnId);

    ReturnPageDto getReturns(final DepartmentId departmentId, final int page, final int size);

    Optional<ReturnDetailsDto> findByShipmentId(final ShipmentId shipmentId);
}
