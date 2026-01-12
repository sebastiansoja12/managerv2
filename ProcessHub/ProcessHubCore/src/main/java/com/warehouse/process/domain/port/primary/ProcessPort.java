package com.warehouse.process.domain.port.primary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.model.InitializeProcessCommand;
import com.warehouse.process.domain.vo.ChangeResponseProcessCommand;

public interface ProcessPort {
    ProcessId initialize(final InitializeProcessCommand command);
    void changeResponse(final ChangeResponseProcessCommand command);
    void finishProcess(final ProcessId processId, final ProcessStatus processStatus);
}
