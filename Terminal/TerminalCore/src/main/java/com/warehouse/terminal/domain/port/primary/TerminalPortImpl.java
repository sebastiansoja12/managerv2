package com.warehouse.terminal.domain.port.primary;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.exception.UserNotFoundException;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.model.request.DeviceSettingsRequest;
import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.domain.service.DeviceVersionService;
import com.warehouse.terminal.domain.service.TerminalService;
import com.warehouse.terminal.domain.service.UserService;
import com.warehouse.terminal.domain.vo.DeviceTypeRequest;
import com.warehouse.terminal.domain.vo.DeviceUserRequest;
import com.warehouse.terminal.domain.vo.DeviceVersionRequest;
import com.warehouse.terminal.domain.vo.User;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
        if (user == null) {
            throw new UserNotFoundException(request.getUsername());
        }
        final Terminal terminal = Terminal.from(request);
        this.terminalService.createTerminal(terminal);
        this.deviceVersionService.saveOrUpdate(terminal.getTerminalId(), terminal.getVersion());
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
        final Username username = request.username();
        final Boolean userExists = this.userService.existsByUsername(username);
        if (!userExists) {
            throw new UserNotFoundException(username);
        }
        this.terminalService.assignUser(deviceId, username);
    }

    @Override
    public void changeVersionTo(final DeviceVersionRequest request) {
        final DeviceId deviceId = request.deviceId();
        final String version = request.version();
        this.terminalService.updateVersion(deviceId, version);
    }

    @Override
    public void updateSettings(final DeviceSettingsRequest request) {
        final Device device = terminalService.findByDeviceId(request.getDeviceId());
        if (device != null) {
            this.terminalService.updateSettings(request);
        }
    }

    @Override
    public List<Terminal> allDevices() {
        return this.terminalService.findAll();
    }

    @Override
    public Terminal getDevice(final DeviceId deviceId) {
        return this.terminalService.findByDeviceId(deviceId);
    }

    private void logTerminalCreate(final TerminalAddRequest request) {
        log.info("Creating terminal device for username {}", request.getUsername());
    }
}
