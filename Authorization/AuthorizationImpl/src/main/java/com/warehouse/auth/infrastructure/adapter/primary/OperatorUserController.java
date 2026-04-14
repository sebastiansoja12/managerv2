package com.warehouse.auth.infrastructure.adapter.primary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.auth.domain.model.CreateOperatorUserCommand;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.OperatorUserRequestMapper;
import com.warehouse.auth.infrastructure.dto.CreateOperatorUserApiRequest;
import com.warehouse.auth.infrastructure.dto.UserIdDto;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.enumeration.UserPermission;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/operators/{operatorId}/users")
public class OperatorUserController {

    private final AuthenticationPort authenticationPort;

    public OperatorUserController(final AuthenticationPort authenticationPort) {
        this.authenticationPort = authenticationPort;
    }

    @PostMapping
    @AccessUserControl(permissions = {UserPermission.ROLE_OPERATOR_CREATE})
    public ResponseEntity<UserIdDto> create(@PathVariable @Positive final Long operatorId,
                                            @Valid @RequestBody final CreateOperatorUserApiRequest request) {
        final CreateOperatorUserCommand command = OperatorUserRequestMapper.toCommand(
                OperatorId.of(operatorId), request
        );
        final UserId userId = authenticationPort.createOperatorUser(command);
        return ResponseEntity.ok(new UserIdDto(userId.value()));
    }
}
