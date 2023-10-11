package com.warehouse.routetracker;


import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.routetracker.configuration.RouteTrackerTestConfiguration;
import com.warehouse.routetracker.domain.enumeration.Status;
import com.warehouse.routetracker.domain.port.secondary.ParcelStatusUpdateRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.ParcelReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.exception.ParcelNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RouteTrackerTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/db.xml")
public class ParcelStatusUpdateRepositoryImplTest {

    @Autowired
    private ParcelStatusUpdateRepository parcelStatusUpdateRepository;
    
    @Autowired
    private ParcelReadRepository repository;

    @Test
    void shouldUpdateParcelStatus() {
        // given
        final Long parcelId = 100L;
        final Status status = Status.DELIVERY;
        // when
        parcelStatusUpdateRepository.updateStatus(parcelId, status);
        // then
        final Optional<ParcelEntity> updatedEntity = repository.findById(parcelId);
		final com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status newStatus = updatedEntity
				.map(ParcelEntity::getStatus).stream()
                .findAny()
				.orElse(com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status.CREATED);
		assertEquals(expected(Status.DELIVERY.name()), newStatus.name());
    }

    @Test
    void shouldNotUpdateParcelStatus() {
        // given
        final Long parcelId = 1L;
        final Status status = Status.DELIVERY;
        final String errorMessage = "Parcel to update with id '1' was not found";
        // when
        final Executable executable = () -> parcelStatusUpdateRepository.updateStatus(parcelId, status);
        // then
        final ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, executable);
        assertEquals(expected(errorMessage), exception.getMessage());
    }
    
    private <T> T expected(T t) {
        return t;
    }
}
