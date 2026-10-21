package com.warehouse.pallet.infrastructure.adapter.primary;

import com.warehouse.pallet.configuration.JwtProperties;
import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.configuration.identificator.OperatorId;
import com.warehouse.pallet.domain.vo.UserContext;
import com.warehouse.pallet.domain.vo.UserId;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.WeakKeyException;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtDecodeService {

    private static final String ACCESS_TOKEN_TYPE = "access";

    private final JwtParser jwtParser;

    public JwtDecodeService(final JwtProperties jwtProperties) {
        final Key signingKey = signingKey(jwtProperties.secretKey());
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .requireIssuer(jwtProperties.issuer())
                .requireAudience(jwtProperties.audience())
                .require("tokenType", ACCESS_TOKEN_TYPE)
                .build();
    }

    private static Key signingKey(final String secretKey) {
        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        } catch (final DecodingException | WeakKeyException exception) {
            throw new IllegalStateException(
                    "jwt.secret-key must be the same Base64 encoded key as used by the manager application", exception);
        }
    }

    public UserContext decode(final String token) {
        final Claims claims = this.jwtParser.parseClaimsJws(token).getBody();
        final Long userId = extractRequiredLongClaim(claims, "userId");
        final Long operatorId = extractRequiredLongClaim(claims, "operatorId", "operator_id");
        final Long departmentId = extractRequiredLongClaim(claims, "departmentId", "department_id");
        return new UserContext(new UserId(userId, claims.getSubject()), new OperatorId(operatorId),
                new DepartmentId(departmentId));
    }

    private Long extractRequiredLongClaim(final Claims claims, final String... claimNames) {
        for (final String claimName : claimNames) {
            final Object value = claims.get(claimName);
            if (value instanceof Number number) {
                return number.longValue();
            }
            if (value instanceof String text && !text.isBlank()) {
                return Long.valueOf(text);
            }
        }
        throw new IllegalArgumentException("Required JWT claim is missing: " + String.join(" or ", claimNames));
    }
}
