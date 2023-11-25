package com.warehouse.returning.domain.service;

import java.util.List;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturnServiceImpl implements ReturnService {

    private final ReturnRepository returnRepository;

    @Override
    public List<ProcessReturn> processReturn(ReturnRequest request) {
        return request
                .getRequests()
                .stream()
                .map(returnRepository::save)
                .toList();
    }

    @Override
    public List<ProcessReturn> updateReturn(ReturnRequest request) {
        return request
                .getRequests()
                .stream()
                .map(returnRepository::update)
                .toList();
    }

    @Override
    public ReturnPackageRequest unlockReturn(ReturnPackageRequest request) {
        final ReturnStatus returnStatus = returnRepository.unlockReturn(request.getParcel().getId(),
                request.getReturnToken());

        return ReturnPackageRequest.builder()
                .supplierCode(request.getSupplierCode())
                .returnStatus(returnStatus)
                .returnToken(request.getReturnToken())
                .parcel(request.getParcel())
                .reason(request.getReason())
                .depotCode(request.getDepotCode())
                .username(request.getUsername())
                .build();
    }

    @Override
    public ReturnModel getReturn(ReturnId returnId) {
        return returnRepository.get(returnId);
    }

    @Override
    public void deleteReturn(ReturnId returnId) {
        returnRepository.delete(returnId);
    }

    private ReturnPackage buildReturnPackage(ReturnPackageRequest returnPackageRequest) {
        return ReturnPackage.builder()
                .returnToken(returnPackageRequest.getReturnToken())
                .returnStatus(returnPackageRequest.getReturnStatus())
                .parcelId(returnPackageRequest.getParcel().getId())
                .reason(returnPackageRequest.getReason())
                .supplierCode(returnPackageRequest.getSupplierCode())
                .depotCode(returnPackageRequest.getDepotCode())
                .username(returnPackageRequest.getUsername())
                .build();
    }
}
