package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.FullNameRequest;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.commonassets.identificator.UserId;

public interface UserService {

    RegisterResponse create(User user);

    User findUser(String username);

    UserId nextUserId();

    void changeFullName(final FullNameRequest request);

    void changeRole(final UserId userId, final User.Role role);

    void addPermission(final UserId userId, final String permission);

    User findUserById(final UserId userId);
}
