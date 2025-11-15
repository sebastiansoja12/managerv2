package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.commonassets.identificator.UserId;

public interface UserRepository {

    UserResponse createOrUpdate(User user);

    User findByUsername(String username);

    User findByApiKey(final String apiKey);

    User findById(final UserId userId);
}
