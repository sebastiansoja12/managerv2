package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.exception.WarehouseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parser;
import static java.util.Date.from;

public class JwtProviderImpl implements JwtProvider {

    private KeyStore keyStore;

    @Value("${token.lifetime}")
    private Long jwtExpirationInSeconds;


    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            final InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new WarehouseException("Exception occurred while loading keystore");
        }


    }

    @Override
    public String generateToken(Authentication authentication) {
        final User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusSeconds(jwtExpirationInSeconds)))
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        parser().setSigningKey(getPublickey()).parseClaimsJws(token);
        return true;
    }

    @Override
    public String getUsernameFromJwt(String jwt) {
        final Claims claims = parser()
                .setSigningKey(getPublickey())
                .parseClaimsJws(jwt)
                .getBody();

        return claims.getSubject();
    }

    @Override
    public String generateTokenWithUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(java.sql.Date.from(Instant.now().plusSeconds(jwtExpirationInSeconds)))
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new WarehouseException("Exception occured while retrieving public key from keystore");
        }
    }

    private PublicKey getPublickey() {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new WarehouseException("Exception occured while " +
                    "retrieving public key from keystore", e);
        }
    }

}
