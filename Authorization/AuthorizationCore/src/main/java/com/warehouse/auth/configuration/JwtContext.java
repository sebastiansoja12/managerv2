package com.warehouse.auth.configuration;

import org.springframework.stereotype.Component;

@Component
public class JwtContext {
    private static final ThreadLocal<String> jwtHolder = new ThreadLocal<>();

    public static void setToken(String token) {
        jwtHolder.set(token);
    }

    public static String getToken() {
        return jwtHolder.get();
    }

    public static void clear() {
        jwtHolder.remove();
    }
}

