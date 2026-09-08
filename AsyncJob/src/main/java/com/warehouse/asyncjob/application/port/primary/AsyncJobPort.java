package com.warehouse.asyncjob.application.port.primary;

import com.warehouse.asyncjob.application.port.primary.command.CreateReadModelRebuildJobCommand;
import com.warehouse.asyncjob.domain.vo.AsyncJobId;
import com.warehouse.asyncjob.domain.vo.AsyncJobSnapshot;
import com.warehouse.commonassets.identificator.OperatorId;

import java.util.List;

public interface AsyncJobPort {

    AsyncJobSnapshot createReadModelRebuildJob(final CreateReadModelRebuildJobCommand command);

    AsyncJobSnapshot get(final AsyncJobId jobId);

    List<AsyncJobSnapshot> findLatest(final OperatorId operatorId, final int limit);
}
