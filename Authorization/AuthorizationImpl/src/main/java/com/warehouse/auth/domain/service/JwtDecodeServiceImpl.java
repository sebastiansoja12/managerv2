package com.warehouse.auth.domain.service;

import java.security.Key;

import org.springframework.stereotype.Service;

import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.vo.DecodedApiTenant;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.exceptionhandler.exception.RestException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtDecodeServiceImpl implements JwtDecodeService {

    private final JwtProvider jwtProvider;

    public JwtDecodeServiceImpl(final JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public DecodedApiTenant decodeJwt(final String token) {
        try {
            final Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            final Long operatorId = extractLongClaim(claims, "operatorId", "operator_id");
            final UserId userId = new UserId(claims.get("userId", Long.class));
            final String issuer = claims.get("username", String.class);

            return new DecodedApiTenant(userId, OperatorId.of(operatorId), issuer);
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
