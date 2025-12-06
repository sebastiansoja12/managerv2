package com.warehouse.supplier.domain.port.primary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.service.DriverLicenseService;
import com.warehouse.supplier.domain.service.SupplierNoDepartmentContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
public class DriverLicenseObserverPortImpl implements DriverLicenseObserverPort {

    private final DriverLicenseService driverLicenseService;

    private final SupplierNoDepartmentContextService supplierNoDepartmentContextService;

    public DriverLicenseObserverPortImpl(final DriverLicenseService driverLicenseService,
                                         final SupplierNoDepartmentContextService supplierNoDepartmentContextService) {
        this.driverLicenseService = driverLicenseService;
        this.supplierNoDepartmentContextService = supplierNoDepartmentContextService;
    }


    @Scheduled(cron = "${purge.cron.expression}")
    @Override
    public void run() {
        log.info("===============  Checking driver licenses validity ===============");
        final List<Supplier> suppliers = this.supplierNoDepartmentContextService.findAll();
        suppliers.forEach(supplier -> {
            final Result<Void, String> result = this.driverLicenseService.validateDriverLicense(supplier.getDriverLicense());
            if (result.isFailure()) {
                log.warn("Suppliers driver license is invalid!", supplier.supplierCode().value());
                this.supplierNoDepartmentContextService.invalidateDriverLicense(supplier);
            }
        });
        log.info("===============  Checking driver licenses validity finished ===============");
    }
}
