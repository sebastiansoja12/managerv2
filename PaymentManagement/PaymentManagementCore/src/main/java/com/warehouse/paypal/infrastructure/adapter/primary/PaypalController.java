package com.warehouse.paypal.infrastructure.adapter.primary;


import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.domain.model.PaymentResponse;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaypalController {

    private final PaypalPort paypalPort;

    @PostMapping("/pay")
    public PaymentResponse payment(@RequestBody PaymentRequest request) {
        return paypalPort.payment(request);
    }

    @GetMapping(value = "/pay/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        final PaymentInformation paymentInformation = new PaymentInformation();
        paymentInformation.setPaymentId(paymentId);
        paymentInformation.setPayerId(payerId);

        return paypalPort.update(paymentInformation);
    }

    @GetMapping(value = "/pay/cancel")
    public String cancelPay() {
        return "Payment has been cancelled";
    }

}
