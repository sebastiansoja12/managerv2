package com.warehouse.supplier.domain.service;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.supplier.domain.vo.DriverLicense;

public interface DriverLicenseService {
    Result<Void, String> validateDriverLicense(final DriverLicense driverLicense);
}
