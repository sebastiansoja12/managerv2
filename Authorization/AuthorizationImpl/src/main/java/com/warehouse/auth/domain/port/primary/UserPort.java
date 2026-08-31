package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.helper.Result;
import com.warehouse.auth.domain.model.FullNameChangeCommand;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.model.UpdateUserCommand;
import com.warehouse.auth.domain.vo.UserDepartmentUpdateRequest;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;


public interface UserPort {

    List<OrganizationUser> findAll();

    OrganizationUser findUser(String username);

    OrganizationUser findUser(UserId userId);

    OrganizationUser update(final UpdateUserCommand command);

    void changePassword(final UserId userId, final String encodedPassword);

    void changeLanguage(final UserId userId, final String language);

    String regenerateApiKey(final UserId userId);

    void deleteApiKey(final UserId userId);

    void changeRole(final UserId userId, final OrganizationUser.Role role);

    void changeAdminDepartmentInfo(final UserDepartmentUpdateRequest request);

    Result<Void, String> addPermission(final UserId userId, final String permission);

    Result<Void, String> removePermission(final UserId userId, final String permission);

    void deleteDataForDepartment(final DepartmentCode departmentCode);

    DepartmentCode getDepartmentCode(final DepartmentId departmentId);

    void changeFullName(final FullNameChangeCommand fullNameChangeCommand);
}
