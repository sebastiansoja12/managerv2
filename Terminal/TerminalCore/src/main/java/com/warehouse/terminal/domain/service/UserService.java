package com.warehouse.terminal.domain.service;

import com.warehouse.terminal.domain.vo.User;

public interface UserService {
    User findByUsername(final String username);
}
