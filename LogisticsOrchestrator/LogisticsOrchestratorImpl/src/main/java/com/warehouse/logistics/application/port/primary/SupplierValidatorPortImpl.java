package com.warehouse.logistics.application.port.primary;

import com.warehouse.logistics.application.port.secondary.SupplierRepository;
import com.warehouse.logistics.domain.exception.InvalidSupplierException;
import com.warehouse.terminal.DeviceInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SupplierValidatorPortImpl implements SupplierValidatorPort {

    private static final Logger log = LoggerFactory.getLogger(SupplierValidatorPortImpl.class);

    private final SupplierRepository supplierRepository;

    public SupplierValidatorPortImpl(final SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public void validateSupplierCode(final DeviceInformation deviceInformation) {
        log.info("Validating supplier: {} from Device: {}", deviceInformation.getUsername(), deviceInformation);
        if (!supplierRepository.validBySupplierCode(deviceInformation.getUsername())) {
            log.error("User with device: [{}] was not validated: Supplier code {} is not valid", deviceInformation,
                    deviceInformation.getUsername());
            throw new InvalidSupplierException("Supplier is not valid");
        }
    }
}
