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
import java.util.function.Function;

@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    @NonNull
    private final JwtProvider jwtProvider;

    @Override
    public String extractUsername(String token) {
        return getUsername(token);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, User user, Long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.forSigningKey(getSigningKey()))
                .compact();
    }

    @Override
    public String generateToken(User user) {
        final Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("firstName", user.getFirstName());
        claimsMap.put("username", user.getUsername());
        claimsMap.put("userId", user.getUserId().value());
        claimsMap.put("tenant", user.getDepartmentCode().getValue());
        final Long expiration = jwtProvider.getExpiration();
        return generateToken(claimsMap, user, expiration);
    }

    @Override
    public String generateToken(final String firstName, final String username, final User.Role role, final DepartmentCode departmentCode) {
        final Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("firstName", firstName);
        claimsMap.put("username", username);
        claimsMap.put("tenant", departmentCode);
        final Long expiration = jwtProvider.getExpiration();
        return Jwts
                .builder()
                .setClaims(claimsMap)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.forSigningKey(getSigningKey()))
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token) && !user.isDeleted();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private String getUsername(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("sub", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(jwtProvider.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
