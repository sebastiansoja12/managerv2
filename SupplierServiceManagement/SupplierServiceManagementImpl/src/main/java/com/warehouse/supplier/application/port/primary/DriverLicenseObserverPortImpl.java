package com.warehouse.supplier.application.port.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.supplier.application.port.secondary.MailServicePort;
import com.warehouse.supplier.application.port.secondary.OperatorServicePort;
import com.warehouse.supplier.application.port.secondary.SupplierRepository;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.service.DriverLicenseService;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class DriverLicenseObserverPortImpl implements DriverLicenseObserverPort {

    private final DriverLicenseService driverLicenseService;

    private final OperatorServicePort operatorServicePort;

    private final SupplierRepository supplierRepository;

    private final OperatorContext operatorContext;

    private final MailServicePort mailServicePort;

    public DriverLicenseObserverPortImpl(final DriverLicenseService driverLicenseService,
                                         final OperatorServicePort operatorServicePort,
                                         final SupplierRepository supplierRepository,
                                         final OperatorContext operatorContext,
                                         final MailServicePort mailServicePort) {
        this.driverLicenseService = driverLicenseService;
        this.operatorServicePort = operatorServicePort;
        this.supplierRepository = supplierRepository;
        this.operatorContext = operatorContext;
        this.mailServicePort = mailServicePort;
    }

    @Override
    public void run() {
        log.info("===============  Checking driver licenses validity ===============");
        this.operatorServicePort.findActiveOperators().forEach(this::run);
        log.info("===============  Checking driver licenses validity finished ===============");
    }

    @Override
    public int run(final OperatorId operatorId) {
        final Instant now = Instant.now();
        return this.operatorContext.runAs(operatorId, () -> {
            int invalidated = 0;
            for (final Supplier supplier : this.supplierRepository.findAllWithInvalidDriverLicense(now)) {
                final Result<Void, String> result =
                        this.driverLicenseService.validateDriverLicense(supplier.getDriverLicense());
                if (result.isFailure()) {
                    log.warn("Suppliers {} driver license is invalid!", supplier.supplierCode().value());
                    supplier.markDriverLicenseAsInvalid();
                    this.supplierRepository.update(supplier);
                    this.mailServicePort.notifyDriverLicenseInvalidated(supplier);
                    invalidated++;
                }
            }
            return invalidated;
        });
    }
}
