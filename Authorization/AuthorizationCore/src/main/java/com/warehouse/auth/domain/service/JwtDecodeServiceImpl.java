package com.warehouse.auth.domain.service;

import java.security.Key;

import org.springframework.stereotype.Service;

import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.vo.DecodedApiTenant;
import com.warehouse.commonassets.identificator.DepartmentCode;
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

            final UserId userId = new UserId(claims.get("userId", Long.class));
            final DepartmentCode departmentCode = new DepartmentCode(claims.get("tenant", String.class));
            final String issuer = claims.get("username", String.class);

            return new DecodedApiTenant(userId, departmentCode, issuer);
        } catch (SignatureException | IllegalArgumentException e) {
            throw new RestException(401, "Invalid or expired JWT token");
        }
    }

    private Key getSigningKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(jwtProvider.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
