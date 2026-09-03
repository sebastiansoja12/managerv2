package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.command.EraseOperatorDataCommand;
import com.warehouse.platformadministration.domain.model.OperatorDataErasure;
import com.warehouse.platformadministration.domain.model.OperatorDataExport;

import java.util.Optional;

public interface OperatorDataPort {

    OperatorDataExport export(final OperatorId operatorId);

    OperatorDataErasure erase(final OperatorId operatorId, final EraseOperatorDataCommand command);

    Optional<OperatorDataErasure> findLastErasure(final OperatorId operatorId);
}
