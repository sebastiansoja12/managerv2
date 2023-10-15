package com.warehouse.deliverytoken;


import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.DeliveryTokenAdapter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

// TODO INPL-6151
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeliveryTokenSecondaryAdapterTest.DeliveryTokenSecondaryAdapterConfigurationTest.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeliveryTokenSecondaryAdapterTest {

    @ComponentScan(basePackages = { "com.warehouse.deliverytoken" })
    @EntityScan(basePackages = { "com.warehouse.deliverytoken" })
    @EnableJpaRepositories(basePackages = { "com.warehouse.deliverytoken"})
    static class DeliveryTokenSecondaryAdapterConfigurationTest {

    }

    @Autowired
    private DeliveryTokenAdapter deliveryTokenAdapter;


}
