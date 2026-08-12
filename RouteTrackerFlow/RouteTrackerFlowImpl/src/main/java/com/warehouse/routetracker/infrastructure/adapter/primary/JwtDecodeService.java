package com.warehouse.routetracker.infrastructure.adapter.primary;

import java.security.Key;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.routetracker.configuration.JwtProperties;
import com.warehouse.routetracker.domain.vo.UserContext;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtDecodeService {

    private static final String ACCESS_TOKEN_TYPE = "access";

    private final JwtParser jwtParser;

    public JwtDecodeService(final JwtProperties jwtProperties) {
        final byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
        final Key signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .requireIssuer(jwtProperties.issuer())
                .requireAudience(jwtProperties.audience())
                .require("tokenType", ACCESS_TOKEN_TYPE)
                .build();
    }

    public UserContext decode(final String token) {
        final Claims claims = this.jwtParser.parseClaimsJws(token).getBody();
        final Long userId = extractRequiredLongClaim(claims, "userId");
        final Long operatorId = extractRequiredLongClaim(claims, "operatorId", "operator_id");
        final Long departmentId = extractRequiredLongClaim(claims, "departmentId", "department_id");
        return new UserContext(new UserId(userId), OperatorId.of(operatorId), new DepartmentId(departmentId));
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
