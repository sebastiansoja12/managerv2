package com.warehouse.terminal.request;

import com.warehouse.terminal.dto.*;

public record DeviceCreateRequestDto(UserIdDto userId,
                                     DepartmentCodeDto departmentCode,
                                     SupplierCodeDto supplierCode,
                                     VersionDto version,
                                     DeviceUserTypeDto deviceUserType,
                                     DeviceTypeDto deviceType,
                                     ScanTypeDto scanType,
                                     ScannerTypeDto scannerType,
                                     DeviceIdentityRequestDto identity,
                                     DeviceHardwareRequestDto hardware,
                                     DeviceSoftwareRequestDto software,
                                     DeviceNetworkRequestDto network,
                                     DeviceSecurityRequestDto security,
                                     DeviceLocationRequestDto location) {
}
