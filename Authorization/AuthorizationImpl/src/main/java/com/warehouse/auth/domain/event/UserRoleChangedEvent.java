package com.warehouse.auth.domain.event;

import com.warehouse.auth.domain.vo.UserSnapshot;

public class UserRoleChangedEvent extends UserChangedEvent implements UserEvent {
    public UserRoleChangedEvent(final UserSnapshot snapshot) {
        super(snapshot);
    }
}
