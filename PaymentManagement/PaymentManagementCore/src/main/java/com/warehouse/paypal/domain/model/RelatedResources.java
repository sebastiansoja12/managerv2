package com.warehouse.paypal.domain.model;

import com.paypal.api.payments.*;
import lombok.Data;

@Data
public class RelatedResources {
    private Sale sale;
    private Authorization authorization;
    private Capture capture;
    private Refund refund;
    private Order order;
}
