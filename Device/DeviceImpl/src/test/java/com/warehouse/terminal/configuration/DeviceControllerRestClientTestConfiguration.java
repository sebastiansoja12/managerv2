package com.warehouse.terminal.configuration;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceInitializationAutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.warehouse.exceptionhandler.ExceptionHandler;
import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.terminal.domain.port.primary.DevicePairPort;
import com.warehouse.terminal.domain.port.primary.DevicePort;
import com.warehouse.terminal.infrastructure.adapter.primary.DeviceController;
import com.warehouse.terminal.infrastructure.adapter.primary.validation.DeviceRequestValidationService;

@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataJpaRepositoriesAutoConfiguration.class,
        DataSourceInitializationAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class
})
public class DeviceControllerRestClientTestConfiguration {

    @Bean
    DevicePort devicePort() {
        return org.mockito.Mockito.mock(DevicePort.class);
    }

    @Bean
    DevicePairPort devicePairPort() {
        return org.mockito.Mockito.mock(DevicePairPort.class);
    }

    @Bean
    CurrentUserApiService currentUserApiService() {
        return org.mockito.Mockito.mock(CurrentUserApiService.class);
    }

    @Bean
    DeviceRequestValidationService deviceRequestValidationService() {
        return new DeviceRequestValidationService();
    }

    @Bean
    DeviceController deviceController(final DevicePort devicePort,
                                      final DevicePairPort devicePairPort,
                                      final CurrentUserApiService currentUserApiService,
                                      final DeviceRequestValidationService validationService) {
        return new DeviceController(devicePort, devicePairPort, currentUserApiService, validationService);
    }

    @Bean
    ExceptionHandler exceptionHandler() {
        return new ExceptionHandler();
    }
}
