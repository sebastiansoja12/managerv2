package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.port.secondary.SoftwareConfigurationServicePort;
import com.warehouse.shipment.domain.vo.SoftwareConfiguration;

public class SoftwareConfigurationServiceMockAdapter implements SoftwareConfigurationServicePort {


    @Override
    public SoftwareConfiguration getSoftwareConfiguration() {
        return null;
    }
}
