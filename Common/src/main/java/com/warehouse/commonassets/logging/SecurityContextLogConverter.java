package com.warehouse.commonassets.logging;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Resolves log context from the authentication active when the event is logged, so code running inside
 * {@code OperatorContext.runAs} is logged as that operator. Falls back to the MDC value set by request filters.
 */
abstract class SecurityContextLogConverter extends ClassicConverter {

    static final String NOT_AVAILABLE = "N/A";

    @Override
    public String convert(final ILoggingEvent event) {
        return currentToken()
                .map(this::fromToken)
                .orElseGet(() -> fromMdc(event.getMDCPropertyMap()));
    }

    protected abstract String fromToken(UsernameTenantPasswordAuthenticationToken token);

    protected abstract String mdcKey();

    private String fromMdc(final Map<String, String> mdc) {
        return Optional.ofNullable(mdc.get(mdcKey())).orElse(NOT_AVAILABLE);
    }

    private static Optional<UsernameTenantPasswordAuthenticationToken> currentToken() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication instanceof UsernameTenantPasswordAuthenticationToken token
                ? Optional.of(token)
                : Optional.empty();
    }
}
