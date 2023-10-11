package com.warehouse.supplier.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierModelRequest;
import com.warehouse.supplier.domain.model.SupplierModelResponse;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import com.warehouse.supplier.domain.port.secondary.SupplierServicePort;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    private final SupplierServicePort servicePort;

    @Override
    public Supplier create(Supplier supplier) {
        return supplierRepository.create(supplier);
    }

    @Override
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    @Override
    public List<Supplier> createMultipleSuppliers(List<Supplier> suppliers) {
        final List<SupplierModelRequest> supplierList = supplierRepository.createMultipleSuppliers(suppliers);
        final List<SupplierModelResponse> response = servicePort.createSuppliers(supplierList);

        /** INPL-6150
         * return response
                .stream()
                .map(this::mapToSupplier)
                .collect(Collectors.toList());
         **/
        return supplierList.stream()
                .map(this::mapToSupplier)
                .collect(Collectors.toList());
    }

    @Override
    public List<Supplier> findSuppliersByDepotCode(String depotCode) {
        return supplierRepository.findByDepotCode(depotCode);
    }

    @Override
    public List<Supplier> findSuppliersBySupplierCode(String supplierCode) {
        return supplierRepository.findBySupplierCode(supplierCode);
    }

    @Override
    public Supplier findSupplierByCode(String supplierCode) {
        return supplierRepository.findByCode(supplierCode);
    }

    private Supplier mapToSupplier(SupplierModelRequest supplierModelRequest) {
        return Supplier.builder()
                .firstName(supplierModelRequest.getFirstName())
                .telephone(supplierModelRequest.getTelephone())
                .lastName(supplierModelRequest.getTelephone())
                .supplierCode(supplierModelRequest.getSupplierCode())
                .depotCode(supplierModelRequest.getDepotCode())
                .build();
    }
}
