package com.warehouse.returning.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.model.Returning;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;
import com.warehouse.returning.domain.vo.ReturnResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturnServiceImpl implements ReturnService {

    private final ReturnRepository returnRepository;

    @Override
    public ReturnResponse processReturn(ReturnRequest request) {
        final List<ReturnPackage> returnPackages = request.getRequests().stream()
                .map(this::buildReturnPackage)
                .collect(Collectors.toList());

        returnPackages.forEach(ReturnPackage::processReturn);

        final Returning returning = new Returning(returnPackages);

        final List<ProcessReturn> processReturns = returning
                .returnPackages()
                .stream()
                .map(returnRepository::save)
                .toList();

        return new ReturnResponse(processReturns);
    }

    @Override
    public ReturnResponse updateReturn(ReturnRequest request) {
        final List<ReturnPackage> returnPackages = request.getRequests().stream()
                .map(this::buildReturnPackage)
                .toList();

        returnPackages.forEach(ReturnPackage::completeReturn);

        final Returning returning = new Returning(returnPackages);

        final List<ProcessReturn> processReturns = returning
                .returnPackages()
                .stream()
                .map(returnRepository::update)
                .toList();

        return new ReturnResponse(processReturns);
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
