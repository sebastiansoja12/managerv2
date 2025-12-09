package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.identificator.SupplierCode;

public record CertificationUpdateRequest(SupplierCode supplierCode, DangerousGoodCertification dangerousGoodCertification) {
}
