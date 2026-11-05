package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.UserApiService;
import com.warehouse.chat.application.port.secondary.ChatUserServicePort;
import com.warehouse.chat.domain.vo.ChatParticipant;
import com.warehouse.chat.domain.vo.ChatRecipient;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;
import java.util.Optional;

public class ChatUserServiceAdapter implements ChatUserServicePort {

    private final CurrentUserApiService currentUserApiService;
    private final CurrentOperatorService currentOperatorService;
    private final UserApiService userApiService;

    public ChatUserServiceAdapter(final CurrentUserApiService currentUserApiService,
                                 final CurrentOperatorService currentOperatorService,
                                 final UserApiService userApiService) {
        this.currentUserApiService = currentUserApiService;
        this.currentOperatorService = currentOperatorService;
        this.userApiService = userApiService;
    }

    @Override
    public UserId currentUserId() {
        return currentUserApiService.getCurrentUserId();
    }

    @Override
    public OperatorId currentOperatorId() {
        return currentOperatorService.getCurrentOperatorId();
    }

    @Override
    public Optional<ChatRecipient> findRecipient(final UserId userId) {
        return Optional.ofNullable(userApiService.findById(userId))
                .map(recipient -> new ChatRecipient(
                        recipient.operatorId() == null ? null : OperatorId.of(recipient.operatorId().value()),
                        !Boolean.TRUE.equals(recipient.deleted())));
    }

    @Override
    public List<ChatParticipant> findActiveParticipants() {
        return userApiService.findAll()
                .stream()
                .filter(user -> !Boolean.TRUE.equals(user.deleted()))
                .map(user -> new ChatParticipant(new UserId(user.userId().value()), user.username(),
                        user.firstName(), user.lastName(),
                        user.departmentCode() == null ? null : new DepartmentCode(user.departmentCode()),
                        user.role(),
                        user.operatorId() == null ? null : OperatorId.of(user.operatorId().value())))
                .toList();
    }
}
