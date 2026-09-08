package com.warehouse.logisticsdelivery.api;

import com.warehouse.logisticsdelivery.api.dto.DeliveryAttemptRequestDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryAttemptResultDto;

public interface DeliveryApiService {

    DeliveryAttemptResultDto recordAttempt(final DeliveryAttemptRequestDto attemptRequest);
}
