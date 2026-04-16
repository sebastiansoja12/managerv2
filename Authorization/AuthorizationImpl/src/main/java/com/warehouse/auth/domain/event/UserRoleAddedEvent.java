package com.warehouse.auth.domain.event;

import com.warehouse.auth.domain.vo.UserSnapshot;

public class UserRoleAddedEvent extends UserChangedEvent implements UserEvent {
    public UserRoleAddedEvent(final UserSnapshot snapshot) {
        super(snapshot);
    }
}
