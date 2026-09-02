package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorShipmentSettingsServicePort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.domain.vo.OperatorShipmentSettings;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OperatorSettingsPortImplTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    private static final OperatorScope SCOPE = new OperatorScope(OPERATOR_ID, new UserId(1L));

    private final OperatorServicePort operatorServicePort = mock(OperatorServicePort.class);

    private final OperatorShipmentSettingsServicePort settingsServicePort = mock(OperatorShipmentSettingsServicePort.class);

    private final OperatorSettingsPort port = new OperatorSettingsPortImpl(operatorServicePort, settingsServicePort);

    @Test
    void shouldReadSettingsInsideOperatorScope() {
        final OperatorShipmentSettings settings = new OperatorShipmentSettings(Map.of("labelConfiguration", Map.of()));
        when(operatorServicePort.findScope(OPERATOR_ID)).thenReturn(Optional.of(SCOPE));
        when(settingsServicePort.find(SCOPE)).thenReturn(Optional.of(settings));

        assertThat(port.findShipmentSettings(OPERATOR_ID)).isEqualTo(settings);
    }

    @Test
    void shouldReportMissingConfigurationAsConflict() {
        when(operatorServicePort.findScope(OPERATOR_ID)).thenReturn(Optional.of(SCOPE));
        when(settingsServicePort.find(SCOPE)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> port.findShipmentSettings(OPERATOR_ID));
    }

    @Test
    void shouldRejectEmptySettings() {
        assertThrows(IllegalArgumentException.class,
                () -> port.updateShipmentSettings(OPERATOR_ID, new OperatorShipmentSettings(Map.of())));
    }

    @Test
    void shouldRejectUnknownOperator() {
        when(operatorServicePort.findScope(OPERATOR_ID)).thenReturn(Optional.empty());

        assertThrows(OperatorNotFoundException.class, () -> port.findShipmentSettings(OPERATOR_ID));
    }
}
