package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.helper.Result;
import com.warehouse.auth.domain.model.FullNameRequest;
import com.warehouse.auth.domain.model.RolePermission;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.identificator.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;


@Slf4j
public class UserPortImpl implements UserPort {

    private final UserService userService;

    public UserPortImpl(final UserService userService) {
        this.userService = userService;
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
        final User.Role role1 = User.Role.ADMIN;
        //role1.setPermissions(Set.of(User.Permission.MANAGER_UPDATE));
    }

    @Override
    public Result<Void, String> addPermission(final UserId userId, final String permission) {

        final UserId currentlyLoggedUser = (UserId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userId.equals(currentlyLoggedUser)) {
            return Result.failure("You cannot add permission to yourself");
        }

        final RolePermission rolePermission = new RolePermission(User.Permission.valueOf(permission));

        final User user = this.userService.findUserById(userId);

        if (rolePermission.isAdmin()) {
            if (!user.isAdmin()) {
                return Result.failure("Permission " + permission + " cannot be assigned to nonadmin user");
            }
        }

        this.userService.addPermission(userId, permission);

        return Result.success();
    }
}
