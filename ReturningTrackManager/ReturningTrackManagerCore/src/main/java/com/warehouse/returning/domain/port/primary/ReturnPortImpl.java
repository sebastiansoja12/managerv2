package com.warehouse.returning.domain.port.primary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warehouse.returning.domain.exception.DepotCodeMissingException;
import com.warehouse.returning.domain.exception.UsernameMissingException;
import com.warehouse.returning.domain.model.Parcel;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.service.ReturnService;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;
import com.warehouse.returning.domain.vo.ReturnResponse;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class ReturnPortImpl implements ReturnPort {

    private final ReturnService returnService;

    private final Logger log = LoggerFactory.getLogger(ReturnPort.class);

    @Override
    public ReturnResponse process(ReturnRequest request) {

        final List<ProcessReturn> processReturns = new ArrayList<>();

        validateRequest(request);

        request.assignDepotToReturnPackages();

        request.assignUserToReturnPackages();

        if (request.isReturnTokenNotAvailable()) {
            final List<ProcessReturn> processReturnsWithoutReturnToken = request.getRequests()
                    .stream()
                    .filter(ReturnPackageRequest::isReturnNotTokenAvailable)
                    .map(this::createProcessReturn)
                    .toList();
            request.getRequests().removeIf(ReturnPackageRequest::isReturnNotTokenAvailable);
            processReturns.addAll(processReturnsWithoutReturnToken);
        }

        if (request.isProcessing()) {
            request.filterProcessingReturns();
            processProcessingReturns(request, processReturns);
        }

        if (request.isCreated()) {
            request.filterCreatedReturns();
            processCreatedReturn(request, processReturns);
        }

        if (request.isCompleted()) {
            logCompletingReturn(request);
        }

        if (request.isCancelled()) {
            request.filterCancelledReturns();
            processCancelledReturn(request);
        }

        return new ReturnResponse(processReturns);
    }

    @Override
    public ReturnModel getReturn(ReturnId returnId) {
        return returnService.getReturn(returnId);
    }

    @Override
    public void delete(ReturnId returnId) {
        returnService.deleteReturn(returnId);
    }

    private void processCancelledReturn(ReturnRequest request) {
        final List<ReturnPackageRequest> requests = request
                .getRequests()
                .stream()
                .peek(this::logCancellingReturn)
                .map(returnService::unlockReturn)
                .toList();
        request.revertCancelledParcels(requests);
    }

    private void processProcessingReturns(ReturnRequest request, List<ProcessReturn> processReturns) {
        logProcessingReturns(request);
        final List<ProcessReturn> updatedReturns = returnService.updateReturn(request);
        processReturns.addAll(updatedReturns);
    }

    private void processCreatedReturn(ReturnRequest request, List<ProcessReturn> processReturns) {
        logCreatedReturns(request);
        final List<ProcessReturn> createdReturns = returnService.processReturn(request);
        processReturns.addAll(createdReturns);
    }

    private void logCreatedReturns(ReturnRequest request) {
        log.info("Processing return for parcels: [{}]",
                request.getRequests().stream()
                        .filter(ReturnPackageRequest::isCreated)
                        .map(ReturnPackageRequest::getParcel)
                        .filter(Objects::nonNull)
                        .map(Parcel::getId)
                        .collect(Collectors.toList()));
    }

    private void logCompletingReturn(ReturnRequest request) {
        log.info("Return already completed for parcels: [{}]",
                request.getRequests().stream()
                        .filter(ReturnPackageRequest::isCompleted)
                        .map(ReturnPackageRequest::getParcel)
                        .filter(Objects::nonNull)
                        .map(Parcel::getId)
                        .collect(Collectors.toList()));
    }

    private void logProcessingReturns(ReturnRequest request) {
        log.info("Completing return for parcels: [{}]",
                request.getRequests().stream()
                        .filter(ReturnPackageRequest::isProcessing)
                        .map(ReturnPackageRequest::getParcel)
                        .filter(Objects::nonNull)
                        .map(Parcel::getId)
                        .collect(Collectors.toList()));
    }

    private void logCancellingReturn(ReturnPackageRequest request) {
        log.info("Cancelled return for parcel: [{}]", request.getParcel().getId());
    }

    private void validateRequest(ReturnRequest request) {
        if (request.isUserMissing()) {
            throw new UsernameMissingException(406, "Username is missing");
        }
        if (request.isDepotCodeMissing()) {
            throw new DepotCodeMissingException(406, "Depot code is missing");
        }
    }

    private ProcessReturn createProcessReturn(ReturnPackageRequest process) {
        return new ProcessReturn(process.getParcel().getId(), null,"Return token not available");
    }
}
