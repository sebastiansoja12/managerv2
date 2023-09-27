package com.warehouse.routetracker;

import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteLogEventPublisherImpl;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.RouteLogEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RouteLogEventPublisherImplTest {
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private RouteLogEvent logEvent;

    @InjectMocks
    private RouteLogEventPublisherImpl logEventPublisher;

    @Test
    public void shouldSendEvent() {
        // given
        doNothing().when(eventPublisher).publishEvent(logEvent);

        // when
        logEventPublisher.send(logEvent);

        // then
        verify(eventPublisher, times(1)).publishEvent(logEvent);
    }

    @Test
    public void shouldLogEvent() {
        // given
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // when
        logEventPublisher.send(logEvent);

        // then
        assertThat(outContent.toString(), containsString("Publishing event RouteLogEvent"));
    }
}
