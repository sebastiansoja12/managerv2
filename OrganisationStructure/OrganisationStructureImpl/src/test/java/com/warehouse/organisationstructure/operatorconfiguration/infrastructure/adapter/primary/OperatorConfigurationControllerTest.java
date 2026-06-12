package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary;

import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperatorConfigurationControllerTest {

    @Mock
    private OperatorConfigurationPort operatorConfigurationPort;

    @Test
    void shouldReturnCurrentOperatorConfiguration() {
        when(operatorConfigurationPort.getCurrent()).thenReturn(Optional.of(OperatorTestFixtures.configuration()));
        final OperatorConfigurationController controller =
                new OperatorConfigurationController(operatorConfigurationPort);

        final ResponseEntity<OperatorConfigurationDto> response = controller.getCurrent();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(OperatorTestFixtures.configurationDto(), response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenCurrentOperatorHasNoConfiguration() {
        when(operatorConfigurationPort.getCurrent()).thenReturn(Optional.empty());
        final OperatorConfigurationController controller =
                new OperatorConfigurationController(operatorConfigurationPort);

        final ResponseEntity<OperatorConfigurationDto> response = controller.getCurrent();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
