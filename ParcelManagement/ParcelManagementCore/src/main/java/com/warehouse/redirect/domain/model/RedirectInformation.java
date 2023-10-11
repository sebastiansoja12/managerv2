package com.warehouse.redirect.domain.model;

import com.warehouse.redirect.domain.vo.Token;
import lombok.Data;

@Data
public class RedirectInformation {
    String url;
    Token token;
}
