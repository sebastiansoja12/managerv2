package com.warehouse.paypal.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Details {
    private String shipping;
    private String subtotal;
    private String tax;
    private String fee;
    private String handlingFee;
    private String giftWrap;
    private String insurance;
    private String shippingDiscount;
}
