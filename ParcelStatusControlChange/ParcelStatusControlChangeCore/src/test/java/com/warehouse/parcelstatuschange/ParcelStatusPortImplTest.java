package com.warehouse.parcelstatuschange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import com.warehouse.parcelstatuschange.domain.exception.ParcelRequestEmptyException;
import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.exception.ParcelNotFoundException;
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
import com.warehouse.parcelstatuschange.domain.vo.Status;
import com.warehouse.parcelstatuschange.domain.vo.Parcel;
import com.warehouse.parcelstatuschange.domain.vo.StatusRequest;
import com.warehouse.parcelstatuschange.domain.port.primary.ParcelStatusPort;
import com.warehouse.parcelstatuschange.domain.port.secondary.ParcelStatusRepository;
import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.ParcelStatusReadRepository;
import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.entity.ParcelEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = ParcelStatusChangeConfigurationTest.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/shipment.xml")
public class ParcelStatusPortImplTest {

    @Autowired
    private ParcelStatusReadRepository parcelStatusReadRepository;

    @Autowired
    private ParcelStatusRepository parcelStatusRepository;

    @Autowired
    private ParcelStatusPort parcelStatusPort;


    @Test
    void shouldUpdateStatus() {
        // given
        final Parcel parcel = new Parcel(1L, Status.RETURN);
        final StatusRequest request = new StatusRequest(parcel);
        // when
        parcelStatusPort.updateStatus(request);
        // then
        final ParcelEntity parcelEntity = parcelStatusReadRepository.findById(1L).orElseThrow();
		assertEquals(parcelEntity.getStatus(),
				com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.enumeration.Status.RETURN);
    }

    @Test
    void shouldNotUpdateStatusWhenParcelWithGivenIdWasNotFound() {
        // given
        final Long parcelId = 2L;
        final Parcel parcel = new Parcel(parcelId, Status.RETURN);
        final StatusRequest request = new StatusRequest(parcel);
        // when && then
        assertThrowsExactly(ParcelNotFoundException.class,
                () -> parcelStatusPort.updateStatus(request));
    }

    @Test
    void shouldNotUpdateStatusWhenParcelInRequestIsNull() {
        // given
        final StatusRequest request = new StatusRequest(null);
        // when && then
        assertThrowsExactly(ParcelRequestEmptyException.class,
                () -> parcelStatusPort.updateStatus(request));
    }

}
