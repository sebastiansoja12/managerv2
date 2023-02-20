package com.warehouse.supplier.infrastructure.adapter.primary;

import com.warehouse.supplier.SupplyService;
import com.warehouse.supplier.dto.SupplierAddRequest;
import com.warehouse.supplier.dto.SupplierDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/suppliers")
@RestController
@AllArgsConstructor
public class SupplierController {

    private final SupplyService service;


    @PostMapping
    public void addSupplier(@RequestBody SupplierAddRequest supplier) {
        service.createSupplier(supplier);
    }

    @PostMapping("/multiple")
    public void addMultipleSuppliers(@RequestBody List<SupplierAddRequest> suppliers) {
        service.createMultipleSuppliers(suppliers);
    }
    @GetMapping("/all")
    public List<SupplierDto> getSuppliers() {
        return service.findAllSuppliers();
    }

    @GetMapping("/supplierCode/{supplierCode}")
    public SupplierDto getSupplierByCode(@PathVariable String supplierCode) {
        return service.findSupplierBySupplierCode(supplierCode);
    }

    @GetMapping("/depotCode/{depotCode}")
    public List<SupplierDto> getSuppliersByDepotCode(@PathVariable String depotCode) {
        return service.findSuppliersByDepotCode(depotCode);
    }


}
