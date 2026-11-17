package com.warehouse.supplier.infrastructure.adapter.primary;

import com.warehouse.supplier.application.port.primary.DangerousGoodObserverPort;
import com.warehouse.supplier.application.port.primary.DriverLicenseObserverPort;
import org.springframework.scheduling.annotation.Scheduled;

public class SupplierValidityJob {

    private final DriverLicenseObserverPort driverLicenseObserverPort;

    private final DangerousGoodObserverPort dangerousGoodObserverPort;

    public SupplierValidityJob(final DriverLicenseObserverPort driverLicenseObserverPort,
                               final DangerousGoodObserverPort dangerousGoodObserverPort) {
        this.driverLicenseObserverPort = driverLicenseObserverPort;
        this.dangerousGoodObserverPort = dangerousGoodObserverPort;
    }

    @Scheduled(cron = "${purge.cron.expression}")
    public void invalidateDriverLicenses() {
        this.driverLicenseObserverPort.run();
    }

    @Scheduled(cron = "${purge.cron.expression}")
    public void invalidateDangerousGoodCertifications() {
        this.dangerousGoodObserverPort.run();
    }
}
