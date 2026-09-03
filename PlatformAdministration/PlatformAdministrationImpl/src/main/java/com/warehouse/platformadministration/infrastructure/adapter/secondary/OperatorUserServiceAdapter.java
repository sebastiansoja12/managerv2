package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.platformadministration.domain.vo.OperatorApiKey;
import com.warehouse.auth.UserApiService;
import com.warehouse.auth.infrastructure.dto.UserIdDto;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorUserCommand;
import com.warehouse.platformadministration.application.port.secondary.OperatorUserServicePort;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.domain.vo.OperatorUser;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.mapper.OperatorDirectoryServiceMapper;

import java.util.List;

public class OperatorUserServiceAdapter implements OperatorUserServicePort {

    private final UserApiService userApiService;

    private final OperatorScopeExecutor operatorScopeExecutor;

    public OperatorUserServiceAdapter(final UserApiService userApiService,
                                      final OperatorScopeExecutor operatorScopeExecutor) {
        this.userApiService = userApiService;
        this.operatorScopeExecutor = operatorScopeExecutor;
    }

    @Override
    public List<OperatorUser> findAll(final OperatorScope operatorScope) {
        return operatorScopeExecutor.execute(operatorScope, () -> userApiService.findAll()
                .stream()
                .map(OperatorDirectoryServiceMapper::toOperatorUser)
                .toList());
    }

    @Override
    public UserId create(final OperatorScope operatorScope, final CreateOperatorUserCommand command) {
        final UserIdDto userId = operatorScopeExecutor.execute(operatorScope,
                () -> userApiService.createOperatorUser(operatorScope.operatorId(),
                        OperatorDirectoryServiceMapper.toCreateOperatorUserApiRequest(command)));
        return new UserId(userId.value());
    }

    @Override
    public List<OperatorApiKey> findApiKeys(final OperatorScope operatorScope) {
        return operatorScopeExecutor.execute(operatorScope, () -> userApiService.findApiKeys()
                .stream()
                .map(apiKey -> new OperatorApiKey(new UserId(apiKey.userId().value()), apiKey.username(),
                        apiKey.firstName(), apiKey.lastName(), apiKey.role(),
                        apiKey.departmentCode() != null ? new DepartmentCode(apiKey.departmentCode()) : null))
                .toList());
    }

    @Override
    public void revokeApiKey(final OperatorScope operatorScope, final UserId userId) {
        operatorScopeExecutor.execute(operatorScope, () -> {
            userApiService.revokeApiKey(userId);
            return null;
        });
    }

    @Override
    public int anonymizeAll(final OperatorScope operatorScope) {
        return operatorScopeExecutor.execute(operatorScope, userApiService::anonymizeOperatorUsers);
    }
}
