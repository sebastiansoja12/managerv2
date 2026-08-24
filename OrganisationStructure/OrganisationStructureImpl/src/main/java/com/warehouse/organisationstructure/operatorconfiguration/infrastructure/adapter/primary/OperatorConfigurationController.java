package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary;

import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLimitsDto;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operator-configurations")
public class OperatorConfigurationController {

    private final OperatorConfigurationPort operatorConfigurationPort;

    public OperatorConfigurationController(final OperatorConfigurationPort operatorConfigurationPort) {
        this.operatorConfigurationPort = operatorConfigurationPort;
    }

    @GetMapping
    public ResponseEntity<OperatorConfigurationDto> getCurrent() {
        return ResponseEntity.of(operatorConfigurationPort.getCurrent()
                .map(OperatorMapper::toDtoConfiguration));
    }

    @GetMapping("/shipment")
    public ResponseEntity<ShipmentConfigurationDto> getCurrentShipmentConfiguration() {
        return ResponseEntity.of(operatorConfigurationPort.getCurrent()
                .map(configuration -> OperatorMapper.toDtoShipmentConfiguration(
                        configuration.getShipmentConfiguration())));
    }

    @PutMapping("/shipment")
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentConfiguration(
            @RequestBody final ShipmentConfigurationDto request) {
        return ResponseEntity.ok(OperatorMapper.toDtoShipmentConfiguration(
                operatorConfigurationPort.updateCurrentShipmentConfiguration(
                        OperatorMapper.toModelShipmentConfiguration(request)).getShipmentConfiguration()));
    }

    @GetMapping("/shipment/limits")
    public ResponseEntity<ShipmentLimitsDto> getCurrentShipmentLimits() {
        return ResponseEntity.of(operatorConfigurationPort.getCurrent()
                .map(OperatorMapper::toDtoCurrentShipmentLimits));
    }
}
