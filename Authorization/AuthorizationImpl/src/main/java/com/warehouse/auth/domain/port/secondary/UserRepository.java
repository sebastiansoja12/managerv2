package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void createOrUpdate(final OrganizationUser user);

    OrganizationUser findByUsername(final String username);

    Optional<OrganizationUser> findByApiKeyHash(final String apiKeyHash);

    OrganizationUser findById(final UserId userId);

    List<OrganizationUser> findAll();

    List<UserId> findAllActiveUsersByDepartmentId(final DepartmentId departmentId);

    OrganizationUser findByEmail(final String email);

    UserId findInitialUser();

    Optional<OrganizationUser> findActiveSuperadmin(final OperatorId operatorId);

    List<UserId> findActiveSuperadminIds(final OperatorId operatorId);
}
