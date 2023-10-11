package com.warehouse.supplier.domain.port.primary;

import java.util.List;
import java.util.stream.Collectors;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierAddRequest;
import com.warehouse.supplier.domain.model.SupplierAddResponse;
import com.warehouse.supplier.domain.service.SupplierCodeGeneratorService;
import com.warehouse.supplier.domain.service.SupplierService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SupplyPortImpl implements SupplyPort {

    private final SupplierService service;

    private final SupplierCodeGeneratorService generatorService;

    @Override
    public List<Supplier> findAllSuppliers() {
        return service.findAll();
    }

    @Override
    public List<SupplierAddResponse> createMultipleSuppliers(List<SupplierAddRequest> supplierAddRequests) {
		final List<Supplier> suppliers = supplierAddRequests.stream()
                .map(this::buildSupplierFromRequest)
                .toList();

        suppliers.forEach(this::assignSupplierCode);

		final List<Supplier> supplierList = service.createMultipleSuppliers(suppliers);

        return supplierList
                .stream()
                .map(this::mapToAddResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Supplier> findSuppliersByDepotCode(String depotCode) {
        return service.findSuppliersByDepotCode(depotCode);
    }

    @Override
    public Supplier findSupplierByCode(String supplierCode) {
        return service.findSupplierByCode(supplierCode);
    }

    @Override
    public List<Supplier> findSuppliersByCode(String supplierCode) {
        return service.findSuppliersBySupplierCode(supplierCode);
    }

    private Supplier buildSupplierFromRequest(SupplierAddRequest supplierRequest) {
        return Supplier.builder()
                .depotCode(supplierRequest.getDepotCode())
                .firstName(supplierRequest.getFirstName())
                .lastName(supplierRequest.getLastName())
                .telephone(supplierRequest.getTelephone())
                .build();
    }

    private SupplierAddResponse mapToAddResponse(Supplier supplier) {
        return new SupplierAddResponse(supplier);
    }

    private void assignSupplierCode(Supplier supplier) {
        final String supplierCode = generatorService.generate();
        supplier.setSupplierCode(supplierCode);
    }
}
