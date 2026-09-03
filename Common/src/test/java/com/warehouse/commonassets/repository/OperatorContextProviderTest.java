package com.warehouse.commonassets.repository;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OperatorContextProviderTest {

    @Test
    void shouldReturnIdentifiersFromCurrentContext() {
        final OperatorId operatorId = OperatorId.of(1L);
        final UserId userId = new UserId(2L);
        final DepartmentId departmentId = new DepartmentId(3L);
        final OperatorContextProvider provider = () -> Optional.of(
                new OperatorDetails(operatorId, userId, departmentId));

        assertEquals(operatorId, provider.currentOperatorId());
        assertEquals(userId, provider.currentUserId());
        assertEquals(departmentId, provider.currentDepartmentId());
    }

    @Test
    void shouldThrowWhenCurrentContextIsMissing() {
        final OperatorContextProvider provider = Optional::empty;

        assertEquals("Operator context is required",
                assertThrows(IllegalStateException.class, provider::currentOperatorId).getMessage());
        assertEquals("User context is required",
                assertThrows(IllegalStateException.class, provider::currentUserId).getMessage());
        assertEquals("Department context is required",
                assertThrows(IllegalStateException.class, provider::currentDepartmentId).getMessage());
    }

    @Test
    void shouldThrowWhenIdentifierIsMissingFromCurrentContext() {
        final OperatorContextProvider provider = () -> Optional.of(new OperatorDetails(null, null, null));

        assertThrows(IllegalStateException.class, provider::currentOperatorId);
        assertThrows(IllegalStateException.class, provider::currentUserId);
        assertThrows(IllegalStateException.class, provider::currentDepartmentId);
    }
}
