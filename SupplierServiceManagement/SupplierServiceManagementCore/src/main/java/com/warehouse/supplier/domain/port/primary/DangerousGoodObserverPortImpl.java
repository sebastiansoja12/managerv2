package com.warehouse.supplier.domain.port.primary;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.service.SupplierNoDepartmentContextService;
import com.warehouse.supplier.domain.vo.DangerousGoodCertification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.List;

@Slf4j
public class DangerousGoodObserverPortImpl implements DangerousGoodObserverPort {

    private final SupplierNoDepartmentContextService supplierNoDepartmentContextService;

    public DangerousGoodObserverPortImpl(final SupplierNoDepartmentContextService supplierNoDepartmentContextService) {
        this.supplierNoDepartmentContextService = supplierNoDepartmentContextService;
    }

    @Scheduled(cron = "${purge.cron.expression}")
    @Override
    public void run() {
        log.info("===============  Checking dangerous good certifications validity ===============");
        final List<Supplier> suppliers = this.supplierNoDepartmentContextService.findAll();
        suppliers.forEach(supplier -> {
            final DangerousGoodCertification dangerousGoodCertification = supplier.getDangerousGoodCertification();
            if (dangerousGoodCertification != null && dangerousGoodCertification.expiryDate().isAfter(Instant.now())) {
                this.supplierNoDepartmentContextService.invalidateCertification(supplier);
            }
        });
        log.info("===============  Checking dangerous good certifications validity finished ===============");
    }
}
