package com.warehouse.delivery.domain.port.primary;


import com.warehouse.delivery.domain.port.secondary.SupplierRepository;
import com.warehouse.delivery.infrastructure.adapter.primary.exception.RestException;
import com.warehouse.terminal.information.Device;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class SupplierValidatorPortImpl implements SupplierValidatorPort {

    private final SupplierRepository supplierRepository;

    @Override
    public void validateSupplierCode(final Device device) {
        log.info("Validating supplier: {} from Device: {}", device.getUsername(), device);
        if (!supplierRepository.validBySupplierCode(device.getUsername())) {
			log.error("User with device: [{}] was not validated: Supplier code {} is not valid", device,
					device.getUsername());
            throw new RestException(400, "Supplier is not valid");
        }
    }
}
