package com.warehouse.redirect.domain.port.primary;

import com.warehouse.redirect.domain.vo.RedirectParcelRequest;
import com.warehouse.redirect.domain.model.RedirectParcelResponse;
import com.warehouse.redirect.domain.model.RedirectRequest;
import com.warehouse.redirect.domain.model.RedirectResponse;

public interface RedirectTokenPort {
    RedirectResponse sendRedirectInformation(RedirectRequest request);

    RedirectParcelResponse redirect(RedirectParcelRequest request);
}
