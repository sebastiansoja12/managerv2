package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.dto.OperatorIdDto;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.auth.infrastructure.dto.UserIdDto;

import java.util.Set;

import static com.warehouse.chat.ChatFixture.*;

final class ChatUserFixture {

    private ChatUserFixture() {
    }

    static UserDto recipient(final Boolean deleted, final OperatorIdDto operatorId) {
        return new UserDto(new UserIdDto(PARTICIPANT_USER_ID.value()), "chat.user", "Chat", "User",
                "chat@example.com", "USER", "WAW-01", "pl", Set.of(), deleted, operatorId, NOW, NOW);
    }
}
