package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.command.StartReadModelRebuildCommand;
import com.warehouse.platformadministration.domain.vo.ReadModelRebuildJob;

import java.util.List;
import java.util.UUID;

public interface ReadModelRebuildPort {

    List<String> readModelTypes();

    ReadModelRebuildJob start(final OperatorId operatorId, final StartReadModelRebuildCommand command);

    ReadModelRebuildJob find(final UUID jobId);

    List<ReadModelRebuildJob> findLatest(final OperatorId operatorId);
}
