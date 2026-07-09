package com.warehouse.auth.domain.event;

import com.warehouse.auth.domain.vo.UserSnapshot;

public class UserFullNameChangedEvent extends UserChangedEvent implements UserEvent {
    public UserFullNameChangedEvent(final UserSnapshot snapshot) {
        super(snapshot);
    }
}
