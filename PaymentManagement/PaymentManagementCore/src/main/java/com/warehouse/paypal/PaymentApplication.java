package com.warehouse.paypal;

import com.warehouse.paypal.domain.model.RedirectUrls;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.warehouse.paypal.domain.properties.PaypalConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.warehouse.paypal"})
@EnableJpaRepositories(basePackages = {"com.warehouse.paypal"})
@ConfigurationPropertiesScan(basePackages = {"com.warehouse.paypal"})
@EnableConfigurationProperties({PaypalConfigurationProperties.class, RedirectUrls.class})
public class PaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
}
