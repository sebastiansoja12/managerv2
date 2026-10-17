package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.OperatorSessionApiService;
import com.warehouse.auth.domain.port.primary.OperatorSessionPort;
import com.warehouse.commonassets.identificator.OperatorId;

public class OperatorSessionApiServiceAdapter implements OperatorSessionApiService {

    private final OperatorSessionPort operatorSessionPort;

    public OperatorSessionApiServiceAdapter(final OperatorSessionPort operatorSessionPort) {
        this.operatorSessionPort = operatorSessionPort;
    }

    @Override
    public long countActiveSessions(final OperatorId operatorId) {
        return operatorSessionPort.countActiveSessions(operatorId);
    }

    @Override
    public void revokeSessions(final OperatorId operatorId) {
        operatorSessionPort.revokeSessions(operatorId);
    }
}
