package com.warehouse.paypal;


import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.paypal.configuration.PaypalTestConfiguration;
import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.domain.port.secondary.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = PaypalTestConfiguration.class)
@TestExecutionListeners( {
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class,
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PaypalRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DatabaseSetup("/data/paypal.xml")
    void shouldReturnPayment() {
        // given
        final String paymentId = "payment";
        // when
        final PaymentInformation information = paymentRepository.findByPaymentId(paymentId);
        // then
        assertThat(information).isNull();
    }
}
