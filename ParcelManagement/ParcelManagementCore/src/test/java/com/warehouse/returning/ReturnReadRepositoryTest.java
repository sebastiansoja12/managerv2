package com.warehouse.returning;


import static org.assertj.core.api.Assertions.assertThat;

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
import com.warehouse.returning.configuration.ReturningTestConfiguration;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturnReadRepository;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.enumeration.ReturnStatus;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = ReturningTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReturnReadRepositoryTest {

    @Autowired
    private ReturnReadRepository repository;


    @Test
    void shouldSaveReturnEntity() {
        // given
        final ReturnEntity returnEntity = new ReturnEntity();
        returnEntity.setReturnStatus(ReturnStatus.PROCESSING);
        returnEntity.setReturnToken("returnToken");
        returnEntity.setUsername("s-soja");
        returnEntity.setParcelId(1L);
        returnEntity.setDepotCode("KT1");
        returnEntity.setSupplierCode("abc");
        // when
        final ReturnEntity savedEntity = repository.save(returnEntity);
        // then
        assertThat(savedEntity.getId()).isGreaterThan(0);
    }

}
