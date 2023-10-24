package com.warehouse.returning.domain.model;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

import static com.warehouse.returning.domain.model.ReturnStatus.CANCELLED;

@Data
public class ReturnRequest {
    private List<ReturnPackageRequest> requests;
    private String depotCode;

    public List<Long> extractParcelIds() {
        return requests.stream()
                .map(ReturnPackageRequest::getParcel)
                .map(Parcel::getId)
                .collect(Collectors.toList());
    }

    public void processReturnPackage(String reason) {
        requests
                .forEach(returnPackage -> returnPackage.processReturn(reason));
    }

    public void revertStatus(Long parcelId, ReturnStatus returnStatus) {
        requests = requests.stream()
                .filter(returnPackage -> returnPackage.getParcel().getId().equals(parcelId))
                .map(returnPackage -> returnPackage.revertStatus(returnStatus))
                .collect(Collectors.toList());
    }

    public boolean isReturnTokenAvailable() {
        return requests.stream()
                .map(ReturnPackageRequest::getReturnToken)
                .noneMatch(String::isEmpty);
    }

    public boolean isCancelled() {
        return requests.stream()
                .map(ReturnPackageRequest::getReturnStatus)
                .anyMatch(this::isCancelled);
    }

    public List<ReturnPackageRequest> cleanReturnRequest() {
        return requests.stream()
                .filter(ReturnPackageRequest::isCancelled)
                .collect(Collectors.toList());
    }

    public void revertStatus() {
        requests = requests.stream()
                .map(returnPackageRequest -> returnPackageRequest.revertStatus(ReturnStatus.CREATED))
                .collect(Collectors.toList());
    }

    private boolean isCancelled(ReturnStatus returnStatus) {
        return CANCELLED.equals(returnStatus);
    }
}
