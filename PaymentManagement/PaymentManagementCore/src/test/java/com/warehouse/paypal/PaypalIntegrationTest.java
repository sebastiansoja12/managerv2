package com.warehouse.paypal;


import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestClient;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.paypal.configuration.PaypalTestConfiguration;
import com.warehouse.paypal.infrastructure.adapter.primary.dto.ParcelId;
import com.warehouse.paypal.infrastructure.adapter.primary.dto.PaymentRequestDto;
import com.warehouse.paypal.infrastructure.adapter.primary.dto.PaymentResponseDto;
import com.warehouse.paypal.infrastructure.adapter.primary.dto.PriceDto;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = PaypalTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Disabled
// TODO create in the future integration tests with restClient
public class PaypalIntegrationTest {


    @Autowired
    private RestClient client;

    @Test
    void shouldCreatePayment() {
        // given
        final PaymentRequestDto request = new PaymentRequestDto();
        request.setParcelId(new ParcelId(1L));
        request.setPrice(new PriceDto(new BigDecimal(30)));

        // when
        final PaymentResponseDto response = client.post()
                .uri("/v2/api/payments")
                .retrieve()
                .body(PaymentResponseDto.class);
        // then
        assertThat(response.getPaymentMethod()).isEqualTo("Paypal");
    }
}
