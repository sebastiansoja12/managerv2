package com.warehouse.supplier.infrastructure.adapter.primary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.exception.DomainException;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.primary.SupplyPort;
import com.warehouse.supplier.domain.vo.*;
import com.warehouse.supplier.infrastructure.adapter.primary.dto.*;
import com.warehouse.supplier.infrastructure.adapter.primary.mapper.RequestMapper;
import com.warehouse.supplier.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.supplier.infrastructure.adapter.secondary.exception.InfrastructureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/suppliers")
@RestController
public class SupplierController {

    private final SupplyPort supplyPort;

    public SupplierController(final SupplyPort supplyPort) {
        this.supplyPort = supplyPort;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_CREATE', 'ROLE_MANAGER_CREATE')")
    public ResponseEntity<?> create(@RequestBody final SupplierCreateApiRequest supplierCreateApiRequest) {
        final SupplierCreateRequest request = RequestMapper.map(supplierCreateApiRequest);
        final SupplierCreateResponse response = this.supplyPort.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseMapper.map(response));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_CREATE', 'ROLE_MANAGER_CREATE')")
    public ResponseEntity<?> updateSupplier(@RequestBody final SupplierUpdateApiRequest supplierUpdateRequest) {
        final SupplierUpdateRequest request = RequestMapper.map(supplierUpdateRequest);
        final Result<Void, String> response = this.supplyPort.update(request);
        final ResponseEntity<?> responseEntity;
		if (response.isSuccess()) {
			responseEntity = ResponseEntity.ok(ResponseMapper.map(SupplierUpdateResponse.ok(request.supplierCode())));
		} else {
			responseEntity = ResponseEntity.badRequest().body(ResponseMapper
					.map(SupplierUpdateResponse.failure(request.supplierCode(), response.getFailure())));
		}
		return responseEntity;
    }

    @PutMapping("/certifications")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_CREATE', 'ROLE_MANAGER_CREATE')")
    public ResponseEntity<?> updateCertification(@RequestBody final CertificationUpdateApiRequest certificationUpdateRequest) {
        final CertificationUpdateRequest request = RequestMapper.map(certificationUpdateRequest);
        final CertificationUpdateResponse response = this.supplyPort.updateCertification(request);
        return ResponseEntity.ok(ResponseMapper.map(response));
    }

    @PutMapping("/driver-licenses")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_CREATE', 'ROLE_MANAGER_CREATE')")
    public ResponseEntity<?> updateDriverLicense(@RequestBody final DriverLicenseApiRequest driverLicenseRequest) {
        final DriverLicenseRequest request = RequestMapper.map(driverLicenseRequest);
        final DriverLicenseResponse response = this.supplyPort.updateDriverLicense(request);
        return ResponseEntity.ok(ResponseMapper.map(response));
    }

    @PutMapping("/supported-package-types")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_CREATE', 'ROLE_MANAGER_CREATE')")
    public ResponseEntity<?> updatePackageTypes(
			@RequestBody final ChangeSupportedPackageTypesApiRequest changeSupportedPackageTypeRequest) {
        final ChangeSupportedPackageTypeRequest request = RequestMapper.map(changeSupportedPackageTypeRequest);
        this.supplyPort.addPackageType(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/devices")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_CREATE', 'ROLE_MANAGER_CREATE')")
    public ResponseEntity<?> updateDevice(
            @RequestBody final ChangeSupplierDeviceApiRequest changeSupplierDeviceRequest) {
        final ChangeSupplierDeviceRequest request = RequestMapper.map(changeSupplierDeviceRequest);
        this.supplyPort.addDevice(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable final Long id) {
        final SupplierId supplierId = new SupplierId(id);
        final Supplier supplier = this.supplyPort.getOneById(supplierId);
        return ResponseEntity.ok(ResponseMapper.map(supplier));
    }

    @GetMapping("/by-suppliercode/{code}")
    public ResponseEntity<?> getOneByCode(@PathVariable final String code) {
        final SupplierCode supplierCode = new SupplierCode(code);
        final Supplier supplier = this.supplyPort.getOneByCode(supplierCode);
        return ResponseEntity.ok(ResponseMapper.map(supplier));
    }


    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleException(final DomainException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<?> handleException(final InfrastructureException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
