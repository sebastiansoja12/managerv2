package com.warehouse.shipment.infrastructure.adapter.secondary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.DefaultShipmentStatusDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentWorkflowConfigurationDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberDateFormatDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberPrefixModeDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberRuleDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberSourceDto;
import com.warehouse.shipment.application.port.secondary.CurrentDepartmentCodePort;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.OperatorShipmentConfigurationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShipmentConfigurationServiceAdapterTest {

    @Mock
    private OperatorConfigurationApiService operatorConfigurationApiService;

    @Mock
    private CurrentDepartmentCodePort currentDepartmentCodePort;

    @Test
    void shouldGetCurrentOperatorShipmentConfiguration() {
        when(operatorConfigurationApiService.getCurrentShipmentConfiguration())
                .thenReturn(new ShipmentConfigurationDto(
                        null,
                        null,
                        null,
                        new ShipmentWorkflowConfigurationDto(
                                DefaultShipmentStatusDto.PREPARED,
                                null,
                                false,
                                true,
                                false,
                                30,
                                "16:00"
                        ),
                        null,
                        null
                ));

        final ShipmentConfigurationServiceAdapter adapter = new ShipmentConfigurationServiceAdapter(
                operatorConfigurationApiService,
                new OperatorShipmentConfigurationMapper(),
                currentDepartmentCodePort
        );

        final OperatorShipmentConfiguration configuration = adapter.getCurrentOperatorShipmentConfiguration();

        assertThat(configuration.workflowSettings().defaultStatus()).isEqualTo(ShipmentStatus.PREPARED);
    }

    @Test
    void shouldSupplyCurrentDepartmentCodeToDepartmentBasedTrackingRule() {
        when(operatorConfigurationApiService.getCurrentShipmentConfiguration())
                .thenReturn(new ShipmentConfigurationDto(
                        null,
                        null,
                        null,
                        null,
                        new TrackingNumberRuleDto(
                                TrackingNumberPrefixModeDto.DEPARTMENT_CODE,
                                "IGNORED",
                                "-",
                                TrackingNumberSourceDto.SEQUENCE,
                                8,
                                true,
                                TrackingNumberDateFormatDto.YYYYMMDD,
                                true
                        ),
                        null
                ));
        when(currentDepartmentCodePort.currentDepartmentCode()).thenReturn(new DepartmentCode("PO1"));
        final ShipmentConfigurationServiceAdapter adapter = new ShipmentConfigurationServiceAdapter(
                operatorConfigurationApiService,
                new OperatorShipmentConfigurationMapper(),
                currentDepartmentCodePort
        );

        final OperatorShipmentConfiguration configuration = adapter.getCurrentOperatorShipmentConfiguration();

        assertThat(configuration.trackingNumberRule().key()).isEqualTo("PO1");
    }
}
