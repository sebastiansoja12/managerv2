package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.helper.Result;
import com.warehouse.auth.domain.model.FullNameRequest;
import com.warehouse.auth.domain.model.RolePermission;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.identificator.UserId;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class UserPortImpl implements UserPort {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    public UserPortImpl(final UserService userService,
                        final AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @Override
    public User findUser(final String username) {
        return userService.findUser(username);
    }

    @Override
    public void updateFullName(final FullNameRequest request) {
        this.userService.changeFullName(request);
    }

    @Override
    public void changeRole(final UserId userId, final User.Role role) {
        this.userService.changeRole(userId, role);
    }

    @Override
    public Result<Void, String> addPermission(final UserId userIdToModify, final String permission) {

        final UserId currentlyLoggedUser = authenticationService.currentUserId();

        if (userIdToModify.equals(currentlyLoggedUser)) {
            return Result.failure("You cannot add permission to yourself");
        }

        final RolePermission rolePermission = new RolePermission(User.Permission.valueOf(permission));

        final User user = this.userService.findUserById(userIdToModify);

        if (rolePermission.isAdmin()) {
            if (user != null && !user.isAdmin()) {
                return Result.failure("Permission " + permission + " cannot be assigned to nonadmin user");
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
}
