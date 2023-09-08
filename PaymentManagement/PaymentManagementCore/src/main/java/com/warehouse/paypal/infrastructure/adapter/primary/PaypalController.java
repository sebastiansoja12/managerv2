package com.warehouse.paypal.infrastructure.adapter.primary;


import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.domain.model.PaymentResponse;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.paypal.infrastructure.adapter.primary.dto.PaymentRequestDto;
import com.warehouse.paypal.infrastructure.adapter.primary.mapper.PaymentRequestMapper;
import com.warehouse.paypal.infrastructure.adapter.primary.mapper.PaymentResponseMapper;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaypalController {

    private final PaypalPort paypalPort;

    private final PaymentRequestMapper requestMapper = Mappers.getMapper(PaymentRequestMapper.class);

    private final PaymentResponseMapper responseMapper = Mappers.getMapper(PaymentResponseMapper.class);

    @PostMapping
    public ResponseEntity<?> payment(@RequestBody PaymentRequestDto paymentRequest) {
        final PaymentRequest request = requestMapper.map(paymentRequest);
        final PaymentResponse response = paypalPort.payment(request);
        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.OK);
    }

    @GetMapping
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        final PaymentInformation paymentInformation = PaymentInformation.builder().build();
        paymentInformation.setPaymentId(paymentId);
        paymentInformation.setPayerId(payerId);

        return paypalPort.update(paymentInformation);
    }

    @GetMapping(value = "/pay/cancel")
    public String cancelPay() {
        return "Payment has been cancelled";
    }

}
