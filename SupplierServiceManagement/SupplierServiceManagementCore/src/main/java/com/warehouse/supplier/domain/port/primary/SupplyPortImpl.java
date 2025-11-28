package com.warehouse.supplier.domain.port.primary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.event.SupplierCreated;
import com.warehouse.supplier.domain.exception.SupplierAlreadyExistsException;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.registry.DomainContext;
import com.warehouse.supplier.domain.service.SupplierCodeGeneratorService;
import com.warehouse.supplier.domain.service.SupplierService;
import com.warehouse.supplier.domain.service.SupplierValidatorService;
import com.warehouse.supplier.domain.vo.SupplierCreateRequest;
import com.warehouse.supplier.domain.vo.SupplierCreateResponse;

import java.time.Instant;

public class SupplyPortImpl implements SupplyPort {

    private final SupplierService supplierService;

    private final SupplierCodeGeneratorService generatorService;

    private final SupplierValidatorService validatorService;

    public SupplyPortImpl(final SupplierService supplierService,
                          final SupplierCodeGeneratorService generatorService,
                          final SupplierValidatorService validatorService) {
        this.supplierService = supplierService;
        this.generatorService = generatorService;
        this.validatorService = validatorService;
    }

    @Override
    public SupplierCreateResponse create(final SupplierCreateRequest supplierCreateRequest) {
        final SupplierCode supplierCode = this.generatorService.generate(supplierCreateRequest.supplierCode());
        validateNotExists(supplierCode);
        final String firstName = supplierCreateRequest.firstName();
        final String lastName = supplierCreateRequest.lastName();
        final String telephoneNumber = supplierCreateRequest.telephoneNumber();
        final SupplierId supplierId = this.supplierService.nextSupplierId();
		final Supplier supplier = new Supplier(supplierId, supplierCode, firstName, lastName, telephoneNumber);

        this.supplierService.create(supplier);

        DomainContext.eventPublisher()
                .publishEvent(new SupplierCreated(supplier.snapshot(), Instant.now()));

        return new SupplierCreateResponse(supplier.supplierCode());
    }

    @Override
    public Supplier getOneById(final SupplierId supplierId) {
        return supplierService.findById(supplierId);
    }

    @Override
    public Supplier getOneByCode(final SupplierCode supplierCode) {
        return supplierService.findByCode(supplierCode);
    }

    private void validateNotExists(final SupplierCode supplierCode) {
        final Result<Void, String> result = this.validatorService.validateSupplierCode(supplierCode);
        if (result.isFailure()) {
            throw new SupplierAlreadyExistsException(result.getFailure());
        }
    }
}
