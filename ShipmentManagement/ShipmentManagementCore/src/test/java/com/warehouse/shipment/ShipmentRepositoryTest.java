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

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentReadRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentRepositoryImpl;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ShipmentNotFoundException;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentEntityMapper;

@ExtendWith(MockitoExtension.class)
public class ShipmentRepositoryTest {

    @Mock
    private ShipmentReadRepository readRepository;

    @Mock
    private ShipmentEntityMapper shipmentEntityMapper;

    private ShipmentRepository shipmentRepository;

    @BeforeEach
    void setUp() {
        shipmentRepository = new ShipmentRepositoryImpl(readRepository);
    }

    @Test
    void shouldCreateShipment() {
        // given
        final Shipment parcel = mock(Shipment.class);
        when(parcel.getShipmentStatus()).thenReturn(ShipmentStatus.CREATED);

        final ParcelEntity entity = new ParcelEntity();

        when(shipmentEntityMapper.map(parcel)).thenReturn(entity);

        // when
        shipmentRepository.createOrUpdate(parcel);
        // then
        Mockito.verify(readRepository).save(entity);
    }

    @Test
    void shouldReturnShipmentById() {
        // given
        final ShipmentId shipmentId = new ShipmentId(1L);
        final ParcelEntity entity = new ParcelEntity();
        final Parcel parcel = mock(Parcel.class);
        when(readRepository.findParcelEntityById(shipmentId.getValue())).thenReturn(Optional.of(entity));
        // when
        final Shipment result = shipmentRepository.findById(shipmentId);

        // then
        assertEquals(parcel, result);
    }

    @Test
    void shouldNotFindShipmentAndThrowException() {
        // given
        final ShipmentId shipmentId = new ShipmentId(1L);
        when(readRepository.findParcelEntityById(shipmentId.getValue())).thenReturn(Optional.empty());

        // when && then
        Assertions.assertThrows(ShipmentNotFoundException.class, () -> {
            shipmentRepository.findById(shipmentId);
        });
    }

    @Test
    void shouldCreateOrUpdate() {
        // given
        final ShipmentUpdate shipmentUpdate = mock(ShipmentUpdate.class);
        final ParcelEntity entity = new ParcelEntity();

        when(readRepository.save(entity)).thenReturn(entity);
        // when
        shipmentRepository.createOrUpdate(mock(Shipment.class));

        // then
        verify(readRepository, times(1)).save(entity);
    }
}
