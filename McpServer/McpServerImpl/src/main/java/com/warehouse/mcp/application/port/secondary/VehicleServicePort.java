package com.warehouse.mcp.application.port.secondary;

import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.mcp.domain.vo.VehicleDraft;

public interface VehicleServicePort {
    VehicleId create(final VehicleDraft vehicle);
}
