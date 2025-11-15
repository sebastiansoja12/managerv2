package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.helper.Result;
import com.warehouse.auth.domain.model.FullNameRequest;
import com.warehouse.auth.domain.model.User;
import com.warehouse.commonassets.identificator.UserId;


public interface UserPort {

    User findUser(String username);

    void updateFullName(final FullNameRequest request);

    void changeRole(final UserId userId, final User.Role role);

    Result<Void, String> addPermission(final UserId userId, final String permission);
}
