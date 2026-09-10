package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.User;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public interface UserRepository {

    void createOrUpdate(final User user);

    User findByUsername(final String username);

    User findByApiKey(final String apiKey);

    User findById(final UserId userId);

    List<User> findAll();

    List<UserId> findAllActiveUsersByDepartmentId(final DepartmentId departmentId);

    User findByEmail(final String email);

    UserId findInitialUser();
}
