package com.warehouse.logistics.domain.port.primary;


import com.warehouse.logistics.domain.port.secondary.SupplierRepository;
import com.warehouse.logistics.infrastructure.adapter.primary.exception.RestException;
import com.warehouse.terminal.DeviceInformation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class SupplierValidatorPortImpl implements SupplierValidatorPort {

    private final SupplierRepository supplierRepository;

    @Override
    public void validateSupplierCode(final DeviceInformation deviceInformation) {
        log.info("Validating supplier: {} from Device: {}", deviceInformation.getUsername(), deviceInformation);
        if (!supplierRepository.validBySupplierCode(deviceInformation.getUsername())) {
			log.error("User with device: [{}] was not validated: Supplier code {} is not valid", deviceInformation,
					deviceInformation.getUsername());
            throw new RestException(400, "Supplier is not valid");
        }
    }
}
