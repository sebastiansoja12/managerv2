package com.warehouse.softwareconfiguration.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Software {

    private String id;
    private String category;
    private String name;
    private String value;
}
