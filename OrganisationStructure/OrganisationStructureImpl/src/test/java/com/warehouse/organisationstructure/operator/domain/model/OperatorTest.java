package com.warehouse.organisationstructure.operator.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

class OperatorTest {

    @Test
    void shouldCreateDefaultConfigurationWhenConfigurationIsMissing() {
        final Operator operator = new Operator(
                OperatorId.of(10002L),
                null,
                TaxId.of("5251112233"),
                true,
                true,
                true,
                "+48123123123",
                "ops@example.com",
                "Default Operator",
                OperatorTestFixtures.CONTRACT_START,
                OperatorTestFixtures.CONTRACT_END,
                OperatorTestFixtures.FOUNDED_DATE,
                null,
                OperatorStatus.ACTIVE,
                OperatorTestFixtures.CREATED_AT,
                OperatorTestFixtures.UPDATED_AT
        );

        final OperatorConfiguration.ShippingCapabilities capabilities =
                operator.getConfiguration().getShippingCapabilities();

        assertTrue(capabilities.isSupportsDomesticShipping());
        assertTrue(capabilities.isSupportsInternationalShipping());
        assertTrue(capabilities.isSupportsCashOnDelivery());
        assertTrue(capabilities.isSupportsParcelLockers());
    }

    @Test
    void shouldExposeSnapshotWithProvisioningDetails() {
        final Operator operator = OperatorTestFixtures.operator();

        final OperatorSnapshot snapshot = operator.snapshot();

        assertEquals(OperatorTestFixtures.OPERATOR_ID, snapshot.operatorId());
        assertEquals(OperatorTestFixtures.TAX_ID, snapshot.taxId());
        assertEquals("Example Logistics", snapshot.companyName());
        assertNotNull(snapshot.configuration());
        assertEquals("WRO-1", snapshot.provisioningDetails().firstDepartment().departmentCode());
    }

    @Test
    void shouldMarkOperatorAsModifiedWhenRegisteringUserIsAssigned() {
        final Operator operator = OperatorTestFixtures.operator();
        final Instant previousUpdatedAt = operator.getUpdatedAt();

        operator.assignRegisteringUser(OperatorTestFixtures.REGISTERING_USER_ID);

        assertEquals(OperatorTestFixtures.REGISTERING_USER_ID, operator.getRegisteringUserId());
        assertTrue(operator.getUpdatedAt().isAfter(previousUpdatedAt));
    }

    @Test
    void shouldRejectMissingRequiredData() {
        assertThrows(NullPointerException.class, () -> new Operator(
                null,
                null,
                OperatorTestFixtures.TAX_ID,
                true,
                true,
                false,
                "+48123123123",
                "ops@example.com",
                "Example Logistics",
                OperatorTestFixtures.CONTRACT_START,
                OperatorTestFixtures.CONTRACT_END,
                OperatorTestFixtures.FOUNDED_DATE,
                OperatorTestFixtures.configuration(),
                OperatorStatus.ACTIVE,
                OperatorTestFixtures.CREATED_AT,
                OperatorTestFixtures.UPDATED_AT
        ));
    }
}
