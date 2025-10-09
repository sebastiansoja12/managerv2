package com.warehouse.returning.domain.port.primary;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.port.secondary.RouteLogServicePort;
import com.warehouse.returning.domain.service.ReturnService;
import com.warehouse.returning.domain.vo.*;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ReturnPortImpl implements ReturnPort {

    private final ReturnService returnService;

    private final RouteLogServicePort routeLogServicePort;

    private final Logger log = LoggerFactory.getLogger(ReturnPort.class);

    @Override
    public ReturnResponse process(final ReturnRequest request) {
        final List<ProcessReturn> processReturns = new ArrayList<>();
        final ReturnToken returnToken = null;
        final List<ReturnPackageRequest> returnPackageRequests = request.getRequests();

        for (final ReturnPackageRequest returnPackageRequest : returnPackageRequests) {
            final ReturnPackageId returnPackageId = this.returnService.nextReturnPackageId();
			final ReturnPackage returnPackage = new ReturnPackage(returnPackageId, returnPackageRequest.getShipmentId(),
					returnPackageRequest.getReason(), returnToken, returnPackageRequest.getDepartmentCode(),
					request.getIssuerDepartmentCode(), returnPackageRequest.getUserId(), request.getIssuerUserId(),
					returnPackageRequest.getReasonCode());
            this.returnService.saveOrUpdate(returnPackage);

            processReturns.add(ProcessReturn.from(returnPackage));
        }

        return new ReturnResponse(processReturns);
    }

    @Override
    public ReturnModel getReturn(ReturnId returnId) {
        return returnService.getReturn(returnId);
    }

    @Override
    public void delete(final ReturnPackageId returnPackageId) {
        returnService.deleteReturn(returnPackageId);
    }
}
