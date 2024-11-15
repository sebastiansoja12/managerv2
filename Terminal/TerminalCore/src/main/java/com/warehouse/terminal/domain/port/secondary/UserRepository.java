package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.vo.User;

public interface UserRepository {
    User findByUsername(final String username);
    User findById(final UserId userId);
    Boolean existsById(final UserId userId);
}
