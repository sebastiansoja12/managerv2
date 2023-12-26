package com.warehouse.supplier.infrastructure.adapter.primary;

import java.util.List;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierAddRequest;
import com.warehouse.supplier.domain.vo.SupplierAddResponse;
import com.warehouse.supplier.domain.port.primary.SupplyPort;
import com.warehouse.supplier.dto.SupplierAddRequestDto;
import com.warehouse.supplier.infrastructure.adapter.primary.mapper.SupplierRequestMapper;
import com.warehouse.supplier.infrastructure.adapter.primary.mapper.SupplierResponseMapper;

import lombok.AllArgsConstructor;

@RequestMapping("/suppliers")
@RestController
@AllArgsConstructor
public class SupplierController {

    private final SupplyPort supplyPort;

    private final SupplierRequestMapper requestMapper = Mappers.getMapper(SupplierRequestMapper.class);

    private final SupplierResponseMapper responseMapper = Mappers.getMapper(SupplierResponseMapper.class);

    @PostMapping
    public ResponseEntity<?> addSupplier(@RequestBody List<SupplierAddRequestDto> supplier) {
        final List<SupplierAddRequest> request = requestMapper.map(supplier);
        final List<SupplierAddResponse> response = supplyPort.createMultipleSuppliers(request);
        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getSuppliers() {
        final List<Supplier> suppliers = supplyPort.findAllSuppliers();
        return new ResponseEntity<>(responseMapper.mapToDto(suppliers), HttpStatus.OK);
    }

    @GetMapping("/supplierCode/{supplierCode}")
    public ResponseEntity<?> getSupplierByCode(@PathVariable String supplierCode) {
        final Supplier supplier = supplyPort.findSupplierByCode(supplierCode);
        return new ResponseEntity<>(responseMapper.map(supplier), HttpStatus.OK);
    }

    @GetMapping("/all/supplierCode/{supplierCode}")
    public ResponseEntity<?> getSuppliersByCode(@PathVariable String supplierCode) {
        final List<Supplier> suppliers = supplyPort.findSuppliersByCode(supplierCode);
        return new ResponseEntity<>(responseMapper.mapToDto(suppliers), HttpStatus.OK);
    }

    @GetMapping("/depotCode/{depotCode}")
    public ResponseEntity<?> getSuppliersByDepotCode(@PathVariable String depotCode) {
        final List<Supplier> suppliers = supplyPort.findSuppliersByDepotCode(depotCode);
        return new ResponseEntity<>(responseMapper.mapToDto(suppliers), HttpStatus.OK);
    }


}
