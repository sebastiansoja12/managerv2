package com.warehouse.terminal.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.exception.UserNotFoundException;
import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.domain.service.DeviceVersionService;
import com.warehouse.terminal.domain.service.TerminalService;
import com.warehouse.terminal.domain.service.UserService;
import com.warehouse.terminal.domain.vo.DeviceTypeRequest;
import com.warehouse.terminal.domain.vo.DeviceUserRequest;
import com.warehouse.terminal.domain.vo.DeviceVersionRequest;
import com.warehouse.terminal.domain.vo.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TerminalPortImpl implements TerminalPort {

    private final TerminalService terminalService;

    private final UserService userService;

    private final DeviceVersionService deviceVersionService;

    public TerminalPortImpl(final TerminalService terminalService,
                            final UserService userService,
                            final DeviceVersionService deviceVersionService) {
        this.terminalService = terminalService;
        this.userService = userService;
        this.deviceVersionService = deviceVersionService;
    }

    @Override
    public void create(final TerminalAddRequest request) {
        logTerminalCreate(request);
        final User user = this.userService.findByUsername(request.getUsername());
        final Terminal terminal = Terminal.from(request, user.userId());
        this.terminalService.createTerminal(terminal);
        this.deviceVersionService.saveOrUpdateDeviceVersion(terminal.getTerminalId(), terminal.getVersion());
    }

    @Override
    public void changeDeviceTypeTo(final DeviceTypeRequest request) {
        final DeviceId deviceId = request.getDeviceId();
        final DeviceType deviceType = request.getDeviceType();
        this.terminalService.changeDeviceType(deviceId, deviceType);
    }

    @Override
    public void changeUserTo(final DeviceUserRequest request) {
        final DeviceId deviceId = request.deviceId();
        final UserId userId = request.userId();
        final Boolean userExists = this.userService.existsByUserId(userId);
        if (!userExists) {
            throw new UserNotFoundException(userId);
        }
        this.terminalService.assignUser(deviceId, userId);
    }

    @Override
    public void changeVersionTo(final DeviceVersionRequest request) {
        final DeviceId deviceId = request.deviceId();
        final String version = request.version();
        this.terminalService.updateVersion(deviceId, version);
    }

    @Override
    public List<Terminal> allDevices() {
        return this.terminalService.findAll();
    }

    private void logTerminalCreate(final TerminalAddRequest request) {
        log.info("Creating terminal device for username {}", request.getUsername());
    }
}
