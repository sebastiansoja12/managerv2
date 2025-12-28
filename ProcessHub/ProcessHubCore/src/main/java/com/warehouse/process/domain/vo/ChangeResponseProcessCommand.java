package com.warehouse.process.domain.vo;

import com.warehouse.commonassets.identificator.ProcessId;

public record ChangeResponseProcessCommand(ProcessId processId, String response) {
}
