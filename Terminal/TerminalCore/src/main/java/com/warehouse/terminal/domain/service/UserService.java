package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.vo.User;
import com.warehouse.terminal.domain.vo.UserToken;

public interface UserService {
    User findByUsername(final Username username);
    UserToken validateUser(final UserId userId);
    Boolean existsByUserId(final UserId userId);
    Boolean existsByUsername(final Username username);
}
