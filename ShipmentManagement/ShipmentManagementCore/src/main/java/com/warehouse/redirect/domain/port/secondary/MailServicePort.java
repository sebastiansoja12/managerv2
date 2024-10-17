package com.warehouse.redirect.domain.port.secondary;

import com.warehouse.redirect.domain.vo.RedirectToken;

public interface MailServicePort {
    void sendRedirectInformation(RedirectToken redirectToken);
}
