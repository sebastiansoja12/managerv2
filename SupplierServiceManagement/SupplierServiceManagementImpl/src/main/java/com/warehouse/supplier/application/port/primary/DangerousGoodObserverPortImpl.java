package com.warehouse.supplier.application.port.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.supplier.application.port.secondary.OperatorServicePort;
import com.warehouse.supplier.application.port.secondary.SupplierRepository;
import com.warehouse.supplier.domain.model.Supplier;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Slf4j
public class DangerousGoodObserverPortImpl implements DangerousGoodObserverPort {

    private final OperatorServicePort operatorServicePort;

    private final SupplierRepository supplierRepository;

    private final OperatorContext operatorContext;

    public DangerousGoodObserverPortImpl(final OperatorServicePort operatorServicePort,
                                         final SupplierRepository supplierRepository,
                                         final OperatorContext operatorContext) {
        this.operatorServicePort = operatorServicePort;
        this.supplierRepository = supplierRepository;
        this.operatorContext = operatorContext;
    }

    @Override
    public void run() {
        log.info("===============  Checking dangerous good certifications validity ===============");
        final Instant now = Instant.now();
        final Set<OperatorId> operatorIds = this.operatorServicePort.findActiveOperators();
        for (final OperatorId operatorId : operatorIds) {
            this.operatorContext.runAs(operatorId, () -> {
                final List<Supplier> suppliers =
                        this.supplierRepository.findAllWithExpiredDangerousGoodCertification(now);
                suppliers.forEach(supplier -> {
                    log.warn("Suppliers {} dangerous good certification expired!", supplier.supplierCode().value());
                    supplier.markCertificationAsInvalid();
                    this.supplierRepository.update(supplier);
                });
            });
        }
        log.info("===============  Checking dangerous good certifications validity finished ===============");
    }
}
