package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.vo.User;
import com.warehouse.terminal.domain.vo.UserToken;

public interface UserService {
    User findByUsername(final String username);
    UserToken validateUser(final UserId userId);
}
