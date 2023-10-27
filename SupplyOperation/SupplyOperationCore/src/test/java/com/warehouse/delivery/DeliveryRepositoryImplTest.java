package com.warehouse.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.delivery.configuration.DeliveryTestConfiguration;
import com.warehouse.delivery.domain.enumeration.DeliveryStatus;
import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.infrastructure.adapter.secondary.DeliveryReadRepository;
import com.warehouse.delivery.infrastructure.adapter.secondary.DeliveryRepositoryImpl;

@ExtendWith({SpringExtension.class})
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
        final Delivery delivery = Delivery.builder()
                .deliveryStatus(DeliveryStatus.DELIVERY)
                .parcelId(1L)
                .supplierCode("abc")
                .depotCode("abc")
                .build();
        // when
        final Delivery savedDelivery = deliveryRepository.saveDelivery(delivery);
        // then
        assertThat(savedDelivery.getId()).isNotNull();
    }
}
