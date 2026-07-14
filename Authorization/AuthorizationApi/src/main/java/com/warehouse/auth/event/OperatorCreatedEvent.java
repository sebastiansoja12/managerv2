package com.warehouse.auth.event;

import com.warehouse.auth.infrastructure.dto.RegisteringUserDto;
import com.warehouse.commonassets.identificator.UserId;

import java.util.function.Consumer;

public class OperatorCreatedEvent {
    private final RegisteringUserDto registeringUser;
    private final Consumer<UserId> userCreatedId;

    public OperatorCreatedEvent(final RegisteringUserDto registeringUser) {
        this(registeringUser, userId -> {
        });
    }

    public OperatorCreatedEvent(final RegisteringUserDto registeringUser, final Consumer<UserId> userCreatedId) {
        this.registeringUser = registeringUser;
        this.userCreatedId = userCreatedId;
    }

    public RegisteringUserDto getRegisteringUser() {
        return registeringUser;
    }

    public Consumer<UserId> getUserCreatedId() {
        return userCreatedId;
    }
}
