package com.warehouse.supplier.domain.port.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.secondary.OperatorServicePort;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import com.warehouse.supplier.domain.service.SupplierInvalidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Slf4j
public class DangerousGoodObserverPortImpl implements DangerousGoodObserverPort {

    private final SupplierInvalidatorService supplierInvalidatorService;

    private final OperatorServicePort operatorServicePort;

    private final SupplierRepository supplierRepository;

    private final OperatorContext operatorContext;

    public DangerousGoodObserverPortImpl(final SupplierInvalidatorService supplierInvalidatorService,
                                         final OperatorServicePort operatorServicePort,
                                         final SupplierRepository supplierRepository,
                                         final OperatorContext operatorContext) {
        this.supplierInvalidatorService = supplierInvalidatorService;
        this.operatorServicePort = operatorServicePort;
        this.supplierRepository = supplierRepository;
        this.operatorContext = operatorContext;
    }

    @Scheduled(cron = "${purge.cron.expression}")
    @Override
    public void run() {
        log.info("===============  Checking dangerous good certifications validity ===============");
        final Instant now = Instant.now();
        final Set<OperatorId> operatorIds = this.operatorServicePort.findActiveOperators();
        for (final OperatorId operatorId : operatorIds) {
            this.operatorContext.runAs(operatorId, () -> {
                final List<Supplier> suppliers = this.supplierRepository.findAllWithExpiredDangerousGoodCertification(now);
                suppliers.forEach(supplier -> {
                    log.warn("Suppliers {} dangerous good certification expired!", supplier.supplierCode().value());
                    this.supplierInvalidatorService.invalidateCertification(supplier);
                });
            });
        }
        log.info("===============  Checking dangerous good certifications validity finished ===============");
    }
}
