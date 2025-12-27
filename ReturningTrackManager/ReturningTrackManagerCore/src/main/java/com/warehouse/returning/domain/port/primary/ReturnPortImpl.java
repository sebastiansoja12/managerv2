package com.warehouse.returning.domain.port.primary;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.service.ReturnService;
import com.warehouse.returning.domain.service.ReturnTokenGeneratorService;
import com.warehouse.returning.domain.vo.*;

public class ReturnPortImpl implements ReturnPort {

    private final ReturnService returnService;
    
    private final ReturnTokenGeneratorService returnTokenGeneratorService;

    private final Logger log = LoggerFactory.getLogger(ReturnPort.class);

    public ReturnPortImpl(final ReturnService returnService,
                          final ReturnTokenGeneratorService returnTokenGeneratorService) {
        this.returnService = returnService;
        this.returnTokenGeneratorService = returnTokenGeneratorService;
    }

    @Override
    public ReturnResponse process(final ReturnRequest request) {
        final List<ProcessReturn> processReturns = new ArrayList<>();
        final List<ReturnPackageRequest> returnPackageRequests = request.getRequests();

        for (final ReturnPackageRequest returnPackageRequest : returnPackageRequests) {
            final ReturnPackage existingPackage = this.returnService.findByShipmentId(returnPackageRequest.getShipmentId());
            if (existingPackage != null) {
                log.info("Return for shipment {} already exists", returnPackageRequest.getShipmentId().value());
                processReturns.add(ProcessReturn.from(existingPackage));
                continue;
            }
            final ReturnPackageId returnPackageId = this.returnService.nextReturnPackageId();
			final ReturnToken returnToken = this.returnTokenGeneratorService.generateToken(
					returnPackageRequest.getShipmentId(), returnPackageRequest.getDepartmentCode(),
					returnPackageRequest.getUserId());
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
    public void changeReasonCode(final ChangeReasonCodeRequest request) {
        this.returnService.changeReasonCode(request.returnPackageId(), request.reasonCode());
    }

    @Override
    public void complete(final ReturnPackageId returnPackageId) {
        this.returnService.completeReturn(returnPackageId);
    }

    @Override
    public ReturnPackage getReturn(final ReturnPackageId returnId) {
        return this.returnService.getReturn(returnId);
    }

    @Override
    public void delete(final ReturnPackageId returnPackageId) {
        returnService.deleteReturn(returnPackageId);
    }
}
