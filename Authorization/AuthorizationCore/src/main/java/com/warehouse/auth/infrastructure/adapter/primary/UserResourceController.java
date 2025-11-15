package com.warehouse.auth.infrastructure.adapter.primary;


import com.warehouse.auth.domain.helper.Result;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.domain.service.ApiKeyService;
import com.warehouse.auth.infrastructure.adapter.primary.dto.FullNameRequest;
import com.warehouse.auth.infrastructure.adapter.primary.validator.RoleValidator;
import com.warehouse.commonassets.identificator.UserId;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserResourceController {

    private final UserPort userPort;

    private final ApiKeyService apiKeyService;

    public UserResourceController(final UserPort userPort, final ApiKeyService apiKeyService) {
        this.userPort = userPort;
        this.apiKeyService = apiKeyService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> findUserByUsername(@PathVariable final String username) {
        final User user = userPort.findUser(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody final FullNameRequest fullNameRequest) {
        final com.warehouse.auth.domain.model.FullNameRequest request = new com.warehouse.auth.domain.model.FullNameRequest(
                fullNameRequest.firstName(), fullNameRequest.lastName(), fullNameRequest.username(), null
        );
        this.userPort.updateFullName(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/roles/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN_CREATE')")
    public ResponseEntity<?> changeUserRole(@PathVariable final Long id, @Param("role") final String userRole) {
        final UserId userId = new UserId(id);
        final User.Role role = User.Role.valueOf(userRole);
        this.userPort.changeRole(userId, role);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/add-permission/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_CREATE', 'ROLE_MANAGER_CREATE')")
    public ResponseEntity<?> addUserPermission(@PathVariable final Long id, @RequestParam("permission") final String permission) {

        final UserId userId = new UserId(id);

        final Result<Void, List<String>> validatorResult = new RoleValidator().validatePermission(permission);

        if (validatorResult.isFailure()) {
            return ResponseEntity.badRequest().body(validatorResult.getFailure());
        }

        final Result<Void, String> result = this.userPort.addPermission(userId, permission);

        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(result.getFailure());
        }

        return ResponseEntity.ok().build();
    }
}
