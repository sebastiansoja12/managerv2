package com.warehouse.account.infrastructure.adapter.primary;

import com.warehouse.account.application.exception.UserInvitationNotFoundException;
import com.warehouse.account.application.port.primary.SupplierAccountPort;
import com.warehouse.account.application.port.primary.command.AcceptUserInvitationCommand;
import com.warehouse.account.application.port.primary.result.UserInvitationResult;
import com.warehouse.account.domain.exception.InvalidUserInvitationException;
import com.warehouse.account.domain.vo.InvitationToken;
import com.warehouse.account.infrastructure.adapter.primary.dto.AcceptUserInvitationApiRequest;
import com.warehouse.account.infrastructure.adapter.primary.dto.UserInvitationApiResponse;
import com.warehouse.account.infrastructure.adapter.primary.dto.UserInvitationErrorApiResponse;
import com.warehouse.account.infrastructure.adapter.primary.dto.UserInvitationTokenApiRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/invitations")
public class UserInvitationController {

    private final SupplierAccountPort supplierAccountPort;

    public UserInvitationController(final SupplierAccountPort supplierAccountPort) {
        this.supplierAccountPort = supplierAccountPort;
    }

    @PostMapping("/verify")
    public ResponseEntity<UserInvitationApiResponse> verify(
            @RequestBody final UserInvitationTokenApiRequest tokenRequest) {
        final UserInvitationResult result = this.supplierAccountPort.getInvitation(token(tokenRequest.token()));
        return ResponseEntity.ok(new UserInvitationApiResponse(result.username(), result.expiresAt()));
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> accept(@RequestBody final AcceptUserInvitationApiRequest acceptRequest) {
        this.supplierAccountPort.acceptInvitation(new AcceptUserInvitationCommand(
                token(acceptRequest.token()), acceptRequest.password()));
        return ResponseEntity.noContent().build();
    }

    private InvitationToken token(final String value) {
        if (value == null || value.isBlank()) {
            throw new UserInvitationNotFoundException();
        }
        return new InvitationToken(value);
    }

    @ExceptionHandler(UserInvitationNotFoundException.class)
    public ResponseEntity<UserInvitationErrorApiResponse> handleNotFound(final UserInvitationNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new UserInvitationErrorApiResponse("INVITATION_NOT_FOUND", "User invitation not found"));
    }

    @ExceptionHandler(InvalidUserInvitationException.class)
    public ResponseEntity<UserInvitationErrorApiResponse> handleInvalid(final InvalidUserInvitationException exception) {
        return ResponseEntity.badRequest()
                .body(new UserInvitationErrorApiResponse(exception.getFailure().name(), exception.getMessage()));
    }
}
