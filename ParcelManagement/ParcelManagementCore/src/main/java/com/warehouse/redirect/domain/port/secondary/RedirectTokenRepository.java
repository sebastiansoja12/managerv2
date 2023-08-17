package com.warehouse.redirect.domain.port.secondary;

import com.warehouse.redirect.domain.model.RedirectToken;
import com.warehouse.redirect.domain.model.Token;

public interface RedirectTokenRepository {
    Token save(RedirectToken redirectToken);
}
