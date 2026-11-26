package com.warehouse.billing.customer.infrastructure.adapter.primary;

import com.warehouse.billing.customer.application.port.primary.BillingCustomerPort;
import com.warehouse.billing.customer.domain.exception.BillingCustomerException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class ExternalBillingCustomerInterceptor implements HandlerInterceptor {

    private final BillingCustomerPort billingCustomerPort;

    public ExternalBillingCustomerInterceptor(final BillingCustomerPort billingCustomerPort) {
        this.billingCustomerPort = billingCustomerPort;
    }

    @Override
    public boolean preHandle(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response,
                             @NonNull final Object handler) {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        try {
            this.billingCustomerPort.currentActive();
            return true;
        } catch (final BillingCustomerException exception) {
            log.warn("External request rejected because the business customer is not active");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
    }
}
