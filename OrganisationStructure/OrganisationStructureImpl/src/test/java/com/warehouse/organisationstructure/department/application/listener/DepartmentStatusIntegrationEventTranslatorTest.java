package com.warehouse.organisationstructure.department.application.listener;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.department.api.dto.DepartmentStatusDto;
import com.warehouse.organisationstructure.department.api.event.DepartmentStatusChangedIntegrationEvent;
import com.warehouse.organisationstructure.department.domain.event.DepartmentArchived;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentSnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepartmentStatusIntegrationEventTranslatorTest {

    private final DepartmentStatusIntegrationEventTranslator translator = new DepartmentStatusIntegrationEventTranslator();

    @Test
    void shouldPublishArchivedDepartmentIntegrationEvent() {
        final DepartmentStatusChangedIntegrationEvent publishedEvent =
                this.translator.translate(new DepartmentArchived(departmentSnapshot(), Instant.now()));

        assertEquals(10L, publishedEvent.affectedDepartmentId().value());
        assertEquals("KT1", publishedEvent.departmentCode().value());
        assertEquals(DepartmentStatusDto.ARCHIVED, publishedEvent.status());
        assertEquals("Closed", publishedEvent.reason());
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
                null,
                "Closed",
                null);
    }
}
