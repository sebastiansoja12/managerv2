package com.warehouse.returning.domain.model;


import java.util.List;
import java.util.stream.Collectors;


public record Returning(List<ReturnPackage> returnPackages) {
    public List<Long> extractParcelIds() {
        return returnPackages.stream()
                .map(ReturnPackage::getParcelId)
                .collect(Collectors.toList());
    }

    public void processReturnPackage() {
        returnPackages.forEach(ReturnPackage::processReturn);
    }

    public void revertStatus(Long parcelId, ReturnStatus returnStatus) {
        returnPackages.stream()
                .filter(returnPackage -> returnPackage.getParcelId().equals(parcelId))
                .map(returnPackage -> returnPackage.revertStatus(returnStatus))
                .collect(Collectors.toList());
    }

    public boolean isReturnTokenAvailable(Long parcelId) {
        return returnPackages.stream()
                .filter(returnPackage -> returnPackage.getParcelId().equals(parcelId))
                .map(ReturnPackage::getReturnToken)
                .noneMatch(String::isEmpty);
    }


}
