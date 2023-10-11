package com.warehouse.auth.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.provider.RefreshTokenProvider;

@ComponentScan(basePackages = { "com.warehouse.auth"})
@EntityScan(basePackages = { "com.warehouse.auth"})
@EnableJpaRepositories(basePackages = { "com.warehouse.auth"})
@ConfigurationPropertiesScan(basePackages = {"com.warehouse.auth"})
@EnableConfigurationProperties({JwtProvider.class, RefreshTokenProvider.class})
public class AuthTestConfiguration {

}
