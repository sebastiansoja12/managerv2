package com.warehouse;

import org.junit.jupiter.api.DynamicNode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

class BoundedContextTestScanner {

    static Stream<DynamicNode> scanForTests(Object invoke) {
        return Arrays
                .stream(invoke.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BoundedContextTest.class))
                .map(method -> methodInvokerWrapper(method, invoke));
    }

    private static DynamicNode methodInvokerWrapper(Method method, Object invoke) {
        method.setAccessible(true);
        try {
            return (DynamicNode) method.invoke(invoke);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
