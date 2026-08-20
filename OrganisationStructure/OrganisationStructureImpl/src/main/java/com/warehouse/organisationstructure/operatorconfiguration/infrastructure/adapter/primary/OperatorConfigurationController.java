package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary;

import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
