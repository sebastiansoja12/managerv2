package com.warehouse.routetracker.configuration;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BeanLogger implements CommandLineRunner {

    private final ApplicationContext applicationContext;

    public BeanLogger(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(final String... args) {
        log.info("Application: {} starting", applicationContext.getApplicationName());
        final String[] beanNames = applicationContext.getBeanDefinitionNames();
        Arrays.stream(beanNames).forEach(beanName -> log.info("Bean initialized: {}", beanName));
        log.warn("Successfully initialized {} beans", applicationContext.getBeanDefinitionCount());
    }
}

