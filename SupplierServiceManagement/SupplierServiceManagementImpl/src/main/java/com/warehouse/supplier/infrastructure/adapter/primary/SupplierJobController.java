package com.warehouse.supplier.infrastructure.adapter.primary;

import com.warehouse.supplier.domain.port.primary.DangerousGoodObserverPort;
import com.warehouse.supplier.domain.port.primary.DriverLicenseObserverPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supplier-jobs")
public class SupplierJobController {

    private final DriverLicenseObserverPort driverLicenseObserverPort;

    private final DangerousGoodObserverPort dangerousGoodObserverPort;

    public SupplierJobController(final DriverLicenseObserverPort driverLicenseObserverPort,
                                 final DangerousGoodObserverPort dangerousGoodObserverPort) {
        this.driverLicenseObserverPort = driverLicenseObserverPort;
        this.dangerousGoodObserverPort = dangerousGoodObserverPort;
    }

    @GetMapping("/driver-licenses")
    public ResponseEntity<?> runDriverLicenses() {
        this.driverLicenseObserverPort.run();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/certifications")
    public ResponseEntity<?> runCertifications() {
        this.dangerousGoodObserverPort.run();
        return ResponseEntity.ok().build();
    }
}
