package com.warehouse.billing.customer.configuration;

import com.warehouse.billing.customer.application.port.primary.BillingCustomerPort;
import com.warehouse.billing.customer.infrastructure.adapter.primary.ExternalBillingCustomerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BillingCustomerWebConfiguration implements WebMvcConfigurer {

    private final BillingCustomerPort billingCustomerPort;

    public BillingCustomerWebConfiguration(final BillingCustomerPort billingCustomerPort) {
        this.billingCustomerPort = billingCustomerPort;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new ExternalBillingCustomerInterceptor(this.billingCustomerPort))
                .addPathPatterns("/external", "/external/**");
    }
}
