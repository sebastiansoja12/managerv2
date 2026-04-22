package com.warehouse.process.infrastructure.adapter.primary;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.port.primary.ProcessPort;
import com.warehouse.process.domain.vo.ShipmentUpdated;
import com.warehouse.process.infrastructure.adapter.primary.mapper.ProcessLogResponseMapper;
import com.warehouse.process.infrastructure.adapter.primary.mapper.RequestMapper;
import com.warehouse.process.infrastructure.dto.InitializeProcessRequestDto;
import com.warehouse.process.infrastructure.dto.ShipmentUpdateDto;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/process-logs")
public class ProcessLogController {

    private final ProcessPort processPort;

    private final CurrentOperatorService currentOperatorService;

    public ProcessLogController(final ProcessPort processPort,
                                final CurrentOperatorService currentOperatorService) {
        this.processPort = processPort;
        this.currentOperatorService = currentOperatorService;
    }

    @GetMapping
    @Operation(summary = "Find process logs for current department")
    public ResponseEntity<?> findAllForCurrentDepartment(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {
        return ResponseEntity.ok(ProcessLogResponseMapper.map(processPort.findAllForCurrentDepartment(pageable)));
    }

    @GetMapping("/{processId}")
    @Operation(summary = "Find process log by process id for current department")
    public ResponseEntity<?> findByIdForCurrentDepartment(@PathVariable final String processId) {
        final ProcessId id = new ProcessId(UUID.fromString(processId));
        return ResponseEntity.ok(ProcessLogResponseMapper.map(processPort.findByIdForCurrentDepartment(id)));
    }

    @PostMapping
    @Operation(summary = "[TEST] Create process log", description = "⚠️ Test Endpoint")
    public ResponseEntity<?> create(@RequestBody final InitializeProcessRequestDto request) {
        final OperatorId operatorId = currentOperatorService.getCurrentOperatorId();
        return ResponseEntity.ok().body(processPort.initialize(RequestMapper.map(request, operatorId)));
    }

    @DeleteMapping("/{processStatus}/{processId}")
    @Operation(summary = "[TEST] Finish process", description = "⚠️ Test Endpoint")
    public ResponseEntity<?> del(@PathVariable final ProcessStatus processStatus, @PathVariable final String processId) {
        final ProcessId id = new ProcessId(UUID.fromString(processId));
        this.processPort.finishProcess(id, processStatus);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{processId}")
    @Operation(summary = "[TEST] Update shipment", description = "⚠️ Test Endpoint")
    public ResponseEntity<?> updateShipment(@PathVariable String processId, @RequestBody ShipmentUpdateDto shipmentUpdate) {
        final ProcessId prId = new ProcessId(UUID.fromString(processId));
        final ShipmentUpdated shipmentUpdated = RequestMapper.map(shipmentUpdate, currentOperatorService.getCurrentOperatorId());
        this.processPort.assignShipmentUpdated(prId, shipmentUpdated);
        return ResponseEntity.ok().build();
    }
}
