package com.warehouse.auth.configuration.access;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.api.DepartmentApiService;

@Component
public class AccessUserControlEvaluator {

    private final UserService userService;
    private final DepartmentApiService departmentApiService;

    public AccessUserControlEvaluator(final UserService userService,
                                      final DepartmentApiService departmentApiService) {
        this.userService = userService;
        this.departmentApiService = departmentApiService;
    }

    public void validate(final Authentication authentication,
                         final AccessUserControl accessUserControl) {
        if (accessUserControl == null) {
            return;
        }

        validateAuthentication(authentication);

        final UserId userId = resolveUserId(authentication);
        final User user = userService.findUserById(userId);
        validateActiveUser(user);
        validateActiveDepartment(user.getDepartmentCode());
        validatePermissions(user, accessUserControl);
    }

    private void validateAuthentication(final Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw unauthorized("Authenticated user is required");
        }
    }

    private UserId resolveUserId(final Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserId userId) {
            return userId;
        }

        throw unauthorized("Authenticated user identifier is missing");
    }

    private void validateActiveUser(final User user) {
        if (user == null || Boolean.TRUE.equals(user.isDeleted())) {
            throw forbidden("User is inactive or deleted");
        }
    }

    private void validateActiveDepartment(final DepartmentCode departmentCode) {
        if (departmentCode == null || !StringUtils.hasText(departmentCode.getValue())) {
            throw forbidden("User department is missing");
        }

        if (departmentApiService.checkIfDepartmentExists(departmentCode) == null) {
            throw forbidden("User department is inactive or deleted");
        }
    }

    private void validatePermissions(final User user,
                                     final AccessUserControl accessUserControl) {
        final Set<String> requiredPermissions = resolveRequiredPermissions(accessUserControl);
        if (requiredPermissions.isEmpty()) {
            return;
        }

        final Set<String> userPermissions = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        final boolean permitted = accessUserControl.requireAllPermissions()
                ? userPermissions.containsAll(requiredPermissions)
                : requiredPermissions.stream().anyMatch(userPermissions::contains);

        if (!permitted) {
            throw forbidden("User does not have required permissions");
        }
    }

    private Set<String> resolveRequiredPermissions(final AccessUserControl accessUserControl) {
        return Stream.concat(
                        Arrays.stream(accessUserControl.value()),
                        Arrays.stream(accessUserControl.permissions()).map(UserPermission::name)
                )
                .filter(StringUtils::hasText)
                .collect(Collectors.toSet());
    }

    private ProblemDetailsException unauthorized(final String detail) {
        return new ProblemDetailsException("about:blank", "Unauthorized", 401, detail);
    }

    private ProblemDetailsException forbidden(final String detail) {
        return new ProblemDetailsException("about:blank", "Forbidden", 403, detail);
    }
}
