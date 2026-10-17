package com.warehouse.auth;

import com.warehouse.commonassets.identificator.OperatorId;

public interface OperatorSessionApiService {

    long countActiveSessions(final OperatorId operatorId);

    void revokeSessions(final OperatorId operatorId);
}
