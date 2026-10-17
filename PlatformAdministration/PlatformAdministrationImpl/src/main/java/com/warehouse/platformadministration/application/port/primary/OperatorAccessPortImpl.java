package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorSessionServicePort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.vo.OperatorAccess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OperatorAccessPortImpl implements OperatorAccessPort {

    private final OperatorServicePort operatorServicePort;

    private final OperatorSessionServicePort operatorSessionServicePort;

    public OperatorAccessPortImpl(final OperatorServicePort operatorServicePort,
                                  final OperatorSessionServicePort operatorSessionServicePort) {
        this.operatorServicePort = operatorServicePort;
        this.operatorSessionServicePort = operatorSessionServicePort;
    }

    @Override
    public OperatorAccess findAccess(final OperatorId operatorId) {
        requireOperator(operatorId);
        return new OperatorAccess(operatorId, operatorSessionServicePort.countActiveSessions(operatorId));
    }

    @Override
    public void revokeSessions(final OperatorId operatorId) {
        requireOperator(operatorId);
        operatorSessionServicePort.revokeSessions(operatorId);
        log.info("Platform owner revoked all sessions of operator {}", operatorId.value());
    }

    private void requireOperator(final OperatorId operatorId) {
        if (operatorServicePort.findScope(operatorId).isEmpty()) {
            throw new OperatorNotFoundException(operatorId);
        }
    }
}
