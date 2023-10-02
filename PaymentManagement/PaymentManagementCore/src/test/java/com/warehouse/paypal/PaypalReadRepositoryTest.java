package com.warehouse.paypal;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.paypal.configuration.PaypalTestConfiguration;
import com.warehouse.paypal.infrastructure.adapter.secondary.PaypalReadRepository;
import com.warehouse.paypal.infrastructure.adapter.secondary.entity.PaypalEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = PaypalTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/data/paypal.xml")
public class PaypalReadRepositoryTest {

    @Autowired
    private PaypalReadRepository readRepository;

    @Test
    void shouldReturnPaymentByPaymentId() {
        // given
        final String paymentId = "paymentId";
        // when
        final Optional<PaypalEntity> paypal = readRepository.findByPaymentId(paymentId);
        // then
        assertTrue(paypal.isPresent());
    }

    @Test
    void shouldNotReturnPaymentByPaymentId() {
        // given
        final String paymentId = "fakePayment";
        // when
        final Optional<PaypalEntity> paypal = readRepository.findByPaymentId(paymentId);
        // then
        assertTrue(paypal.isEmpty());
    }
}
