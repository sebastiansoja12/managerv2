package com.warehouse.terminal.domain.port.primary;

import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.model.TerminalVersion;
import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.domain.service.DevicePairService;
import com.warehouse.terminal.domain.service.TerminalService;
import com.warehouse.terminal.domain.service.TerminalValidatorService;
import com.warehouse.terminal.domain.service.UserService;
import com.warehouse.terminal.domain.vo.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TerminalPairPortImpl implements TerminalPairPort {
    
    private final TerminalValidatorService terminalValidatorService;
    
    private final TerminalService terminalService;

    private final UserService userService;

    private final DevicePairService devicePairService;

	public TerminalPairPortImpl(final TerminalValidatorService terminalValidatorService,
                                final TerminalService terminalService,
                                final UserService userService,
                                final DevicePairService devicePairService) {
		this.terminalValidatorService = terminalValidatorService;
		this.terminalService = terminalService;
        this.userService = userService;
        this.devicePairService = devicePairService;
    }

    @Override
    public boolean isConnected(final TerminalId terminalId) {
        return false;
    }

    @Override
    public boolean isValid(final TerminalId terminalId) {
        return false;
    }

    @Override
    public boolean isUserValid(final TerminalId terminalId, final UserId userId) {
        return false;
    }

    @Override
    public boolean isVersionValid(final TerminalId terminalId, final TerminalVersion version) {
        return false;
    }

    @Override
    public boolean updateRequired(final TerminalId terminalId, final TerminalVersion version) {
        return false;
    }

    @Override
    public void pair(final TerminalId terminalId) {
        final Terminal terminal = this.terminalService.findByDeviceId(terminalId);
        log.info("Pairing terminal {}", terminal.getTerminalId());

        this.terminalValidatorService.validateDepartment(terminal.getDepotCode());
        this.terminalValidatorService.validateTerminalVersion(terminal.getTerminalId());
        this.userService.validateUser(terminal.getUserId());
        this.devicePairService.pairDevice(terminal);
    }

    @Override
    public void unpair(final TerminalId terminalId) {

    }

    @Override
    public void update(final TerminalId terminalId) {
        final Terminal terminal = this.terminalService.findByDeviceId(terminalId);

    }

    @Override
    public void create(final TerminalAddRequest request) {
        logTerminalCreate(request);
        final User user = this.userService.findByUsername(request.getUsername());
        final Terminal terminal = Terminal.from(request, user.userId());
        this.terminalService.createTerminal(terminal);
    }

    private void logTerminalCreate(final TerminalAddRequest request) {
        log.info("Creating terminal device for username {}", request.getUsername());
    }
}
