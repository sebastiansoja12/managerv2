package com.warehouse.returning.infrastructure.adapter.primary;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.junit.jupiter.api.Test;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

class ReturnControllerValidationTest {

    @Test
    void shouldDeclareReturnsQueryValidationAtTheControllerBoundary() throws NoSuchMethodException {
        final Method getAll = ReturnController.class.getMethod(
                "getAll", String.class, int.class, int.class);
        final Parameter[] parameters = getAll.getParameters();

        assertThat(parameters[0].getAnnotation(NotBlank.class)).isNotNull();
        assertThat(parameters[1].getAnnotation(Min.class).value()).isZero();
        assertThat(parameters[2].getAnnotation(Min.class).value()).isEqualTo(1L);
        assertThat(parameters[2].getAnnotation(Max.class).value()).isEqualTo(100L);
    }
}
