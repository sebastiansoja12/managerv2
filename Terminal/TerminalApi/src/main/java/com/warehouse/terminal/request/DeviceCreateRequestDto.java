package com.warehouse.terminal.request;

import com.warehouse.terminal.dto.*;

public record DeviceCreateRequestDto(UserIdDto userId,
                                     SupplierCodeDto supplierCode,
                                     VersionDto version,
                                     DeviceTypeDto deviceType) {
}
