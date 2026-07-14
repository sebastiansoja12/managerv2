package com.warehouse.returning.domain.service;

import java.security.Key;

import org.springframework.stereotype.Service;

import com.warehouse.returning.domain.provider.JwtProvider;
import com.warehouse.returning.domain.vo.DecodedApiOperator;
import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.UserId;
import com.warehouse.returning.infrastructure.adapter.secondary.exception.RestException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class ApiKeyService {

    private final JwtProvider jwtProvider;

    public ApiKeyService(final JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public DecodedApiOperator decodeJwt(final String token) {
        try {
            final Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            final UserId userId = new UserId(claims.get("userId", Long.class));
            final DepartmentCode departmentCode = new DepartmentCode(claims.get("tenant", String.class));
            final Long operatorId = extractLongClaim(claims, "operatorId", "operator_id");
            final String issuer = claims.get("username", String.class);

            return new DecodedApiOperator(userId, departmentCode, operatorId, issuer);
        } catch (SignatureException | IllegalArgumentException e) {
            throw new RestException(401, "Invalid or expired JWT token");
        }
    }

    private Long extractLongClaim(final Claims claims, final String... claimNames) {
        for (final String claimName : claimNames) {
            final Object value = claims.get(claimName);
            if (value instanceof Number number) {
                return number.longValue();
            }
            if (value instanceof String text && !text.isBlank()) {
                return Long.valueOf(text);
            }
        }
        return null;
    }

    private Key getSigningKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(jwtProvider.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
