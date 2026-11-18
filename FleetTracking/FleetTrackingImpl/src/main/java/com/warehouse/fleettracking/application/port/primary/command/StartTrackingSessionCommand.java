package com.warehouse.fleettracking.application.port.primary.command;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.VehicleId;

public record StartTrackingSessionCommand(DeviceId deviceId, VehicleId vehicleId) {
}
