package com.warehouse.paypal;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
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
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = PaypalTestConfiguration.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PaypalReadRepositoryTest {

    @Autowired
    private PaypalReadRepository repository;

    @Test
    @DatabaseSetup("/data/paypal.xml")
    void shouldReturnPayment() {
        // given
        final String paymentId = "payment";
        // when
        final PaypalEntity entity = repository.findByPaymentId(paymentId);
        // then
        assertThat(entity).isNull();
    }
}
