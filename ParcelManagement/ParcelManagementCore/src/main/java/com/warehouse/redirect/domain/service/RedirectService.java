package com.warehouse.redirect.domain.service;

import com.warehouse.redirect.domain.model.RedirectToken;
import com.warehouse.redirect.domain.model.Token;

public interface RedirectService {
    Token saveRedirectToken(RedirectToken redirectToken);
}
