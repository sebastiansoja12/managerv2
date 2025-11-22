package com.warehouse.supplier.domain.port.primary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.service.SupplierCodeGeneratorService;
import com.warehouse.supplier.domain.service.SupplierService;
import com.warehouse.supplier.domain.service.SupplierValidatorService;
import com.warehouse.supplier.domain.vo.SupplierCreateRequest;
import com.warehouse.supplier.domain.vo.SupplierCreateResponse;

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
        final SupplierCode supplierCode = generatorService.generate(supplierCreateRequest.supplierCode());
        validateNotExists(supplierCode);
        final String firstName = supplierCreateRequest.firstName();
        final String lastName = supplierCreateRequest.lastName();
        final String telephoneNumber = supplierCreateRequest.telephoneNumber();
        final SupplierId supplierId = this.supplierService.nextSupplierId();
		final Supplier supplier = new Supplier(supplierId, supplierCode, firstName, lastName, telephoneNumber);

        this.supplierService.create(supplier);

        return new SupplierCreateResponse(supplier.supplierCode());
    }

    private void validateNotExists(final SupplierCode supplierCode) {
        final Result<Void, String> result = this.validatorService.validateSupplierCode(supplierCode);
        if (result.isFailure()) {
            throw new IllegalArgumentException(result.getFailure());
        }
    }

    @Override
    public Supplier getOneById(final SupplierId supplierId) {
        return supplierService.findById(supplierId);
    }

    @Override
    public Supplier getOneByCode(final SupplierCode supplierCode) {
        return supplierService.findByCode(supplierCode);
    }
}
