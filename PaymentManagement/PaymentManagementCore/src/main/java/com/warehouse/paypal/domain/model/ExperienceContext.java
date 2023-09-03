package com.warehouse.paypal.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExperienceContext {
    private String payment_method_preference;
    private String brand_name;
    private String locale;
    private String landing_page;
    private String shipping_preference;
    private String user_action;
    private String return_url;
    private String cancel_url;
}
