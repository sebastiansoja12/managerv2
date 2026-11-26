package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.FakeGatewayOutcome;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.PaymentApiResponse;
import com.warehouse.billing.payment.infrastructure.adapter.primary.mapper.PaymentWebMapper;
import com.warehouse.commonassets.identificator.PaymentId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billing/dev/fake-gateway")
@ConditionalOnProperty(name = "billing.payment-gateway", havingValue = "fake", matchIfMissing = true)
public class FakeGatewayController {

    private final PaymentPort paymentPort;

    private final PaymentWebMapper paymentWebMapper;

    public FakeGatewayController(final PaymentPort paymentPort, final PaymentWebMapper paymentWebMapper) {
        this.paymentPort = paymentPort;
        this.paymentWebMapper = paymentWebMapper;
    }

    @RequestMapping(value = "/{paymentId}/{outcome}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<PaymentApiResponse> complete(@PathVariable final PaymentId paymentId,
                                                       @PathVariable final FakeGatewayOutcome outcome) {
        if (FakeGatewayOutcome.COMPLETED.equals(outcome)) {
            return ResponseEntity.ok(this.paymentWebMapper.toResponse(this.paymentPort.confirmOnlinePayment(
                    this.paymentWebMapper.toFakeConfirmation(paymentId))));
        }
        return ResponseEntity.of(this.paymentPort.findById(paymentId).map(this.paymentWebMapper::toResponse));
    }
}
