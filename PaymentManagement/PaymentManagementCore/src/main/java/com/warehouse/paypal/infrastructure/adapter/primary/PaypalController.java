package com.warehouse.paypal.infrastructure.adapter.primary;


import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.domain.model.PaymentResponse;
import com.warehouse.paypal.domain.port.secondary.PaymentSecondaryPort;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaypalController {

    private final PaymentSecondaryPort paymentSecondaryPort;

    @PostMapping("/pay")
    public PaymentResponse payment(@RequestBody PaymentRequest request) {
        return paymentSecondaryPort.payment(request);
    }

    @GetMapping(value = "/pay/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        return paymentSecondaryPort.update(paymentId, payerId);
    }

    @GetMapping(value = "/pay/cancel")
    public String cancelPay() {
        return "Payment has been cancelled";
    }

}
