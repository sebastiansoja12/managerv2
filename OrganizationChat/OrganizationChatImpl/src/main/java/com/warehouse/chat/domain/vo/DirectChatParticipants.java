package com.warehouse.chat.domain.vo;

import com.warehouse.chat.domain.exception.ChatException;
import com.warehouse.commonassets.identificator.UserId;

import static com.warehouse.chat.domain.exception.ChatException.Reason.ACCESS_DENIED;
import static com.warehouse.chat.domain.exception.ChatException.Reason.INVALID_REQUEST;

public record DirectChatParticipants(UserId firstUserId, UserId secondUserId) {

    public DirectChatParticipants(final UserId firstUserId, final UserId secondUserId) {
        if (firstUserId == null || secondUserId == null || firstUserId.equals(secondUserId)) {
            throw new ChatException(INVALID_REQUEST, "A direct conversation requires another user");
        }
        final boolean ordered = firstUserId.value() < secondUserId.value();
        this.firstUserId = ordered ? firstUserId : secondUserId;
        this.secondUserId = ordered ? secondUserId : firstUserId;
    }

    public UserId otherUser(final UserId userId) {
        requireParticipant(userId);
        return firstUserId.equals(userId) ? secondUserId : firstUserId;
    }

    public void requireParticipant(final UserId userId) {
        if (!firstUserId.equals(userId) && !secondUserId.equals(userId)) {
            throw new ChatException(ACCESS_DENIED, "User is not a participant of this conversation");
        }
    }
}
