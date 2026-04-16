package com.warehouse.auth.domain.event;

import com.warehouse.auth.domain.vo.UserSnapshot;

public class UserChangedEvent implements UserEvent {
    private final UserSnapshot snapshot;

    public UserChangedEvent(final UserSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public UserSnapshot getSnapshot() {
        return snapshot;
    }
}
