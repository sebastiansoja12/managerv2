package com.warehouse.redirect.domain.vo;

import lombok.Value;

@Value
public class RedirectInformation {
    String url;
    Token token;
}
