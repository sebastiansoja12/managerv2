package com.warehouse.logistics.infrastructure.adapter.primary;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.warehouse.logistics.infrastructure.adapter.primary.DeviceContextAuthenticator.AuthenticatedDeviceContext;
import com.warehouse.terminal.jaxb.DeviceType;
import com.warehouse.terminal.jaxb.TerminalRequest;

@Aspect
public class DeviceAccessValidatorAspect {

    private final DeviceContextAuthenticator deviceContextAuthenticator;

    public DeviceAccessValidatorAspect(final DeviceContextAuthenticator deviceContextAuthenticator) {
        this.deviceContextAuthenticator = deviceContextAuthenticator;
    }

    @Around("@annotation(com.warehouse.commonassets.validator.DeviceAccessValidator)")
    public Object validateDeviceAccess(final ProceedingJoinPoint joinPoint) throws Throwable {
        final TerminalRequest terminalRequest = resolveTerminalRequest(joinPoint);
        final DeviceType device = resolveDevice(terminalRequest);

        final AuthenticatedDeviceContext authenticatedDevice =
                deviceContextAuthenticator.authenticateByPairKey(device.getPairKey());
        applyAuthenticatedDeviceContext(device, authenticatedDevice);

        try {
            return joinPoint.proceed();
        } finally {
            deviceContextAuthenticator.clear();
        }
    }

    private TerminalRequest resolveTerminalRequest(final ProceedingJoinPoint joinPoint) {
        for (final Object argument : joinPoint.getArgs()) {
            if (argument instanceof TerminalRequest terminalRequest) {
                return terminalRequest;
            }
        }
        throw new IllegalArgumentException("DeviceAccessValidator requires TerminalRequest argument");
    }

    private DeviceType resolveDevice(final TerminalRequest terminalRequest) {
        final DeviceType device = terminalRequest.getDevice();
        if (device == null) {
            throw new IllegalArgumentException("Missing device data in SOAP request");
        }
        return device;
    }

    private void applyAuthenticatedDeviceContext(final DeviceType device,
                                                 final AuthenticatedDeviceContext authenticatedDevice) {
        if (authenticatedDevice.deviceId() != null && !authenticatedDevice.deviceId().isBlank()) {
            device.setDeviceID(authenticatedDevice.deviceId());
        }
        if (authenticatedDevice.username() != null && !authenticatedDevice.username().isBlank()) {
            device.setResponsibleUser(authenticatedDevice.username());
        }
        device.setDepartmentCode(authenticatedDevice.departmentCode());
    }
}
