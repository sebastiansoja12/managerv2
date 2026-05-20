package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.api.event.OperatorInitialDepartmentCreateEvent;
import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;

class OperatorNotifyMapperTest {

    @Test
    void shouldMapOperatorSnapshotToDepartmentEvent() {
        final OperatorSnapshot snapshot = OperatorTestFixtures.operator().snapshot();

        final OperatorInitialDepartmentCreateEvent event = OperatorDepartmentNotifyMapper.toEvent(snapshot);

        assertEquals(OperatorTestFixtures.OPERATOR_ID, event.operatorId());
        assertEquals("WRO-1", event.departmentCode().getValue());
        assertEquals("Example Logistics", event.companyName());
        assertEquals(OperatorTestFixtures.TAX_ID.value(), event.taxId());
        assertEquals("Wroclaw", event.city());
        assertEquals("WAREHOUSE", event.departmentType());
    }

    @Test
    void shouldMapOperatorSnapshotToUserEventAndCallback() {
        final UserId userId = new UserId(4444L);
        final CapturedUserId capturedUserId = new CapturedUserId();

        final com.warehouse.auth.event.OperatorCreatedEvent event =
                OperatorUserNotifyMapper.toEvent(OperatorTestFixtures.operator().snapshot(), capturedUserId::capture);
        event.getUserCreatedId().accept(userId);

        assertEquals("Jan", event.getRegisteringUser().firstName());
        assertEquals("j.kowalski", event.getRegisteringUser().username());
        assertEquals("WRO-1", event.getRegisteringUser().departmentCode().value());
        assertEquals(OperatorTestFixtures.OPERATOR_ID.value(), event.getRegisteringUser().operatorId().value());
        assertEquals(userId, capturedUserId.value);
    }

    private static class CapturedUserId {
        private UserId value;

        private void capture(final UserId userId) {
            value = userId;
        }
    }
}
