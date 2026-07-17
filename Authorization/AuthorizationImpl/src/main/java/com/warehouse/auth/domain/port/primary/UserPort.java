package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.helper.Result;
import com.warehouse.auth.domain.model.FullNameRequest;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.model.UpdateUserCommand;
import com.warehouse.auth.domain.vo.UserDepartmentUpdateRequest;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;


public interface UserPort {

    List<User> findAll();

    User findUser(String username);

    User findUser(UserId userId);

    User update(final UpdateUserCommand command);

    void updateFullName(final FullNameRequest request);

    void changePassword(final UserId userId, final String encodedPassword);

    void changeLanguage(final UserId userId, final String language);

    void changeRole(final UserId userId, final User.Role role);

    void changeAdminDepartmentInfo(final UserDepartmentUpdateRequest request);

    Result<Void, String> addPermission(final UserId userId, final String permission);

    Result<Void, String> removePermission(final UserId userId, final String permission);

    void deleteDataForDepartment(final DepartmentCode departmentCode);
}
