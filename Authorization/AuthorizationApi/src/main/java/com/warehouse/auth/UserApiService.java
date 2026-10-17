package com.warehouse.auth;

import com.warehouse.auth.infrastructure.dto.UserApiKeyDto;
import com.warehouse.auth.infrastructure.dto.CreateOperatorUserApiRequest;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.auth.infrastructure.dto.UserIdDto;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public interface UserApiService {
    UserDto findById(final UserId userId);
    UserDto findByUsername(final String username);
    UserIdDto findInitialUserForOperator();
    List<UserDto> findAll();
    UserIdDto createOperatorUser(final OperatorId operatorId, final CreateOperatorUserApiRequest request);

    List<UserApiKeyDto> findApiKeys();

    void revokeApiKey(final UserId userId);

    int anonymizeOperatorUsers();
}
