package com.warehouse.supplier.application.port.primary.command;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.supplier.domain.vo.DangerousGoodCertification;

public record CertificationUpdateCommand(SupplierCode supplierCode, DangerousGoodCertification dangerousGoodCertification) {
}
