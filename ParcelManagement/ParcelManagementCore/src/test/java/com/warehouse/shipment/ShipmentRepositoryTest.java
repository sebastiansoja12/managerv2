package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.ParcelUpdate;
import com.warehouse.shipment.domain.model.ShipmentParcel;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentReadRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentRepositoryImpl;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ParcelMapper;

@ExtendWith(MockitoExtension.class)
public class ShipmentRepositoryTest {

    @Mock
    private ShipmentReadRepository readRepository;

    @Mock
    private ParcelMapper parcelMapper;

    private ShipmentRepository shipmentRepository;

    @BeforeEach
    void setUp() {
        shipmentRepository = new ShipmentRepositoryImpl(readRepository, parcelMapper);
    }

    @Test
    void shouldSaveParcel() {
        // given
        final ShipmentParcel parcel = new ShipmentParcel();
        parcel.setStatus(Status.CREATED);

        final ParcelEntity entity = new ParcelEntity();

        when(parcelMapper.map(parcel)).thenReturn(entity);

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
        when(readRepository.findParcelEntityById(parcelId)).thenReturn(Optional.of(entity));
        when(parcelMapper.map(entity)).thenReturn(parcel);
        // when
        final Parcel result = shipmentRepository.loadParcelById(parcelId);

        // then
        assertEquals(parcel, result);
    }

    @Test
    void shouldNotFindParcelAndThrowException() {
        // given
        final Long parcelId = 1L;
        when(readRepository.findParcelEntityById(parcelId)).thenReturn(Optional.empty());

        // when && then
        Assertions.assertThrows(ParcelNotFoundException.class, () -> {
            shipmentRepository.loadParcelById(parcelId);
        });
    }

    @Test
    void shouldUpdate() {
        // given
        final ParcelUpdate parcelUpdate = ParcelUpdate.builder().build();
        final ParcelEntity entity = new ParcelEntity();

        when(parcelMapper.map(parcelUpdate)).thenReturn(entity);
        when(readRepository.save(entity)).thenReturn(entity);
        // when
        final Parcel result = shipmentRepository.update(parcelUpdate);

        // then
        verify(readRepository, times(1)).save(entity);
    }
}
