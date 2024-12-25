package com.warehouse.configuration;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BeanMainLogger implements CommandLineRunner {

    private final ApplicationContext applicationContext;

    public BeanMainLogger(final ApplicationContext applicationContext) {
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

