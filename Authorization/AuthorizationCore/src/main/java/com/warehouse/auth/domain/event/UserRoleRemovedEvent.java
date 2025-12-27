package com.warehouse.auth.domain.event;

import com.warehouse.auth.domain.vo.UserSnapshot;

public class UserRoleRemovedEvent extends UserChangedEvent implements UserEvent {
    public UserRoleRemovedEvent(final UserSnapshot snapshot) {
        super(snapshot);
    }
}
