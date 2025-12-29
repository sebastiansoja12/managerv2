package com.warehouse.process.infrastructure.adapter.primary;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.port.primary.ProcessPort;
import com.warehouse.process.domain.vo.ShipmentUpdated;
import com.warehouse.process.infrastructure.adapter.primary.mapper.RequestMapper;
import com.warehouse.process.infrastructure.dto.InitializeProcessRequestDto;
import com.warehouse.process.infrastructure.dto.ShipmentUpdateDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/process-logs")
@Tag(
        name = "TEST ONLY — Process Logs (to be deleted)",
        description = "⚠️ Test controller"
)
public class ProcessLogController {

    private final ProcessPort processPort;

    public ProcessLogController(final ProcessPort processPort) {
        this.processPort = processPort;
    }

    @PostMapping
    @Operation(summary = "[TEST] Create process log", description = "⚠️ Test Endpoint")
    public ResponseEntity<?> create(@RequestBody final InitializeProcessRequestDto request) {
        return ResponseEntity.ok().body(processPort.initialize(RequestMapper.map(request)));
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
        final ShipmentUpdated shipmentUpdated = RequestMapper.map(shipmentUpdate);
        this.processPort.assignShipmentUpdated(prId, shipmentUpdated);
        return ResponseEntity.ok().build();
    }
}

