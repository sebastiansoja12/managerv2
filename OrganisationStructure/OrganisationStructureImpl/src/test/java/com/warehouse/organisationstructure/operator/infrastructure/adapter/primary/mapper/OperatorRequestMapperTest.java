package com.warehouse.organisationstructure.operator.infrastructure.adapter.primary.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operator.domain.model.CreateOperatorCommand;
import com.warehouse.organisationstructure.operator.domain.model.OperatorStatus;
import com.warehouse.organisationstructure.operator.domain.model.UpdateOperatorCommand;

class OperatorRequestMapperTest {

    @Test
    void shouldMapCreateRequestToCommand() {
        final CreateOperatorCommand command = OperatorRequestMapper.toCommand(
                OperatorTestFixtures.createApiRequest()
        );

        assertEquals("Jan", command.userFirstName());
        assertEquals("j.kowalski", command.username());
        assertEquals(OperatorTestFixtures.TAX_ID, command.taxId());
        assertEquals("Example Logistics", command.companyName());
        assertNotNull(command.configuration());
        assertEquals("WRO-1", command.firstDepartment().departmentCode());
    }

    @Test
    void shouldMapUpdateRequestToCommand() {
        final UpdateOperatorCommand command = OperatorRequestMapper.toCommand(
                OperatorTestFixtures.updateApiRequest()
        );

        assertEquals("5259998877", command.taxId().value());
        assertEquals("Updated Logistics", command.companyName());
        assertEquals(OperatorStatus.SUSPENDED, command.status());
        assertNotNull(command.configuration());
        assertEquals(31.5, command.configuration().getShipmentLimits().getMaxWeight());
    }
}
