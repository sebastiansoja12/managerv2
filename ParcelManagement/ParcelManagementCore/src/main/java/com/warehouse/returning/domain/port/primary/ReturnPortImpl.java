package com.warehouse.returning.domain.port.primary;

import com.warehouse.returning.domain.model.*;
import com.warehouse.returning.domain.service.ReturnService;
import com.warehouse.returning.domain.vo.ReturnResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Slf4j
public class ReturnPortImpl implements ReturnPort {

    private final ReturnService returnService;

    @Override
    public ReturnResponse process(ReturnRequest request) {

        final List<Parcel> cancelledParcels = request.cleanReturnRequest().stream()
                .map(ReturnPackageRequest::getParcel)
                .collect(Collectors.toList());

        logCancelledParcels(cancelledParcels);

        logRevertStatus();

        cancelledParcels.forEach(parcel -> request.revertStatus(parcel.getId(), ReturnStatus.CREATED));

        final boolean isAvailable = request.isReturnTokenAvailable();

        if (!isAvailable) {
            throw new RuntimeException("error");
        }

        return returnService.processReturning(request);
    }

    private void logRevertStatus() {
        log.info("Reverting return status for given parcels...");
    }

    private void logCancelledParcels(List<Parcel> cancelledParcels) {
        log.warn("Given parcels were cancelled: {}", cancelledParcels);
    }
}
