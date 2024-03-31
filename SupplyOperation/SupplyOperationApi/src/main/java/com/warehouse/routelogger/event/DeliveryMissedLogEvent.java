package com.warehouse.routelogger.event;

import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.dto.DeliveryMissedRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class DeliveryMissedLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private DeliveryMissedRequestDto deliveryMissedRequest;

    @Builder
    public DeliveryMissedLogEvent(@NonNull DeliveryMissedRequestDto deliveryMissedRequest) {
        super();
        this.deliveryMissedRequest = deliveryMissedRequest;
    }

}
