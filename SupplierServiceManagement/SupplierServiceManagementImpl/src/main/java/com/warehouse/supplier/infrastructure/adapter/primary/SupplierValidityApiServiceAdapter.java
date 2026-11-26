package com.warehouse.supplier.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.supplier.api.SupplierValidityApiService;
import com.warehouse.supplier.application.port.primary.DangerousGoodObserverPort;
import com.warehouse.supplier.application.port.primary.DriverLicenseObserverPort;

public class SupplierValidityApiServiceAdapter implements SupplierValidityApiService {

    private final DriverLicenseObserverPort driverLicenseObserverPort;

    private final DangerousGoodObserverPort dangerousGoodObserverPort;

    public SupplierValidityApiServiceAdapter(final DriverLicenseObserverPort driverLicenseObserverPort,
                                             final DangerousGoodObserverPort dangerousGoodObserverPort) {
        this.driverLicenseObserverPort = driverLicenseObserverPort;
        this.dangerousGoodObserverPort = dangerousGoodObserverPort;
    }

    @Override
    public int invalidateDriverLicenses(final OperatorId operatorId) {
        return driverLicenseObserverPort.run(operatorId);
    }

    @Override
    public int invalidateDangerousGoodCertifications(final OperatorId operatorId) {
        return dangerousGoodObserverPort.run(operatorId);
    }
}
