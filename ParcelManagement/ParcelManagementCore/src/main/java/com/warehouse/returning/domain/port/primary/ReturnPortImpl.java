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

        logRevertStatus();

        cancelledParcels.forEach(parcel -> request.revertStatus(parcel.getId(), ReturnStatus.CREATED));

        if (!request.isReturnTokenAvailable()) {
            throw new ReturnTokenMissingException(8080, "Return token is missing");
        }

        return returnService.processReturning(request);
    }

    private void validateRequest(ReturnRequest request) {
        if (request.isUserMissing()) {
            throw new UsernameMissingException(8081, "Username is missing");
        }
        if (request.isDepotCodeMissing()) {
            throw new DepotCodeMissingException(8082, "Depot code is missing");
        }
    }

    private void logRevertStatus() {
        log.info("Reverting return status for given parcels...");
    }

    private void logCancelledParcels(List<Parcel> cancelledParcels) {
        log.warn("Given parcels were cancelled: {}", cancelledParcels);
    }
}
