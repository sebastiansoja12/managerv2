package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.Account;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.vo.Impersonation;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    private static final String TOKEN_TYPE_CLAIM = "tokenType";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String PLATFORM_OWNER_ACCESS_TOKEN_TYPE = "platformOwnerAccess";
    private static final String IMPERSONATED_BY_CLAIM = "impersonatedBy";
    private static final String IMPERSONATED_BY_USERNAME_CLAIM = "impersonatedByUsername";
    private static final String IMPERSONATION_CONSENT_CLAIM = "impersonationConsentId";
    private static final long IMPERSONATION_EXPIRATION_MILLIS = 1_800_000L;

    @NonNull
    private final JwtProvider jwtProvider;

    @Override
    public String extractUsername(final String token) {
        return getUsername(token);
    }

    @Override
    public Optional<DepartmentId> extractDepartmentId(final String token) {
        return Optional.ofNullable(extractAllClaims(token).get("departmentId"))
                .map(Object::toString)
                .map(Long::valueOf)
                .map(DepartmentId::new);
    }

    @Override
    public String generateToken(final Map<String, Object> extraClaims, final Account account, final Long expiration) {
        extraClaims.putIfAbsent(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(account.getUsername())
                .setIssuer(jwtProvider.getIssuer())
                .setAudience(jwtProvider.getAudience())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.forSigningKey(getSigningKey()))
                .compact();
    }

    @Override
    public String generateToken(final OrganizationUser user) {
        return generateToken(userClaims(user), user, jwtProvider.getExpiration());
    }

    @Override
    public String generateImpersonationToken(final OrganizationUser user,
                                             final PlatformOwner platformOwner,
                                             final Long consentId,
                                             final Instant accessExpiresAt) {
        final Map<String, Object> claimsMap = userClaims(user);
        claimsMap.put(IMPERSONATED_BY_CLAIM, platformOwner.getPlatformOwnerId().value());
        claimsMap.put(IMPERSONATED_BY_USERNAME_CLAIM, platformOwner.getUsername());
        if (consentId != null) {
            claimsMap.put(IMPERSONATION_CONSENT_CLAIM, consentId);
        }
        long expiration = Math.min(jwtProvider.getExpiration(), IMPERSONATION_EXPIRATION_MILLIS);
        if (accessExpiresAt != null) {
            expiration = Math.max(1_000L, Math.min(expiration, accessExpiresAt.toEpochMilli() - System.currentTimeMillis()));
        }
        return generateToken(claimsMap, user, expiration);
    }

    @Override
    public Optional<Impersonation> extractImpersonation(final String token) {
        final Claims claims = extractAllClaims(token);
        return Optional.ofNullable(claims.get(IMPERSONATED_BY_CLAIM))
                .map(Object::toString)
                .map(Long::valueOf)
                .map(platformOwnerId -> new Impersonation(new PlatformOwnerId(platformOwnerId),
                        claims.get(IMPERSONATED_BY_USERNAME_CLAIM, String.class),
                        Optional.ofNullable(claims.get(IMPERSONATION_CONSENT_CLAIM))
                                .map(Object::toString)
                                .map(Long::valueOf)
                                .orElse(null)));
    }

    private Map<String, Object> userClaims(final OrganizationUser user) {
        final Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("firstName", user.getFirstName());
        claimsMap.put("username", user.getUsername());
        claimsMap.put("userId", user.getUserId().value());
        claimsMap.put("operatorId", user.operatorId().value());
        claimsMap.put("departmentId", user.getDepartmentId().getValue());
        return claimsMap;
    }

    @Override
    public String generateToken(final String firstName, final String username, final OrganizationUser.Role role, final DepartmentCode departmentCode) {
        final Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("firstName", firstName);
        claimsMap.put("username", username);
        final Long expiration = jwtProvider.getExpiration();
        return Jwts
                .builder()
                .setClaims(claimsMap)
                .setSubject(username)
                .setIssuer(jwtProvider.getIssuer())
                .setAudience(jwtProvider.getAudience())
                .claim(TOKEN_TYPE_CLAIM, "apiKey")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.forSigningKey(getSigningKey()))
                .compact();
    }

    @Override
    public boolean isTokenValid(final String token, final OrganizationUser user) {
        final Claims claims = extractAllClaims(token);
        final String username = claims.getSubject();
        final String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
        return username.equals(user.getUsername())
                && ACCESS_TOKEN_TYPE.equals(tokenType)
                && !claims.getExpiration().before(new Date())
                && !Boolean.TRUE.equals(user.isDeleted());
    }

    @Override
    public String generatePlatformOwnerToken(final PlatformOwner platformOwner) {
        final Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(TOKEN_TYPE_CLAIM, PLATFORM_OWNER_ACCESS_TOKEN_TYPE);
        claimsMap.put("firstName", platformOwner.getFirstName());
        claimsMap.put("username", platformOwner.getUsername());
        claimsMap.put("platformOwnerId", platformOwner.getPlatformOwnerId().value());
        return generateToken(claimsMap, platformOwner, jwtProvider.getExpiration());
    }

    @Override
    public boolean isPlatformOwnerTokenValid(final String token, final PlatformOwner platformOwner) {
        final Claims claims = extractAllClaims(token);
        final String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
        return claims.getSubject().equals(platformOwner.getUsername())
                && PLATFORM_OWNER_ACCESS_TOKEN_TYPE.equals(tokenType)
                && !claims.getExpiration().before(new Date())
                && !Boolean.TRUE.equals(platformOwner.isDeleted());
    }

    private String getUsername(final String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("sub", String.class);
    }

    @Override
    public Optional<Instant> extractIssuedAt(final String token) {
        return Optional.ofNullable(extractAllClaims(token).getIssuedAt()).map(Date::toInstant);
    }

    private Claims extractAllClaims(final String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .requireIssuer(jwtProvider.getIssuer())
                .requireAudience(jwtProvider.getAudience())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(jwtProvider.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
