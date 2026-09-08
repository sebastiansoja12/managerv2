package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.UserApiService;
import com.warehouse.chat.application.port.secondary.ChatUserServicePort;
import com.warehouse.chat.domain.vo.ChatRecipient;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

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
}
