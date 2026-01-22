package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.port.secondary.SoftwareConfigurationServicePort;
import com.warehouse.shipment.domain.vo.SoftwareConfiguration;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.tools.softwareconfiguration.SoftwareConfigurationProperties;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SoftwareConfigurationServiceAdapter implements SoftwareConfigurationServicePort {

    private final Retry retry;
    
    private final SoftwareConfigurationProperties softwareConfigurationProperties;

    private final RouteTrackerLogProperties routeTrackerLogProperties;

	public SoftwareConfigurationServiceAdapter(final RetryConfig retryConfig,
                                               final SoftwareConfigurationProperties softwareConfigurationProperties,
                                               final RouteTrackerLogProperties routeTrackerLogProperties) {
        this.retry = Retry.of("softwareConfiguration", retryConfig);
        this.softwareConfigurationProperties = softwareConfigurationProperties;
        this.routeTrackerLogProperties = routeTrackerLogProperties;
    }

    @Override
    public SoftwareConfiguration getShipmentSoftwareConfiguration() {
        return new SoftwareConfiguration(routeTrackerLogProperties.getShipment(), routeTrackerLogProperties.getUrl());
    }

    @Override
    public SoftwareConfiguration getShipmentPersonSoftwareConfiguration() {
        return new SoftwareConfiguration(routeTrackerLogProperties.getShipmentPerson(), routeTrackerLogProperties.getUrl());
    }
}
