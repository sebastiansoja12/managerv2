package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.platformadministration.domain.vo.OperatorApiKey;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorUserCommand;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.domain.vo.OperatorUser;

import java.util.List;

public interface OperatorUserServicePort {

    List<OperatorUser> findAll(final OperatorScope operatorScope);

    UserId create(final OperatorScope operatorScope, final CreateOperatorUserCommand command);

    List<OperatorApiKey> findApiKeys(final OperatorScope operatorScope);

    void revokeApiKey(final OperatorScope operatorScope, final UserId userId);

    int anonymizeAll(final OperatorScope operatorScope);
}
