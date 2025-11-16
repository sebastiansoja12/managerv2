package com.warehouse.auth.domain.event;

import com.warehouse.auth.domain.vo.UserSnapshot;

public class UserLoggedOutEvent extends UserChangedEvent implements UserEvent {
    private final UserSnapshot snapshot;

    public UserLoggedOutEvent(final UserSnapshot snapshot) {
        super(snapshot);
        this.snapshot = snapshot;
    }

    public UserSnapshot getSnapshot() {
        return snapshot;
    }
}
