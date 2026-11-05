package com.warehouse.commonassets.logging;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import ch.qos.logback.classic.spi.LoggingEvent;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityContextLogConverterTest {

    private final OperatorLogConverter operatorConverter = new OperatorLogConverter();

    private final UserLogConverter userConverter = new UserLogConverter();

    private final OperatorContext operatorContext = new OperatorContext();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldResolveOperatorFromRunAs() {
        final LoggingEvent event = event(Map.of("operator", "5", "user", "50"));

        final String operator = operatorContext.runAs(new OperatorId(1L), () -> operatorConverter.convert(event));
        final String user = operatorContext.runAs(new OperatorId(1L), () -> userConverter.convert(event));

        assertThat(operator).isEqualTo("1");
        assertThat(user).isEqualTo("N/A");
    }

    @Test
    void shouldResolveUserFromRunAs() {
        final String user = operatorContext.runAs(new OperatorId(1L), new UserId(10L),
                () -> userConverter.convert(event(Collections.emptyMap())));

        assertThat(user).isEqualTo("10");
    }

    @Test
    void shouldFallBackToMdcWithoutAuthentication() {
        assertThat(operatorConverter.convert(event(Map.of("operator", "PLATFORM")))).isEqualTo("PLATFORM");
        assertThat(userConverter.convert(event(Collections.emptyMap()))).isEqualTo("N/A");
    }

    private static LoggingEvent event(final Map<String, String> mdc) {
        final LoggingEvent event = new LoggingEvent();
        event.setMDCPropertyMap(mdc);
        return event;
    }
}
