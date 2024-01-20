package com.warehouse.softwareconfiguration;


import com.warehouse.softwareconfiguration.domain.port.primary.SoftwareConfigurationPortImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;


@ExtendWith(SpringExtension.class)
@DataMongoTest
@ContextConfiguration(classes = SoftwareTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SoftwareConfigurationPortImplIntegrationTest {

    @Autowired
    private SoftwareConfigurationPortImpl softwareConfigurationPort;


    @Test
    void shouldCreateSoftwareProperty() {
        // given

        // when

        // then
    }
}
