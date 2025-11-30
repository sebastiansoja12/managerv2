package com.warehouse.supplier.domain.service;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.supplier.domain.vo.DriverLicense;

public class DriverLicenseServiceImpl implements DriverLicenseService {


    @Override
    public Result<Void, String> validateDriverLicense(final DriverLicense driverLicense) {
        final Result<Void, String> result;
        if (driverLicense.acquiredDate().isAfter(driverLicense.drivingLicenseExpiryDate())) {
            result = Result.failure("Acquired date is after expiry date");
        }
        else if (driverLicense.number().length() < 14) {
            result = Result.failure("Wrong driver license number");
        } else {
            result = Result.success();
        }
        return result;
    }
}
