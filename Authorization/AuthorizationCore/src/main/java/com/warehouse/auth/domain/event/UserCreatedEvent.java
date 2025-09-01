package com.warehouse.auth.domain.event;

import com.warehouse.auth.domain.vo.UserSnapshot;

public class UserCreatedEvent extends UserChangedEvent {
    public UserCreatedEvent(final UserSnapshot snapshot) {
        super(snapshot);
    }
}
