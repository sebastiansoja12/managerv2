package com.warehouse.auth.infrastructure.adapter.primary;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.auth.domain.helper.Result;
import com.warehouse.auth.domain.model.FullNameRequest;
import com.warehouse.auth.domain.model.UpdateUserCommand;
import com.warehouse.auth.domain.model.CreateUserCommand;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.CurrentOperatorPort;
import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.domain.service.JwtDecodeService;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.UserRequestMapper;
import com.warehouse.auth.infrastructure.adapter.primary.validator.RoleValidator;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.BusinessException;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.TechnicalException;
import com.warehouse.auth.infrastructure.dto.FullNameRequestApiDto;
import com.warehouse.auth.infrastructure.dto.UpdateUserApiRequest;
import com.warehouse.auth.infrastructure.dto.CreateUserApiRequest;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.auth.infrastructure.dto.UserIdDto;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.enumeration.UserPermission;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@AccessUserControl
public class UserResourceController {

    private final UserPort userPort;
    private final AuthenticationPort authenticationPort;

    private final JwtDecodeService jwtDecodeService;

    private final CurrentOperatorPort currentOperatorPort;

    public UserResourceController(final UserPort userPort,
                                  final AuthenticationPort authenticationPort,
                                  final JwtDecodeService jwtDecodeService,
                                  final CurrentOperatorPort currentOperatorPort) {
        this.userPort = userPort;
        this.authenticationPort = authenticationPort;
        this.jwtDecodeService = jwtDecodeService;
        this.currentOperatorPort = currentOperatorPort;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAllUsers() {
        return ResponseEntity.ok(userPort.findAll()
                .stream()
                .map(ResponseMapper::map)
                .toList());
    }

    @PostMapping
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_CREATE})
    public ResponseEntity<UserIdDto> createUser(
            @Valid @RequestBody final CreateUserApiRequest request) {
        final CreateUserCommand command = UserRequestMapper.toCommand(request);
        final UserId userId = authenticationPort.createUser(command);
        return ResponseEntity.ok(new UserIdDto(userId.value()));
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

    @PutMapping("/{id}")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_CREATE, UserPermission.ROLE_MANAGER_CREATE})
    public ResponseEntity<UserDto> updateUser(@PathVariable final Long id,
                                              @Valid @RequestBody final UpdateUserApiRequest request) {
        final UpdateUserCommand command = UserRequestMapper.toCommand(new UserId(id), request);
        return ResponseEntity.ok(ResponseMapper.map(userPort.update(command)));
    }

    @PutMapping("/roles/{id}")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_CREATE})
    public ResponseEntity<?> changeUserRole(@PathVariable final Long id,
                                            @RequestParam("role") final String userRole) {
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
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_CREATE, UserPermission.ROLE_MANAGER_CREATE})
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
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_CREATE, UserPermission.ROLE_MANAGER_CREATE})
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
