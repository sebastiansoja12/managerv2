package com.warehouse.warehouse;

import org.springframework.beans.factory.annotation.Value;

public class WarehouseConfiguration {

    @Value("${wr.spring.url}")
    public String springUrl;

    @Value("${wr.url}")
    public String guiUrl;
}
