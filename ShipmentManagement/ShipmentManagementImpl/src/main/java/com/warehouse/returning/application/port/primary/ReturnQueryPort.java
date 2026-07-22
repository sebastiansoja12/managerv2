package com.warehouse.returning.application.port.primary;

import com.warehouse.returning.api.dto.ReturnDetailsDto;
import com.warehouse.returning.api.dto.ReturnPageDto;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import java.util.Optional;

public interface ReturnQueryPort {
    ReturnDetailsDto getDetails(final ReturnPackageId returnId);

    ReturnPageDto getReturns(final DepartmentId departmentId, final int page, final int size);

    Optional<ReturnDetailsDto> findByShipmentId(final ShipmentId shipmentId);
}
