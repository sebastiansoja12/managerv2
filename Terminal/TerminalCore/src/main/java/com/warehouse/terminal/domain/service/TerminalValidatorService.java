package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.TerminalId;

public interface TerminalValidatorService {
    void validateDepartment(final String departmentCode);
    void validateTerminalVersion(final TerminalId terminalId);
}
