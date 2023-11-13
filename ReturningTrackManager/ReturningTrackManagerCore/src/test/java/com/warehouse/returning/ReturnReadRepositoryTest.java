package com.warehouse.returning;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

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
import com.github.springtestdbunit.annotation.DatabaseSetup;
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

    @Test
    @DatabaseSetup("/dataset/returning.xml")
    void shouldFindFirstByParcelId() {
        // given
        final Long parcelId = 1L;
        // when
        final Optional<ReturnEntity> entity = repository.findFirstByParcelId(parcelId);
        // then
        assertTrue(entity.isPresent());
    }

    @Test
    @DatabaseSetup("/dataset/returning.xml")
    void shouldNotFindReturningByParcelId() {
        // given
        final Long parcelId = 2137L;
        // when
        final Optional<ReturnEntity> entity = repository.findFirstByParcelId(parcelId);
        // then
        assertTrue(entity.isEmpty());
    }

    @Test
    @DatabaseSetup("/dataset/returning.xml")
    void shouldFindByReturnId() {
        // given
        final Long returnId = 1L;
        // when
        final Optional<ReturnEntity> entity = repository.findById(returnId);
        // then
        assertTrue(entity.isPresent());
    }

    @Test
    @DatabaseSetup("/dataset/returning.xml")
    void shouldNotFindByReturnId() {
        // given
        final Long returnId = 2137L;
        // when
        final Optional<ReturnEntity> entity = repository.findById(returnId);
        // then
        assertTrue(entity.isEmpty());
    }

    @Test
    @DatabaseSetup("/dataset/returningWithInformation.xml")
    void shouldFindByReturnIdWithInformation() {
        // given
        final Long returnId = 1L;
        // when
        final Optional<ReturnEntity> entity = repository.findById(returnId);
        // then
        assertTrue(entity.isPresent());
        assertTrue(entity.map(ReturnEntity::getReturnInformationEntity).isPresent());
    }



}
