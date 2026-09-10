package com.warehouse.department.application.listener;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.department.api.dto.DepartmentStatusDto;
import com.warehouse.department.api.event.DepartmentStatusChangedIntegrationEvent;
import com.warehouse.department.domain.event.DepartmentArchived;
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
class DepartmentStatusIntegrationEventListenerTest {

    @Mock
    private IntegrationEventPublisher integrationEventPublisher;

    private DepartmentStatusIntegrationEventListener listener;

    @BeforeEach
    void setUp() {
        this.listener = new DepartmentStatusIntegrationEventListener(this.integrationEventPublisher);
    }

    @Test
    void shouldPublishArchivedDepartmentIntegrationEvent() {
        this.listener.handle(new DepartmentArchived(departmentSnapshot(), Instant.now()));

        final ArgumentCaptor<DepartmentStatusChangedIntegrationEvent> eventCaptor =
                ArgumentCaptor.forClass(DepartmentStatusChangedIntegrationEvent.class);
        verify(this.integrationEventPublisher).publish(eventCaptor.capture());
        final DepartmentStatusChangedIntegrationEvent publishedEvent = eventCaptor.getValue();
        assertEquals(10L, publishedEvent.affectedDepartmentId().value());
        assertEquals("KT1", publishedEvent.departmentCode().value());
        assertEquals(DepartmentStatusDto.ARCHIVED, publishedEvent.status());
        assertEquals("10", publishedEvent.eventKey());
        assertEquals(
                "department.status.changed",
                publishedEvent.getClass().getAnnotation(IntegrationEventType.class).value());
    }

    private static DepartmentSnapshot departmentSnapshot() {
        return new DepartmentSnapshot(
                new DepartmentId(10L),
                new com.warehouse.commonassets.identificator.DepartmentCode("KT1"),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
