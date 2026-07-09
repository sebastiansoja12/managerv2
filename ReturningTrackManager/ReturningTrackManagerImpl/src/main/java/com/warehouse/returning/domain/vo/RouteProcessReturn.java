package com.warehouse.returning.domain.vo;

import lombok.Value;

import java.util.List;

@Value
public class RouteProcessReturn {
    List<ProcessReturn> processReturn;
    String depotCode;
    String username;
}
