package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.auth.OperatorSessionApiService;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.secondary.OperatorSessionServicePort;

public class OperatorSessionServiceAdapter implements OperatorSessionServicePort {

    private final OperatorSessionApiService operatorSessionApiService;

    public OperatorSessionServiceAdapter(final OperatorSessionApiService operatorSessionApiService) {
        this.operatorSessionApiService = operatorSessionApiService;
    }

    @Override
    public long countActiveSessions(final OperatorId operatorId) {
        return operatorSessionApiService.countActiveSessions(operatorId);
    }

    @Override
    public void revokeSessions(final OperatorId operatorId) {
        operatorSessionApiService.revokeSessions(operatorId);
    }
}
