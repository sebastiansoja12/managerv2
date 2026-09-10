package com.warehouse.logistics.application.port.primary;

import com.warehouse.logistics.application.port.secondary.DepartmentServicePort;
import com.warehouse.logistics.domain.exception.InvalidDepartmentException;
import com.warehouse.terminal.DeviceInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DepartmentValidatorPortImpl implements DepartmentValidatorPort {

    private static final Logger log = LoggerFactory.getLogger(DepartmentValidatorPortImpl.class);

    private final DepartmentServicePort departmentServicePort;

    public DepartmentValidatorPortImpl(final DepartmentServicePort departmentServicePort) {
        this.departmentServicePort = departmentServicePort;
    }

    @Override
    public void validateDepartment(final DeviceInformation deviceInformation) {
        log.info("Validating department {} from device {}", deviceInformation.getDepartmentCode(), deviceInformation);
        if (!this.departmentServicePort.existsByCode(deviceInformation.getDepartmentCode())) {
            log.error("User {} validation for department {} failed. Used Device: [{}]", deviceInformation.getUsername(),
                    deviceInformation.getDepartmentCode(), deviceInformation);
            throw new InvalidDepartmentException("Department is not valid");
        }
        log.info("Device [{}] department validated", deviceInformation);
    }
}
