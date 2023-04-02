package com.warehouse.shipment;

import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentReadRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentRepositoryImpl;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ParcelMapper;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class ShipmentRepositoryTest {

    @Mock
    private ShipmentReadRepository readRepository;

    @Mock
    private ParcelMapper parcelMapper;

    private ShipmentRepository shipmentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shipmentRepository = new ShipmentRepositoryImpl(readRepository, parcelMapper);
    }

    @Test
    void shouldSaveParcel() {
        // given
        final Parcel parcel = new Parcel();
        parcel.setId(1L);

        final ParcelEntity entity = new ParcelEntity();
        // map to entity
        Mockito.when(parcelMapper.map(parcel)).thenReturn(entity);
        // when
        shipmentRepository.save(parcel);

        // then
        Mockito.verify(readRepository).save(entity);
    }

    @Test
    void shouldDeleteParcel() {
        // given
        final Long parcelId = 1L;

        // when
        shipmentRepository.delete(parcelId);

        // then
        Mockito.verify(readRepository).deleteById(parcelId);
    }

    @Test
    void shouldReturnParcelById() {
        // given
        final Long parcelId = 1L;
        final ParcelEntity entity = new ParcelEntity();
        final Parcel parcel = new Parcel();
        Mockito.when(readRepository.findParcelEntityById(parcelId)).thenReturn(Optional.of(entity));
        Mockito.when(parcelMapper.map(entity)).thenReturn(parcel);
        // when
        final Parcel result = shipmentRepository.loadParcelById(parcelId);

        // then
        Assertions.assertEquals(parcel, result);
    }

    @Test
    void shouldNotFindParcelAndThrowException() {
        // given
        final Long parcelId = 1L;
        Mockito.when(readRepository.findParcelEntityById(parcelId)).thenReturn(Optional.empty());

        // when && then
        Assertions.assertThrows(ParcelNotFoundException.class, () -> {
            shipmentRepository.loadParcelById(parcelId);
        });
    }

    @Test
    void shouldUpdate() {
        // given
        final Parcel parcelUpdate = new Parcel();
        final ParcelEntity entity = new ParcelEntity();
        final UpdateParcelResponse updateParcelResponse = new UpdateParcelResponse();

        Mockito.when(parcelMapper.mapForUpdate(parcelUpdate)).thenReturn(entity);
        Mockito.when(readRepository.save(entity)).thenReturn(entity);
        Mockito.when(parcelMapper.mapToUpdateParcelResponse(entity)).thenReturn(updateParcelResponse);
        // when
        final UpdateParcelResponse result = shipmentRepository.update(parcelUpdate);

        // then
        Assertions.assertEquals(updateParcelResponse, result);
    }
}
