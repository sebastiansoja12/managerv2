package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.helper.Result;
import com.warehouse.auth.domain.model.FullNameChangeCommand;
import com.warehouse.auth.domain.model.RolePermission;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.model.UpdateUserCommand;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.domain.vo.UserDepartmentUpdateRequest;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;


@Slf4j
public class UserPortImpl implements UserPort {

    private static final int API_KEY_SIZE_BYTES = 32;

    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final SecureRandom secureRandom;

    public UserPortImpl(final UserService userService,
                        final AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.secureRandom = new SecureRandom();
    }

    @Override
    public User findUser(final String username) {
        return userService.findUser(username);
    }

    @Override
    public List<User> findAll() {
        return userService.findAll();
    }

    @Override
    public User findUser(final UserId userId) {
        return userService.findUserById(userId);
    }

    @Override
    public User update(final UpdateUserCommand command) {
        return userService.update(command);
    }

    @Override
    public void changePassword(final UserId userId, final String encodedPassword) {
        this.userService.changePassword(userId, encodedPassword);
    }

    @Override
    public void changeLanguage(final UserId userId, final String language) {
        this.userService.changeLanguage(userId, language);
    }

    @Override
    public String regenerateApiKey(final UserId userId) {
        final String apiKey = generateApiKey();
        this.userService.changeApiKey(userId, apiKey);
        return apiKey;
    }

    @Override
    public void deleteApiKey(final UserId userId) {
        this.userService.changeApiKey(userId, null);
    }

    private String generateApiKey() {
        final byte[] randomBytes = new byte[API_KEY_SIZE_BYTES];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    @Override
    public void changeRole(final UserId userId, final User.Role role) {
        this.userService.changeRole(userId, role);
    }

    @Override
    public void changeAdminDepartmentInfo(final UserDepartmentUpdateRequest request) {
        this.userService.updateDefaultDepartmentUser(request);
    }

    @Override
    public Result<Void, String> addPermission(final UserId userIdToModify, final String permission) {

        final UserId currentlyLoggedUser = authenticationService.currentUserId();

        if (userIdToModify.equals(currentlyLoggedUser)) {
            return Result.failure("You cannot add permission to yourself");
        }

        final RolePermission rolePermission = new RolePermission(UserPermission.valueOf(permission));

        final User user = this.userService.findUserById(userIdToModify);

        if (rolePermission.isAdmin()) {
            if (user != null && !user.isAdmin()) {
                return Result.failure("UserPermission " + permission + " cannot be assigned to nonadmin user");
            }
        }

        this.userService.addPermission(userIdToModify, permission);

        return Result.success();
    }

    @Override
    public Result<Void, String> removePermission(final UserId userIdToModify, final String permission) {

        final UserId currentlyLoggedUser = authenticationService.currentUserId();

        if (userIdToModify.equals(currentlyLoggedUser)) {
            return Result.failure("You cannot remove permission for yourself");
        }

        this.userService.removePermission(userIdToModify, permission);

        return Result.success();
    }

    @Override
    public void deleteDataForDepartment(final DepartmentCode departmentCode) {
        log.info("Deleting data for department {}", departmentCode.getValue());
        final List<UserId> users = this.userService.findAllActiveUsersByDepartmentCode(departmentCode);
        for (final UserId user : users) {
            log.info("Deleting data for user {}", user.getValue());
            this.userService.deleteDataForUser(user);
        }
    }

    @Override
    public DepartmentCode getDepartmentCode(final DepartmentId departmentId) {
        return userService.getDepartmentCode(departmentId);
    }

    @Override
    public void changeFullName(final FullNameChangeCommand fullNameChangeCommand) {
        this.userService.changeFullName(fullNameChangeCommand);
    }
}
