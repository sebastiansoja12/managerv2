package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.deliveryreturn.domain.model.ReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.vo.ReturnTokenResponse;

public interface ReturnTokenServicePort {
    ReturnTokenResponse sign(final ReturnTokenRequest returnTokenRequest);
}
