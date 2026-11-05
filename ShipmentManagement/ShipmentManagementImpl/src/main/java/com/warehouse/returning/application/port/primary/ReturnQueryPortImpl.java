package com.warehouse.returning.application.port.primary;

import com.warehouse.returning.api.dto.ReturnDetailsDto;
import com.warehouse.returning.api.dto.ReturnPageDto;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.application.port.secondary.ReturningTrackManagerServicePort;
import com.warehouse.returning.application.service.ReturnDepartmentAccessService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ReturnQueryPortImpl implements ReturnQueryPort {
    private final ReturningTrackManagerServicePort returningTrackManagerServicePort;
    private final ReturnDepartmentAccessService returnDepartmentAccessService;

    public ReturnQueryPortImpl(final ReturningTrackManagerServicePort returningTrackManagerServicePort,
                               final ReturnDepartmentAccessService returnDepartmentAccessService) {
        this.returningTrackManagerServicePort = returningTrackManagerServicePort;
        this.returnDepartmentAccessService = returnDepartmentAccessService;
    }

    @Override
    public ReturnDetailsDto getDetails(final ReturnPackageId returnId) {
        final ReturnDetailsDto details = returningTrackManagerServicePort.getDetails(returnId);
        returnDepartmentAccessService.requireAccess(details.assignedDepartmentId(), details.returnedDepartmentId());
        return details;
    }

    @Override
    public ReturnPageDto getReturns(final DepartmentId departmentId, final int page, final int size) {
        returnDepartmentAccessService.requireAccess(departmentId);
        return returningTrackManagerServicePort.getReturns(departmentId, page, size);
    }

    @Override
    public Optional<ReturnDetailsDto> findByShipmentId(final ShipmentId shipmentId) {
        return returningTrackManagerServicePort.findByShipmentId(shipmentId);
    }
}
