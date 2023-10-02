package com.warehouse.paypal.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
public class Transactions {
    public Amount amount;
    public String description;
    public String related_resources;
}
