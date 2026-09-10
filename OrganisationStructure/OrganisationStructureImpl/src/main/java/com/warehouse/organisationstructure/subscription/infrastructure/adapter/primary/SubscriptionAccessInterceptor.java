package com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.organisationstructure.subscription.application.port.primary.OperatorSubscriptionPort;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.api.CurrentSubscriptionApiResponse;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.mapper.OperatorSubscriptionApiMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class SubscriptionAccessInterceptor implements HandlerInterceptor {

    public static final String INACTIVE_SUBSCRIPTION_MESSAGE =
            "The operator subscription is not active. Contact support to renew the plan.";

    private static final Set<String> ALWAYS_AVAILABLE_AUTH_PATHS = Set.of(
            "/auth/login",
            "/auth/logout",
            "/auth/refresh",
            "/auth/csrf",
            "/auth/signup",
            "/auth/refresh-tokens",
            "/auth/me",
            "/auth/me/impersonation",
            "/auth/me/language",
            "/auth/me/password"
    );

    private static final List<String> ALWAYS_AVAILABLE_PATHS = List.of(
            "/subscription",
            "/maintenance",
            "/platform"
    );

    private final OperatorSubscriptionPort operatorSubscriptionPort;

    private final ObjectMapper objectMapper;

    public SubscriptionAccessInterceptor(final OperatorSubscriptionPort operatorSubscriptionPort,
                                         final ObjectMapper objectMapper) {
        this.operatorSubscriptionPort = operatorSubscriptionPort;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(@NonNull final HttpServletRequest request,
                             @NonNull final HttpServletResponse response,
                             @NonNull final Object handler) throws IOException {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        final String path = request.getRequestURI().substring(request.getContextPath().length());
        if (isAlwaysAvailable(path)) {
            return true;
        }
        final CurrentSubscriptionApiResponse subscription;
        try {
            if (operatorSubscriptionPort.isCurrentAccessAllowed()) {
                return true;
            }
            subscription = OperatorSubscriptionApiMapper.toCurrentResponse(operatorSubscriptionPort.findCurrent(),
                    false);
        } catch (RuntimeException exception) {
            log.error("Could not resolve operator subscription for {}", path, exception);
            return true;
        }
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("subscriptionInactive", true);
        body.put("status", subscription.status());
        body.put("plan", subscription.plan());
        body.put("startDate", subscription.startDate());
        body.put("endDate", subscription.endDate());
        body.put("message", INACTIVE_SUBSCRIPTION_MESSAGE);
        response.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getOutputStream(), body);
        return false;
    }

    private boolean isAlwaysAvailable(final String path) {
        if (ALWAYS_AVAILABLE_AUTH_PATHS.contains(path)) {
            return true;
        }
        return ALWAYS_AVAILABLE_PATHS.stream()
                .anyMatch(available -> path.equals(available) || path.startsWith(available + "/"));
    }
}
