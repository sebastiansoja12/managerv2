package com.warehouse.inparcel;

import org.springframework.beans.factory.annotation.Value;

public class InparcelConfiguration {

    @Value("${in.spring.url}")
    public String springUrl;

    @Value("${in.url}")
    public String guiUrl;

}
