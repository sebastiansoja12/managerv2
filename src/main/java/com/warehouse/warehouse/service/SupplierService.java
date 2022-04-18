package com.warehouse.warehouse.service;

import com.warehouse.warehouse.dto.SupplierDto;
import com.warehouse.warehouse.model.Supplier;
import com.warehouse.warehouse.repository.SupplierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public Supplier findBySupplierCode(String supplierCode) {
        return supplierRepository.findBySupplierCode(supplierCode);
    }

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    public Supplier save(SupplierDto supplier){
        return Supplier.builder()
                .supplierCode(supplier.getSupplierCode())
                .firstName(supplier.getFirstName())
                .lastName(supplier.getLastName())
                .telephone(supplier.getTelephone())
                .build();
    }

    public void delete(String supplierCode){
        Supplier supplierToDelete = supplierRepository.findBySupplierCode(supplierCode);
        supplierRepository.delete(supplierToDelete);
    }
}