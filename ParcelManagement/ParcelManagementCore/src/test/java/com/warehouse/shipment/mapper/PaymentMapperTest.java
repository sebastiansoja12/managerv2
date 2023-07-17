package com.warehouse.shipment.mapper;

import com.warehouse.shipment.domain.vo.PaymentRequest;
import com.warehouse.shipment.domain.vo.PaymentResponse;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.PaymentMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.PaymentMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentMapperTest {

    private PaymentMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new PaymentMapperImpl();
    }

    @Test
    void shouldMapFromModelPaymentRequestToMailPaymentRequest() {
        // given
        final PaymentRequest request = new PaymentRequest(1L, 20);
        // when
        final com.warehouse.paypal.domain.model.PaymentRequest paymentRequest = mapper.map(request);
        // then
        assertThat(paymentRequest.getParcelId()).isEqualTo(1L);
    }

    @Test
    void shouldMapFromModelPaymentResponseToMailPaymentResponse() {
        // given
        final PaymentResponse response = new PaymentResponse();
        response.setPaymentUrl("test.pl");
        // when
        final com.warehouse.paypal.domain.model.PaymentResponse paymentResponse = mapper.map(response);
        // then
        assertThat(paymentResponse.getLink().getPaymentUrl()).isEqualTo("test.pl");
    }
}
