package com.warehouse.auth.infrastructure.adapter.secondary.filter;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public final class PlatformOwnerPaths {

    private static final List<String> PATHS = List.of("/platform", "/operators", "/admin/async-jobs");

    private PlatformOwnerPaths() {
    }

    public static boolean matches(final HttpServletRequest request) {
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        return PATHS.stream().anyMatch(platformPath -> path.equals(platformPath) || path.startsWith(platformPath + "/"));
    }
}
