package com.warehouse.mail.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.mail.domain.vo.ShipmentNotificationPolicy;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.ShipmentNotificationChannelDto;
import com.warehouse.organisationstructure.api.dto.ShipmentNotificationConfigurationDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class OperatorShipmentNotificationPolicyAdapterTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(3L);

    private final OperatorConfigurationApiService operatorConfigurationApiService =
            mock(OperatorConfigurationApiService.class);
    private final OperatorShipmentNotificationPolicyAdapter adapter =
            new OperatorShipmentNotificationPolicyAdapter(operatorConfigurationApiService);

    @Test
    void shouldFollowOperatorMilestoneFlagsWhenEmailChannelIsEnabled() {
        when(operatorConfigurationApiService.getShipmentNotificationConfiguration(OPERATOR_ID))
                .thenReturn(configuration(ShipmentNotificationChannelDto.BOTH));

        assertEquals(new ShipmentNotificationPolicy(true, false, true), adapter.policyFor(OPERATOR_ID));
    }

    @Test
    void shouldNotEmailRecipientWhenOperatorUsesSmsOnly() {
        when(operatorConfigurationApiService.getShipmentNotificationConfiguration(OPERATOR_ID))
                .thenReturn(configuration(ShipmentNotificationChannelDto.SMS));

        assertEquals(new ShipmentNotificationPolicy(false, false, false), adapter.policyFor(OPERATOR_ID));
    }

    @Test
    void shouldNotRestrictEventsWithoutOperator() {
        assertEquals(ShipmentNotificationPolicy.unrestricted(), adapter.policyFor(null));
        verifyNoInteractions(operatorConfigurationApiService);
    }

    private static ShipmentNotificationConfigurationDto configuration(final ShipmentNotificationChannelDto channel) {
        return new ShipmentNotificationConfigurationDto(true, false, true, true, channel);
    }
}
