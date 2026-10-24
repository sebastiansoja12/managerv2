package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

public class ActingDepartmentHeader {

    public static final String NAME = "X-Acting-Department";

    public Optional<DepartmentCode> requestedDepartmentCode() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof final ServletRequestAttributes servletRequestAttributes)) {
            return Optional.empty();
        }
        return fromValue(servletRequestAttributes.getRequest().getHeader(NAME));
    }

    public Optional<DepartmentCode> fromValue(final String headerValue) {
        return Optional.ofNullable(headerValue)
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(DepartmentCode::new);
    }
}
