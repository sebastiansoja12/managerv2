package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.secondary.OperatorJobServicePort;
import com.warehouse.platformadministration.domain.enumeration.OperatorJobType;
import com.warehouse.platformadministration.domain.vo.OperatorJobRun;
import com.warehouse.supplier.api.SupplierValidityApiService;

public class DangerousGoodValidationJobServiceAdapter implements OperatorJobServicePort {

    private final SupplierValidityApiService supplierValidityApiService;

    public DangerousGoodValidationJobServiceAdapter(final SupplierValidityApiService supplierValidityApiService) {
        this.supplierValidityApiService = supplierValidityApiService;
    }

    @Override
    public OperatorJobType jobType() {
        return OperatorJobType.DANGEROUS_GOOD_CERTIFICATION_VALIDATION;
    }

    @Override
    public OperatorJobRun run(final OperatorId operatorId) {
        return new OperatorJobRun(jobType(), false,
                supplierValidityApiService.invalidateDangerousGoodCertifications(operatorId), null);
    }
}
