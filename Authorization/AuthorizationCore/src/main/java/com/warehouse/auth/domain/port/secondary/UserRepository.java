package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public interface UserRepository {

    UserResponse createOrUpdate(final User user);

    User findByUsername(final String username);

    User findByApiKey(final String apiKey);

    User findById(final UserId userId);

    List<UserId> findAllActiveUsersByDepartmentCode(final DepartmentCode departmentCode);

    User findByEmail(final String email);
}
