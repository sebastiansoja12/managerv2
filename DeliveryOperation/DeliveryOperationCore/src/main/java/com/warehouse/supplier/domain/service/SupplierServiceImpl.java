package com.warehouse.supplier.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierModelRequest;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public Supplier create(Supplier supplier) {
        return supplierRepository.create(supplier);
    }

    @Override
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    @Override
    public Supplier update(Supplier supplier) {
        return supplierRepository.update(supplier);
    }

    @Override
    public List<Supplier> createMultipleSuppliers(List<Supplier> suppliers) {
        final List<SupplierModelRequest> supplierList = supplierRepository.createMultipleSuppliers(suppliers);
        return supplierList.stream()
                .map(this::mapToSupplier)
                .collect(Collectors.toList());
    }

    @Override
    public List<Supplier> findSuppliersByDepotCode(String depotCode) {
        return supplierRepository.findByDepotCode(depotCode);
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
                .departmentCode(supplierModelRequest.getDepartmentCode())
                .build();
    }
}
