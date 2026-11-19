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
import java.util.List;
import java.util.Set;

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
        final Instant now = Instant.now();
        final Set<OperatorId> operatorIds = this.operatorServicePort.findActiveOperators();
        for (final OperatorId operatorId : operatorIds) {
            this.operatorContext.runAs(operatorId, () -> {
                final List<Supplier> suppliers = this.supplierRepository.findAllWithInvalidDriverLicense(now);
                suppliers.forEach(supplier -> {
                    final Result<Void, String> result =
                            this.driverLicenseService.validateDriverLicense(supplier.getDriverLicense());
                    if (result.isFailure()) {
                        log.warn("Suppliers {} driver license is invalid!", supplier.supplierCode().value());
                        supplier.markDriverLicenseAsInvalid();
                        this.supplierRepository.update(supplier);
                        this.mailServicePort.notifyDriverLicenseInvalidated(supplier);
                    }
                });
            });
        }
        log.info("===============  Checking driver licenses validity finished ===============");
    }
}
