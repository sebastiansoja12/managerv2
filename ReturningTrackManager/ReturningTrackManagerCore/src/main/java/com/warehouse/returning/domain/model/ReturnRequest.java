package com.warehouse.returning.domain.model;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReturnRequest {
    private List<ReturnPackageRequest> requests;
    private String depotCode;
    private String username;

    public boolean isReturnTokenNotAvailable() {
		return requests.stream()
                .anyMatch(ReturnPackageRequest::isReturnNotTokenAvailable);
    }

    public void assignDepotToReturnPackages() {
        requests = requests.stream()
                .peek(returnPackageRequest -> returnPackageRequest.updateDepot(depotCode))
                .collect(Collectors.toList());
    }

    public void assignUserToReturnPackages() {
        requests = requests.stream()
                .peek(returnPackageRequest -> returnPackageRequest.updateUser(username))
                .collect(Collectors.toList());
    }

    public boolean isUserMissing() {
        return StringUtils.isEmpty(username);
    }

    public boolean isDepotCodeMissing() {
        return StringUtils.isEmpty(depotCode);
    }

    public void filterProcessingReturns() {
        requests = requests.stream()
                .filter(ReturnPackageRequest::isProcessing)
                .peek(ReturnPackageRequest::completeReturn)
                .collect(Collectors.toList());
    }

    public void filterCreatedReturns() {
        requests = requests.stream()
                .filter(ReturnPackageRequest::isCreated)
                .peek(ReturnPackageRequest::processReturn)
                .collect(Collectors.toList());
    }

    public void revertCancelledParcels(List<ReturnPackageRequest> requests) {
        this.requests = requests;
    }

    public boolean isCancelled() {
        return requests.stream().anyMatch(ReturnPackageRequest::isCancelled);
    }

    public boolean isProcessing() {
        return checkStatus(ReturnPackageRequest::isProcessing);
    }

    public boolean isCreated() {
        return checkStatus(ReturnPackageRequest::isCreated);
    }

    public boolean isCompleted() {
        return checkStatus(ReturnPackageRequest::isCompleted);
    }

    private boolean checkStatus(Predicate<ReturnPackageRequest> condition) {
        return requests.stream().anyMatch(condition);
    }

    public void filterCancelledReturns() {
        requests = requests.stream()
                .filter(ReturnPackageRequest::isCancelled)
                .collect(Collectors.toList());
    }
}
