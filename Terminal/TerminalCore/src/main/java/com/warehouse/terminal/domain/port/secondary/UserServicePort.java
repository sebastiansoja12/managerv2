package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.vo.User;

public interface UserServicePort {
    User findUserById(final UserId userId);
}
