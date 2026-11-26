package com.warehouse.supplier.api;

import com.warehouse.commonassets.identificator.OperatorId;

public interface SupplierValidityApiService {

    int invalidateDriverLicenses(final OperatorId operatorId);

    int invalidateDangerousGoodCertifications(final OperatorId operatorId);
}
