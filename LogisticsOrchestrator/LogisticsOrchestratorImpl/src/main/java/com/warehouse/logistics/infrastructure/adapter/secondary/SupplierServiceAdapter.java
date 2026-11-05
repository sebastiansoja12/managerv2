package com.warehouse.logistics.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.logistics.application.port.secondary.SupplierServicePort;
import com.warehouse.logistics.domain.vo.SupplierAssignment;
import com.warehouse.supplier.api.SupplierApiService;

public class SupplierServiceAdapter implements SupplierServicePort {

    private final SupplierApiService supplierApiService;

    private final OperatorContextProvider operatorContextProvider;

    public SupplierServiceAdapter(final SupplierApiService supplierApiService,
                                  final OperatorContextProvider operatorContextProvider) {
        this.supplierApiService = supplierApiService;
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public Optional<SupplierAssignment> findCurrentSupplier() {
        return operatorContextProvider.currentContext()
                .map(OperatorDetails::userId)
                .flatMap(supplierApiService::findProfileByUserId)
                .map(profile -> new SupplierAssignment(profile.supplierId(), profile.vehicleId()));
    }
}
