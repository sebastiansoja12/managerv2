package com.warehouse.returning.domain.model;

import java.util.List;
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

    public void revertStatus(Long parcelId, ReturnStatus returnStatus) {
        requests = requests.stream()
                .peek(returnPackage -> {
                    if (returnPackage.getParcel().getId().equals(parcelId)) {
                        returnPackage.revertStatus(returnStatus);
                    }
                })
                .collect(Collectors.toList());
    }

    public boolean isReturnTokenAvailable() {
		return requests.stream()
                .anyMatch(ReturnPackageRequest::isReturnTokenAvailable);
    }

    public List<ReturnPackageRequest> cleanReturnRequest() {
        return requests.stream()
                .filter(ReturnPackageRequest::isCancelled)
                .collect(Collectors.toList());
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
}
