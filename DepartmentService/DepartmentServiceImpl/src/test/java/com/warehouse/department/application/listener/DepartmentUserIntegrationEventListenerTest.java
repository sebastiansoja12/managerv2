package com.warehouse.department.application.listener;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.api.event.DepartmentUserChangedIntegrationEvent;
import com.warehouse.department.domain.event.DepartmentEmailChanged;
import com.warehouse.department.domain.vo.DepartmentSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DepartmentUserIntegrationEventListenerTest {

    @Mock
    private IntegrationEventPublisher integrationEventPublisher;

    private DepartmentUserIntegrationEventListener listener;

    @BeforeEach
    void setUp() {
        this.listener = new DepartmentUserIntegrationEventListener(this.integrationEventPublisher);
    }

    @Test
    void shouldPublishDepartmentUserChangedIntegrationEvent() {
        final Instant timestamp = Instant.EPOCH;

        this.listener.handle(new DepartmentEmailChanged(departmentSnapshot(), timestamp));

        final ArgumentCaptor<DepartmentUserChangedIntegrationEvent> eventCaptor =
                ArgumentCaptor.forClass(DepartmentUserChangedIntegrationEvent.class);
        verify(this.integrationEventPublisher).publish(eventCaptor.capture());
        final DepartmentUserChangedIntegrationEvent event = eventCaptor.getValue();
        assertEquals("KT1", event.departmentCode().value());
        assertEquals(new UserId(20L), event.userId());
        assertEquals("123456789", event.telephoneNumber());
        assertEquals("kt1@example.com", event.email());
        assertEquals(timestamp, event.timestamp());
    }

    private static DepartmentSnapshot departmentSnapshot() {
        return new DepartmentSnapshot(
                new DepartmentId(10L),
                new DepartmentCode("KT1"),
                null,
                null,
                "123456789",
                null,
                "kt1@example.com",
                null,
                null,
                null,
                null,
                null,
                null,
                new UserId(20L),
                null,
                null);
    }
}
