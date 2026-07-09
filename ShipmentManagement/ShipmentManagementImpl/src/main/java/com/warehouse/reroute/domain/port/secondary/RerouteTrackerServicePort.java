package com.warehouse.reroute.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.domain.vo.SoftwareConfiguration;

public interface RerouteTrackerServicePort {
    void sendRerouteRequest(final SoftwareConfiguration softwareConfiguration, final ShipmentId shipmentId);
}
