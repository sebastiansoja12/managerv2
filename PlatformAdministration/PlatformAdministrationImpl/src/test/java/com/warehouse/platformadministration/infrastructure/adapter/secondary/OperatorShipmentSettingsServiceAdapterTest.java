package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OperatorShipmentSettingsServiceAdapterTest {

    private static final OperatorScope SCOPE = new OperatorScope(OperatorId.of(20L), new UserId(5L));

    private final OperatorConfigurationApiService operatorConfigurationApiService = mock(OperatorConfigurationApiService.class);

    private final OperatorShipmentSettingsServiceAdapter adapter = new OperatorShipmentSettingsServiceAdapter(
            operatorConfigurationApiService, new OperatorScopeExecutor(new OperatorContext()), new ObjectMapper());

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReadConfigurationInsideOperatorContext() {
        final AtomicReference<OperatorId> operatorDuringCall = new AtomicReference<>();
        when(operatorConfigurationApiService.getCurrentShipmentConfiguration()).thenAnswer(invocation -> {
            operatorDuringCall.set(((UsernameTenantPasswordAuthenticationToken) SecurityContextHolder.getContext()
                    .getAuthentication()).getOperatorId());
            return null;
        });

        assertThat(adapter.find(SCOPE)).isEmpty();
        assertThat(operatorDuringCall.get()).isEqualTo(OperatorId.of(20L));
    }
}
