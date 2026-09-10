package com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.organisationstructure.subscription.application.port.primary.OperatorSubscriptionPort;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.api.CurrentSubscriptionApiResponse;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.mapper.OperatorSubscriptionApiMapper;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscription")
@AccessUserControl
public class CurrentSubscriptionController {

    private final OperatorSubscriptionPort operatorSubscriptionPort;

    public CurrentSubscriptionController(final OperatorSubscriptionPort operatorSubscriptionPort) {
        this.operatorSubscriptionPort = operatorSubscriptionPort;
    }

    @GetMapping
    public ResponseEntity<CurrentSubscriptionApiResponse> current() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(OperatorSubscriptionApiMapper.toCurrentResponse(operatorSubscriptionPort.findCurrent(),
                        operatorSubscriptionPort.isCurrentAccessAllowed()));
    }
}
