package com.warehouse.driverhub.application.port.primary;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.driverhub.application.port.primary.command.DriverCommand;
import com.warehouse.driverhub.domain.vo.DriverChange;
import com.warehouse.driverhub.domain.vo.DriverDetails;

import java.util.List;

public interface DriverPort {
    List<DriverDetails> findDrivers(final boolean activeOnly);
    DriverDetails getDriver(final DriverId driverId);
    DriverDetails addDriver(final CarrierId carrierId, final DriverCommand command);
    DriverDetails changeDriver(final DriverId driverId, final DriverChange change);
}
