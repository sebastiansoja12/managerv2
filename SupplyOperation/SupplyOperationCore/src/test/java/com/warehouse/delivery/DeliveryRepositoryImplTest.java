package com.warehouse.delivery;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.delivery.configuration.DeliveryTestConfiguration;
import com.warehouse.delivery.infrastructure.adapter.secondary.DeliveryReadRepository;
import com.warehouse.delivery.infrastructure.adapter.secondary.DeliveryRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DataJpaTest
@ContextConfiguration(classes = DeliveryTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeliveryRepositoryImplTest {


    @Autowired
    private DeliveryReadRepository repository;

    private DeliveryRepositoryImpl deliveryRepository;

    @BeforeEach
    void setup() {
        deliveryRepository = new DeliveryRepositoryImpl(repository);
    }

    @Test
    void shouldSaveDelivery() {
        // given

        // when

        // then
    }
}
