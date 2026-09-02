package com.warehouse.auth.infrastructure.adapter.primary.kafka;

import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.domain.vo.UserDepartmentUpdateRequest;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.api.dto.DepartmentCodeDto;
import com.warehouse.department.api.dto.DepartmentIdDto;
import com.warehouse.department.api.dto.DepartmentStatusDto;
import com.warehouse.department.api.event.DepartmentStatusChangedIntegrationEvent;
import com.warehouse.department.api.event.DepartmentUserChangedIntegrationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DepartmentUserEventKafkaListenerTest {

    @Mock
    private UserPort userPort;

    private DepartmentUserEventKafkaListener listener;

    @BeforeEach
    void setUp() {
        this.listener = new DepartmentUserEventKafkaListener(this.userPort);
    }

    @Test
    void shouldDeleteDepartmentUserDataWhenDepartmentIsDeleted() {
        this.listener.handle(event(DepartmentStatusDto.DELETED));

        verify(this.userPort).deleteDataForDepartment(new DepartmentCode("KT1"));
    }

    @Test
    void shouldKeepDepartmentUserDataWhenDepartmentIsArchived() {
        this.listener.handle(event(DepartmentStatusDto.ARCHIVED));

        verify(this.userPort, never()).deleteDataForDepartment(new DepartmentCode("KT1"));
    }

    @Test
    void shouldUpdateDepartmentUserFromSharedIntegrationEventContract() {
        final UserId userId = new UserId(20L);
        final DepartmentUserChangedIntegrationEvent event = new DepartmentUserChangedIntegrationEvent(
                new DepartmentCodeDto("KT1"),
                userId,
                "123456789",
                "kt1@example.com",
                Instant.EPOCH);

        this.listener.handle(event);

        verify(this.userPort).changeAdminDepartmentInfo(new UserDepartmentUpdateRequest(
                new DepartmentCode("KT1"),
                userId,
                "123456789",
                "kt1@example.com"));
    }

    @Test
    void shouldConsumeDepartmentStatusEventThroughClassLevelKafkaListener() throws NoSuchMethodException {
        final KafkaListener listenerAnnotation = DepartmentUserEventKafkaListener.class
                .getAnnotation(KafkaListener.class);
        final KafkaHandler handlerAnnotation = DepartmentUserEventKafkaListener.class
                .getDeclaredMethod("handle", DepartmentStatusChangedIntegrationEvent.class)
                .getAnnotation(KafkaHandler.class);

        assertNotNull(listenerAnnotation);
        assertNotNull(handlerAnnotation);
        assertEquals(
                "${manager.kafka.topics.department-status-events:department.status.events}",
                listenerAnnotation.topics()[1]);
    }

    private static DepartmentStatusChangedIntegrationEvent event(final DepartmentStatusDto status) {
        return new DepartmentStatusChangedIntegrationEvent(
                new DepartmentIdDto(10L),
                new DepartmentCodeDto("KT1"),
                status);
    }
}
