package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.FullNameChangeCommand;
import com.warehouse.auth.domain.model.UpdateUserCommand;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.domain.vo.UserDepartmentUpdateRequest;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public interface UserService {

    RegisterResponse create(final OrganizationUser user);

    OrganizationUser findUser(final String username);

    List<OrganizationUser> findAll();

    OrganizationUser update(final UpdateUserCommand command);

    UserId nextUserId();

    void changeFullName(final FullNameChangeCommand request);

    void changePassword(final UserId userId, final String encodedPassword);

    void changeLanguage(final UserId userId, final String language);

    void changeApiKey(final UserId userId, final String apiKey);

    void changeRole(final UserId userId, final OrganizationUser.Role role);

    void addPermission(final UserId userId, final String permission);

    void removePermission(final UserId userId, final String permission);

    OrganizationUser findUserById(final UserId userId);

    OrganizationUser findByEmail(final String email);

    List<UserId> findAllActiveUsersByDepartmentCode(final DepartmentCode departmentCode);

    void deleteDataForUser(final UserId userId);

    void updateDefaultDepartmentUser(final UserDepartmentUpdateRequest request);

    UserId findInitialUser();

    DepartmentCode getDepartmentCode(final DepartmentId departmentId);

    DepartmentId getDepartmentId(final DepartmentCode departmentCode);

    void anonymize(final UserId userId);
}
