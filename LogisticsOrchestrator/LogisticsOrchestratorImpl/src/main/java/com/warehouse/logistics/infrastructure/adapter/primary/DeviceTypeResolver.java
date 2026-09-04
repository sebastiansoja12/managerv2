package com.warehouse.logistics.infrastructure.adapter.primary;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.warehouse.terminal.jaxb.DeviceType;

public class DeviceTypeResolver {

    private static final ClassValue<Optional<MethodHandle>> DEVICE_ACCESSORS = new ClassValue<>() {
        @Override
        protected Optional<MethodHandle> computeValue(final Class<?> requestType) {
            return findDeviceAccessor(requestType);
        }
    };

    public DeviceType resolve(final Object[] arguments) {
        return Arrays.stream(arguments)
                .filter(argument -> argument != null && DEVICE_ACCESSORS.get(argument.getClass()).isPresent())
                .findFirst()
                .map(this::readDevice)
                .orElseThrow(() -> new IllegalArgumentException(
                        "DeviceAccessValidator requires a SOAP request argument with a DeviceType element"));
    }

    private DeviceType readDevice(final Object request) {
        final MethodHandle accessor = DEVICE_ACCESSORS.get(request.getClass()).orElseThrow();
        final DeviceType device;
        try {
            device = (DeviceType) accessor.invoke(request);
        } catch (final Throwable exception) {
            throw new IllegalStateException("Cannot read device data from " + request.getClass().getSimpleName(), exception);
        }
        if (device == null) {
            throw new IllegalArgumentException("Missing device data in SOAP request");
        }
        return device;
    }

    private static Optional<MethodHandle> findDeviceAccessor(final Class<?> requestType) {
        final List<Method> accessors = Arrays.stream(requestType.getMethods())
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getParameterCount() == 0)
                .filter(method -> method.getName().startsWith("get"))
                .filter(method -> DeviceType.class.isAssignableFrom(method.getReturnType()))
                .toList();
        if (accessors.size() > 1) {
            throw new IllegalStateException(requestType.getSimpleName() + " has more than one DeviceType element");
        }
        return accessors.stream().findFirst().map(method -> unreflect(requestType, method));
    }

    private static MethodHandle unreflect(final Class<?> requestType, final Method method) {
        try {
            return MethodHandles.publicLookup()
                    .findVirtual(requestType, method.getName(), MethodType.methodType(method.getReturnType()));
        } catch (final ReflectiveOperationException exception) {
            throw new IllegalStateException("Cannot access " + requestType.getSimpleName() + "#" + method.getName(), exception);
        }
    }
}
