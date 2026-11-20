package com.warehouse.analytics.application.port.secondary;

import com.warehouse.commonassets.identificator.UserId;

public interface AnalyticsUserServicePort {

    UserId currentUserId();
}
