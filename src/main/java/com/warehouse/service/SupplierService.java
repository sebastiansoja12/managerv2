package com.warehouse.service;

import com.warehouse.dto.SupplierDto;
import com.warehouse.exceptions.DepotNotFound;
import com.warehouse.mapper.SupplierMapper;
import com.warehouse.entity.Depot;
import com.warehouse.entity.Supplier;
import com.warehouse.repository.DepotRepository;
import com.warehouse.repository.SupplierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final DepotRepository depotRepository;

    private final SupplierMapper supplierMapper;

    public Supplier findBySupplierCode(String supplierCode) {
        return supplierRepository.findBySupplierCode(supplierCode);
    }

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    public Supplier save(SupplierDto supplier){
        return supplierRepository.save(Supplier.builder()
                .supplierCode(supplier.getSupplierCode())
                .firstName(supplier.getFirstName())
                .lastName(supplier.getLastName())
                .telephone(supplier.getTelephone())
                .depot(getDepotByCode(supplier))
                .build());
    }

    public List<Supplier> saveMultipleSuppliers(List<SupplierDto> supplier) {
        return supplierRepository.saveAll(supplierMapper.mapToList(supplier));
    }

    public List<Supplier> saveMultipleSuppliersDisabled(List<Supplier> suppliers) {
        return supplierRepository.saveAll(suppliers);
    }

    public void delete(String supplierCode){
        Supplier supplierToDelete = supplierRepository.findBySupplierCode(supplierCode);
        supplierRepository.delete(supplierToDelete);
    }

    public Depot getDepotByCode(SupplierDto supplier){
        return depotRepository.findByDepotCode(supplier.getDepotCode()).orElseThrow(() ->
                new DepotNotFound("Given depot doesnt exist!"));
    }
}