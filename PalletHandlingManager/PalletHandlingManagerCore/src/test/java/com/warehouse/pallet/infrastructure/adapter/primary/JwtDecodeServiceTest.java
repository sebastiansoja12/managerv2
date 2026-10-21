package com.warehouse.pallet.infrastructure.adapter.primary;

import com.warehouse.pallet.configuration.JwtProperties;
import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.configuration.identificator.OperatorId;
import com.warehouse.pallet.domain.vo.UserContext;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtDecodeServiceTest {

    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final JwtProperties PROPERTIES = new JwtProperties(SECRET, "manager-v2", "manager-v2-gui");

    private final JwtDecodeService jwtDecodeService = new JwtDecodeService(PROPERTIES);

    @Test
    void shouldExtractUserOperatorAndDepartment() {
        final UserContext userContext = this.jwtDecodeService.decode(token(Map.of(
                "userId", 5, "operatorId", 7, "departmentId", "11", "tokenType", "access")));

        assertThat(userContext.userId().value()).isEqualTo(5L);
        assertThat(userContext.operatorId()).isEqualTo(new OperatorId(7L));
        assertThat(userContext.departmentId()).isEqualTo(new DepartmentId(11L));
    }

    @Test
    void shouldRejectTokenWithoutOperator() {
        final String token = token(Map.of("userId", 5, "departmentId", 11, "tokenType", "access"));

        assertThatThrownBy(() -> this.jwtDecodeService.decode(token)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectRefreshToken() {
        final String token = token(Map.of("userId", 5, "operatorId", 7, "departmentId", 11, "tokenType", "refresh"));

        assertThatThrownBy(() -> this.jwtDecodeService.decode(token)).isInstanceOf(JwtException.class);
    }

    @Test
    void shouldFailFastWhenSecretIsNotResolved() {
        assertThatThrownBy(() -> new JwtProperties("${JWT_SECRET_KEY}", "manager-v2", "manager-v2-gui"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("JWT_SECRET_KEY");
    }

    @Test
    void shouldReportInvalidSecretEncoding() {
        assertThatThrownBy(() -> new JwtDecodeService(new JwtProperties("not_base64_secret", "manager-v2", "manager-v2-gui")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Base64");
    }

    private String token(final Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject("warehouse.user")
                .setIssuer("manager-v2")
                .setAudience("manager-v2-gui")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET)), SignatureAlgorithm.HS256)
                .compact();
    }
}
