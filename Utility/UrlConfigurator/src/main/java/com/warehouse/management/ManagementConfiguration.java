package com.warehouse.management;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;



@PropertySource("classpath:application.properties")
public class ManagementConfiguration {

    @Value("${spring.url}")
    public String springUrl;

    @Value("${mg.url}")
    public String guiUrl;

}
