package com.warehouse.configuration;

import org.slf4j.MDC;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

import com.warehouse.auth.domain.event.UserChangedEvent;

public class TenantAwareEventMulticaster extends SimpleApplicationEventMulticaster {

    @Override
    public void multicastEvent(final ApplicationEvent event) {
        multicastEvent(event, ResolvableType.forInstance(event));
    }

    @Override
    public void multicastEvent(final ApplicationEvent event, @Nullable final ResolvableType eventType) {
        final Object actual = unwrap(event);
        final String tenant = extractTenantCode(actual);

        try (MDC.MDCCloseable c = tenant != null ? MDC.putCloseable("tenant", tenant) : null) {
            super.multicastEvent(event, eventType);
        }
    }

    private Object unwrap(final Object event) {
        if (event instanceof PayloadApplicationEvent<?> pae) {
            return pae.getPayload();
        }
        return event;
    }

    private String extractTenantCode(final Object event) {
        if (event instanceof UserChangedEvent uce && uce.getSnapshot() != null) {
            return uce.getSnapshot().departmentCode();
        }
        return null;
    }
}

