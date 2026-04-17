package com.warehouse.organisationstructure.operator.infrastructure.adapter.primary;

import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.dto.CreateOperatorApiRequest;
import com.warehouse.organisationstructure.api.dto.OperatorDto;
import com.warehouse.organisationstructure.api.dto.UpdateOperatorApiRequest;
import com.warehouse.organisationstructure.operator.domain.model.CreateOperatorCommand;
import com.warehouse.organisationstructure.operator.domain.model.UpdateOperatorCommand;
import com.warehouse.organisationstructure.operator.domain.port.primary.OperatorPort;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.primary.mapper.OperatorRequestMapper;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;

import java.util.List;

@RestController
@RequestMapping("/operators")
public class OperatorController {

    private final OperatorPort operatorPort;

    public OperatorController(final OperatorPort operatorPort) {
        this.operatorPort = operatorPort;
    }

    @GetMapping
    public ResponseEntity<List<OperatorDto>> getAll() {
        return ResponseEntity.ok(operatorPort.getAll()
                .stream()
                .map(OperatorMapper::toDto)
                .toList());
    }

    @GetMapping("/{operatorId}")
    public ResponseEntity<OperatorDto> getById(@PathVariable final Long operatorId) {
        return ResponseEntity.ok(OperatorMapper.toDto(operatorPort.getById(OperatorId.of(operatorId))));
    }

    @PostMapping
    public ResponseEntity<OperatorIdDto> create(@RequestBody final CreateOperatorApiRequest request) {
        final CreateOperatorCommand command = OperatorRequestMapper.toCommand(request);
        final OperatorId operatorId = operatorPort.create(command);
        return ResponseEntity.ok(OperatorMapper.toDtoId(operatorId));
    }

    @PutMapping("/{operatorId}")
    public ResponseEntity<OperatorDto> update(@PathVariable final Long operatorId,
                                              @RequestBody final UpdateOperatorApiRequest request) {
        final UpdateOperatorCommand command = OperatorRequestMapper.toCommand(request);
        return ResponseEntity.ok(OperatorMapper.toDto(operatorPort.update(OperatorId.of(operatorId), command)));
    }
}
