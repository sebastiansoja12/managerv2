package com.warehouse.auth.infrastructure.adapter.primary;


import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.auth.domain.helper.Result;
import com.warehouse.auth.domain.model.FullNameRequest;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.CurrentOperatorPort;
import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.domain.service.JwtDecodeService;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.auth.infrastructure.adapter.primary.validator.RoleValidator;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.BusinessException;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.TechnicalException;
import com.warehouse.auth.infrastructure.dto.FullNameRequestApiDto;
import com.warehouse.commonassets.identificator.UserId;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@AccessUserControl
public class UserResourceController {

    private final UserPort userPort;

    private final JwtDecodeService jwtDecodeService;

    private final CurrentOperatorPort currentOperatorPort;

    public UserResourceController(final UserPort userPort, final JwtDecodeService jwtDecodeService,
                                  final CurrentOperatorPort currentOperatorPort) {
        this.userPort = userPort;
        this.jwtDecodeService = jwtDecodeService;
        this.currentOperatorPort = currentOperatorPort;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> findUserByUsername(@PathVariable final String username) {
        final User user = userPort.findUser(username);
        return new ResponseEntity<>(ResponseMapper.map(user), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody final FullNameRequestApiDto fullNameRequest) {
        final FullNameRequest request = new FullNameRequest(
                fullNameRequest.firstName(), fullNameRequest.lastName(), fullNameRequest.username(), null
        );
        this.userPort.updateFullName(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/roles/{id}")
    @AccessUserControl("ROLE_ADMIN_CREATE")
    public ResponseEntity<?> changeUserRole(@PathVariable final Long id, @Param("role") final String userRole) {
        final UserId userId = new UserId(id);
        final Result<Void, List<String>> validatorResult = new RoleValidator().validateRole(userRole);

        if (validatorResult.isFailure()) {
            return ResponseEntity.badRequest().body(validatorResult.getFailure());
        }


        final User.Role role = User.Role.valueOf(userRole);
        this.userPort.changeRole(userId, role);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/permissions/{id}")
    @AccessUserControl(permissions = {"ROLE_ADMIN_CREATE", "ROLE_MANAGER_CREATE"})
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

    @DeleteMapping("/permissions/{id}")
    @AccessUserControl(permissions = {"ROLE_ADMIN_CREATE", "ROLE_MANAGER_CREATE"})
    public ResponseEntity<?> removeUserPermission(@PathVariable final Long id, @RequestParam("permission") final String permission) {

        final UserId userId = new UserId(id);

        final Result<Void, List<String>> validatorResult = new RoleValidator().validatePermission(permission);

        if (validatorResult.isFailure()) {
            return ResponseEntity.badRequest().body(validatorResult.getFailure());
        }

        final Result<Void, String> result = this.userPort.removePermission(userId, permission);

        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(result.getFailure());
        }

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(exception = {BusinessException.class, TechnicalException.class})
    public ResponseEntity<?> handleUserNotFoundException() {
        return ResponseEntity.notFound().build();
    }
}
