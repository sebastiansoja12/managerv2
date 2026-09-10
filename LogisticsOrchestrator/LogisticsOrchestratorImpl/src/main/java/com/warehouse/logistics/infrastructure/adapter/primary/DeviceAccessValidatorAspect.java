package com.warehouse.logistics.infrastructure.adapter.primary;

import com.warehouse.terminal.jaxb.DeviceType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class DeviceAccessValidatorAspect {

    private final DeviceContextAuthenticator deviceContextAuthenticator;

    private final DeviceTypeResolver deviceTypeResolver;

    public DeviceAccessValidatorAspect(final DeviceContextAuthenticator deviceContextAuthenticator,
                                       final DeviceTypeResolver deviceTypeResolver) {
        this.deviceContextAuthenticator = deviceContextAuthenticator;
        this.deviceTypeResolver = deviceTypeResolver;
    }

    @Around("@annotation(com.warehouse.commonassets.validator.DeviceAccessValidator)")
    public Object validateDeviceAccess(final ProceedingJoinPoint joinPoint) throws Throwable {
        final DeviceType device = deviceTypeResolver.resolve(joinPoint.getArgs());

        final DeviceContextAuthenticator.AuthenticatedDeviceContext authenticatedDevice =
                deviceContextAuthenticator.authenticateByPairKey(device.getPairKey());
        applyAuthenticatedDeviceContext(device, authenticatedDevice);

        try {
            return joinPoint.proceed();
        } finally {
            deviceContextAuthenticator.clear();
        }
    }

    private void applyAuthenticatedDeviceContext(final DeviceType device,
                                                 final DeviceContextAuthenticator.AuthenticatedDeviceContext authenticatedDevice) {
        if (authenticatedDevice.deviceId() != null && !authenticatedDevice.deviceId().isBlank()) {
            device.setDeviceID(authenticatedDevice.deviceId());
        }
        if (authenticatedDevice.username() != null && !authenticatedDevice.username().isBlank()) {
            device.setResponsibleUser(authenticatedDevice.username());
        }
        device.setDepartmentCode(authenticatedDevice.departmentCode());
    }
}
