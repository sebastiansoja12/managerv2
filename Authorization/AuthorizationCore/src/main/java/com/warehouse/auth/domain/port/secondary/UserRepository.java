package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.UserResponse;

public interface UserRepository {

    UserResponse saveUser(User user);

    User findByUsername(String username);
}
