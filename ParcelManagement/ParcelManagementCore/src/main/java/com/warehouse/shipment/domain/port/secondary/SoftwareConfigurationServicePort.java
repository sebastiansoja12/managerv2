package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.model.SoftwareConfigurationProperties;
import com.warehouse.shipment.domain.vo.SoftwareConfiguration;

public interface SoftwareConfigurationServicePort {
    SoftwareConfiguration getSoftwareConfiguration(final SoftwareConfigurationProperties softwareConfigurationProperties);
}
