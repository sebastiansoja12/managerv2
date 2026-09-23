package com.warehouse.returning.application.port.primary;

import com.warehouse.returning.api.dto.ReturnDetailsDto;
import com.warehouse.returning.api.dto.ReturnPageDto;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.application.port.secondary.ReturningTrackManagerServicePort;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ReturnQueryPortImpl implements ReturnQueryPort {
    private final ReturningTrackManagerServicePort returningTrackManagerServicePort;

    public ReturnQueryPortImpl(final ReturningTrackManagerServicePort returningTrackManagerServicePort) {
        this.returningTrackManagerServicePort = returningTrackManagerServicePort;
    }

    @Override
    public ReturnDetailsDto getDetails(final ReturnPackageId returnId) {
        return returningTrackManagerServicePort.getDetails(returnId);
    }

    @Override
    public ReturnPageDto getReturns(final DepartmentId departmentId, final int page, final int size) {
        return returningTrackManagerServicePort.getReturns(departmentId, page, size);
    }

    @Override
    public Optional<ReturnDetailsDto> findByShipmentId(final ShipmentId shipmentId) {
        return returningTrackManagerServicePort.findByShipmentId(shipmentId);
    }
}
