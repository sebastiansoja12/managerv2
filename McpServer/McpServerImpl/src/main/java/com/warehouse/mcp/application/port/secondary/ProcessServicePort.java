package com.warehouse.mcp.application.port.secondary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.mcp.domain.vo.ProcessDetails;

public interface ProcessServicePort {
    ProcessDetails findProcess(final ProcessId processId);
}
