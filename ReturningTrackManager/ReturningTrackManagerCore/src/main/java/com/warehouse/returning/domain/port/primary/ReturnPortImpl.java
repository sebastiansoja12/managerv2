package com.warehouse.returning.domain.port.primary;

import java.util.List;
import java.util.stream.Collectors;

import com.warehouse.returning.domain.exception.DepotCodeMissingException;
import com.warehouse.returning.domain.exception.ReturnTokenMissingException;
import com.warehouse.returning.domain.exception.UsernameMissingException;
import com.warehouse.returning.domain.model.Parcel;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.service.ReturnService;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;
import com.warehouse.returning.domain.vo.ReturnResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@AllArgsConstructor
@Slf4j
public class ReturnPortImpl implements ReturnPort {

    private final ReturnService returnService;

    @Override
    public ReturnResponse process(ReturnRequest request) {

        validateRequest(request);

        request.assignDepotToReturnPackages();

        request.assignUserToReturnPackages();

        final List<Parcel> cancelledParcels = request.cleanReturnRequest().stream()
                .map(ReturnPackageRequest::getParcel)
                .collect(Collectors.toList());

        logCancelledParcels(cancelledParcels);

        cancelledParcels.forEach(parcel -> {
            logRevertStatus(parcel.getId());
            request.revertStatus(parcel.getId(), ReturnStatus.CREATED);
        });

        if (!request.isReturnTokenAvailable()) {
            final List<ProcessReturn> processReturns = request.getRequests()
                    .stream()
                    .filter(ReturnPackageRequest::isReturnNotTokenAvailable)
                    .map(this::createProcessReturn)
                    .toList();
            return new ReturnResponse(processReturns);
        }

        if (request.isProcessing()) {
            final ReturnRequest processingRequest = ReturnRequest.builder()
                    .requests(request.filterProcessingReturns())
                    .username(request.getUsername())
                    .depotCode(request.getDepotCode())
                    .build();
            return returnService.updateReturn(processingRequest);
        }
        return returnService.processReturn(request);
    }

    @Override
    public ReturnModel getReturn(ReturnId returnId) {
        return returnService.getReturn(returnId);
    }

    @Override
    public void delete(ReturnId returnId) {
        returnService.deleteReturn(returnId);
    }

    private void validateRequest(ReturnRequest request) {
        if (request.isUserMissing()) {
            throw new UsernameMissingException(8081, "Username is missing");
        }
        if (request.isDepotCodeMissing()) {
            throw new DepotCodeMissingException(8082, "Depot code is missing");
        }
    }

    private void logRevertStatus(Long id) {
        log.info("Reverting return status for parcel [ID:{}]", id);
    }

    private void logCancelledParcels(List<Parcel> cancelledParcels) {
        log.warn("Given parcels were cancelled: {}", cancelledParcels);
    }

    private ProcessReturn createProcessReturn(ReturnPackageRequest process) {
        return new ProcessReturn(process.getParcel().getId(), "Return token not available");
    }
}
