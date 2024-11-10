package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.terminal.domain.port.secondary.DeviceVersionRepository;

public class TerminalValidatorServiceImpl implements TerminalValidatorService {

    private final DeviceVersionRepository deviceVersionRepository;

    public TerminalValidatorServiceImpl(final DeviceVersionRepository deviceVersionRepository) {
        this.deviceVersionRepository = deviceVersionRepository;
    }

    @Override
    public void validateDepartment(final String departmentCode) {
        // todo
    }

    @Override
    public void validateTerminalVersion(final TerminalId terminalId) {
        if (deviceVersionRepository.updateRequired(terminalId)) {
            throw new RuntimeException("Device needs to be updated");
        }
    }
}
