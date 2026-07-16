package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.commonassets.identificator.DepartmentCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    private static final String TOKEN_TYPE_CLAIM = "tokenType";
    private static final String ACCESS_TOKEN_TYPE = "access";

    @NonNull
    private final JwtProvider jwtProvider;

    @Override
    public String extractUsername(final String token) {
        return getUsername(token);
    }

    @Override
    public String generateToken(final Map<String, Object> extraClaims, final User user, final Long expiration) {
        extraClaims.put(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuer(jwtProvider.getIssuer())
                .setAudience(jwtProvider.getAudience())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.forSigningKey(getSigningKey()))
                .compact();
    }

    @Override
    public String generateToken(final User user) {
        final Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("firstName", user.getFirstName());
        claimsMap.put("username", user.getUsername());
        claimsMap.put("userId", user.getUserId().value());
        claimsMap.put("operatorId", user.operatorId().value());
        final Long expiration = jwtProvider.getExpiration();
        return generateToken(claimsMap, user, expiration);
    }

    @Override
    public String generateToken(final String firstName, final String username, final User.Role role, final DepartmentCode departmentCode) {
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
    public boolean isTokenValid(final String token, final User user) {
        final Claims claims = extractAllClaims(token);
        final String username = claims.getSubject();
        final String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
        return username.equals(user.getUsername())
                && ACCESS_TOKEN_TYPE.equals(tokenType)
                && !claims.getExpiration().before(new Date())
                && !Boolean.TRUE.equals(user.isDeleted());
    }

    private String getUsername(final String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("sub", String.class);
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
