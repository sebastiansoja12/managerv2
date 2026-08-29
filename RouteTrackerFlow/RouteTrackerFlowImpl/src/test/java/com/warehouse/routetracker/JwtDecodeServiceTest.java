package com.warehouse.routetracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.warehouse.routetracker.configuration.JwtProperties;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.domain.vo.UserContext;
import com.warehouse.routetracker.infrastructure.adapter.primary.JwtDecodeService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

class JwtDecodeServiceTest {

    private static final String SECRET_KEY =
            "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final String ISSUER = "manager-v2";
    private static final String AUDIENCE = "manager-v2-gui";

    @Test
    void shouldDecodeUserOperatorAndDepartmentFromAccessToken() {
        final JwtDecodeService jwtDecodeService = new JwtDecodeService(
                new JwtProperties(SECRET_KEY, ISSUER, AUDIENCE)
        );
        final String token = createAccessToken(42L, 7L, 10L);

        final UserContext userContext = jwtDecodeService.decode(token);

        assertThat(userContext.userId()).isEqualTo(new UserId(42L));
        assertThat(userContext.operatorId()).isEqualTo(new OperatorId(7L));
        assertThat(userContext.departmentId()).isEqualTo(new DepartmentId(10L));
    }

    private String createAccessToken(final Long userId, final Long operatorId, final Long departmentId) {
        final byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        final Key signingKey = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder()
                .setSubject("user")
                .setIssuer(ISSUER)
                .setAudience(AUDIENCE)
                .claim("tokenType", "access")
                .claim("userId", userId)
                .claim("operatorId", operatorId)
                .claim("departmentId", departmentId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(signingKey, SignatureAlgorithm.forSigningKey(signingKey))
                .compact();
    }
}
