package com.warehouse.redirect.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.redirect.domain.vo.RedirectToken;
import com.warehouse.redirect.domain.vo.Token;

public interface RedirectTokenRepository {
    Token save(RedirectToken redirectToken);

    RedirectToken findByShipmentId(final ShipmentId shipmentId);

    void update(RedirectToken redirectToken);
}
