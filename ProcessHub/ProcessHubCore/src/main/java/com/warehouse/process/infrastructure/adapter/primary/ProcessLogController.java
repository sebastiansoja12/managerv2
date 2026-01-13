package com.warehouse.process.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.infrastructure.adapter.primary.mapper.RequestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.process.domain.port.primary.ProcessPort;
import com.warehouse.process.infrastructure.dto.InitializeProcessRequestDto;

import java.util.UUID;

@RestController
@RequestMapping("/process-logs")
public class ProcessLogController {

    private final ProcessPort processPort;

    public ProcessLogController(final ProcessPort processPort) {
        this.processPort = processPort;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final InitializeProcessRequestDto request) {
        return ResponseEntity.ok().body(processPort.initialize(RequestMapper.map(request)));
    }

    @DeleteMapping("/{processStatus}/{processId}")
    public ResponseEntity<?> del(@PathVariable final ProcessStatus processStatus, @PathVariable final String processId) {
        final ProcessId id = new ProcessId(UUID.fromString(processId));
        this.processPort.finishProcess(id, processStatus);
        return ResponseEntity.ok().build();
    }
}
