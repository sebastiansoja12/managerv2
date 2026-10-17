package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.Account;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.vo.Impersonation;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

public interface JwtService {
    String extractUsername(String jwt);

    Optional<DepartmentId> extractDepartmentId(String jwt);

    String generateToken(Map<String, Object> extraClaims, Account account, Long expiration);

    String generateToken(final OrganizationUser user);

    String generateToken(final String firstName, final String username, final OrganizationUser.Role role, final DepartmentCode departmentCode);

    boolean isTokenValid(String token, OrganizationUser user);

    String generatePlatformOwnerToken(final PlatformOwner platformOwner);

    boolean isPlatformOwnerTokenValid(final String token, final PlatformOwner platformOwner);

    String generateImpersonationToken(final OrganizationUser user,
                                      final PlatformOwner platformOwner,
                                      final Long consentId,
                                      final java.time.Instant accessExpiresAt);

    Optional<Impersonation> extractImpersonation(final String token);

    Optional<Instant> extractIssuedAt(final String token);
}
