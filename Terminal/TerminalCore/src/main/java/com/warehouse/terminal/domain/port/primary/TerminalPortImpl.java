package com.warehouse.terminal.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.domain.service.TerminalService;
import com.warehouse.terminal.domain.service.UserService;
import com.warehouse.terminal.domain.vo.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TerminalPortImpl implements TerminalPort {

    private final TerminalService terminalService;

    private final UserService userService;

    public TerminalPortImpl(final TerminalService terminalService, final UserService userService) {
        this.terminalService = terminalService;
        this.userService = userService;
    }

    @Override
    public void update(final DeviceId deviceId) {
        final Terminal terminal = this.terminalService.findByDeviceId(deviceId);
    }

    @Override
    public void create(final TerminalAddRequest request) {
        logTerminalCreate(request);
        final User user = this.userService.findByUsername(request.getUsername());
        final Terminal terminal = Terminal.from(request, user.userId());
        this.terminalService.createTerminal(terminal);
    }

    @Override
    public List<Terminal> allDevices() {
        return this.terminalService.findAll();
    }

    private void logTerminalCreate(final TerminalAddRequest request) {
        log.info("Creating terminal device for username {}", request.getUsername());
    }
}
