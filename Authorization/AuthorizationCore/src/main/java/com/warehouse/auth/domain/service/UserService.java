package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.FullNameRequest;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public interface UserService {

    RegisterResponse create(final User user);

    User findUser(final String username);

    UserId nextUserId();

    void changeFullName(final FullNameRequest request);

    void changeRole(final UserId userId, final User.Role role);

    void addPermission(final UserId userId, final String permission);

    void removePermission(final UserId userId, final String permission);

    User findUserById(final UserId userId);

    List<UserId> findAllActiveUsersByDepartmentCode(final DepartmentCode departmentCode);

    void deleteDataForUser(final UserId userId);
}
