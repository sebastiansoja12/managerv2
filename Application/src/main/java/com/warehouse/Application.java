package com.warehouse;

import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.provider.RefreshTokenProvider;
import com.warehouse.paypal.configuration.PaypalConfigurationProperties;
import com.warehouse.paypal.domain.model.RedirectUrls;
import com.warehouse.paypal.domain.properties.PayeeProperties;
import com.warehouse.positionstack.PositionStackProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.warehouse.*"})
@EnableJpaRepositories(basePackages = {"com.warehouse.*"})
@ConfigurationPropertiesScan(basePackages = {"com.warehouse.*"})
@EnableConfigurationProperties({JwtProvider.class, PositionStackProperties.class, RefreshTokenProvider.class,
		PaypalConfigurationProperties.class, RedirectUrls.class, PayeeProperties.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
