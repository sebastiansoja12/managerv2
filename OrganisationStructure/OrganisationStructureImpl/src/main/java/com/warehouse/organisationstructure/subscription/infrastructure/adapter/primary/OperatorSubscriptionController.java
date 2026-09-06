package com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.subscription.application.port.primary.OperatorSubscriptionPort;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.api.AssignSubscriptionApiRequest;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.api.OperatorSubscriptionApiResponse;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.mapper.OperatorSubscriptionApiMapper;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operators/{operatorId}/subscription")
@AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
public class OperatorSubscriptionController {

    private final OperatorSubscriptionPort operatorSubscriptionPort;

    public OperatorSubscriptionController(final OperatorSubscriptionPort operatorSubscriptionPort) {
        this.operatorSubscriptionPort = operatorSubscriptionPort;
    }

    @GetMapping
    public ResponseEntity<OperatorSubscriptionApiResponse> find(@PathVariable final OperatorId operatorId) {
        return operatorSubscriptionPort.find(operatorId)
                .map(OperatorSubscriptionApiMapper::toResponse)
                .map(response -> ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(response))
                .orElseGet(() -> ResponseEntity.noContent().cacheControl(CacheControl.noStore()).build());
    }

    @PutMapping
    public ResponseEntity<OperatorSubscriptionApiResponse> assign(@PathVariable final OperatorId operatorId,
                                                                  @Valid @RequestBody final AssignSubscriptionApiRequest request) {
        return ResponseEntity.ok(OperatorSubscriptionApiMapper.toResponse(operatorSubscriptionPort.assign(
                OperatorSubscriptionApiMapper.toCommand(operatorId, request))));
    }
}
