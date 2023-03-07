package com.warehouse.tracker;

import org.springframework.beans.factory.annotation.Value;

public class TrackerConfiguration {

    @Value("${tr.spring.url}")
    public String springUrl;

    @Value("${tr.url}")
    public String guiUrl;
}
