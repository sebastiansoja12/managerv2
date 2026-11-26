package com.warehouse.organisationstructure.department.application.listener;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.department.api.event.DepartmentUserChangedIntegrationEvent;
import com.warehouse.organisationstructure.department.domain.event.DepartmentEmailChanged;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentSnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepartmentUserIntegrationEventTranslatorTest {

    private final DepartmentUserIntegrationEventTranslator translator = new DepartmentUserIntegrationEventTranslator();

    @Test
    void shouldPublishDepartmentUserChangedIntegrationEvent() {
        final Instant timestamp = Instant.EPOCH;

        final DepartmentUserChangedIntegrationEvent event =
                this.translator.translate(new DepartmentEmailChanged(departmentSnapshot(), timestamp));

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
                null,
                null,
                null);
    }
}
