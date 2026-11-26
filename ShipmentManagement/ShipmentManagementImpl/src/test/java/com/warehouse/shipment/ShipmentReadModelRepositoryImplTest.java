package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentReadModelRepositoryImpl;
import com.warehouse.shipment.domain.vo.ShipmentDeliveryDetails;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentReadEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentPersistenceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ShipmentReadModelRepositoryImplTest {

    @SuppressWarnings("unchecked")
    private final OperatorFilteredRepository<ShipmentReadEntity> repository = mock(OperatorFilteredRepository.class);
    @SuppressWarnings("unchecked")
    private final Criteria<ShipmentReadEntity> criteria = mock(Criteria.class);
    private final ShipmentPersistenceMapper mapper = new ShipmentPersistenceMapper();
    private final ShipmentReadModelRepositoryImpl readModelRepository =
            new ShipmentReadModelRepositoryImpl(repository, mapper);
    private final ShipmentSnapshot snapshot = DataTestCreator.shipment().snapshot();
    private final LocalDateTime plannedAt = LocalDateTime.of(2026, 11, 6, 12, 0);
    private final PalletTrackingNumber palletTrackingNumber = new PalletTrackingNumber("PAL000000000001");

    @BeforeEach
    void setUp() {
        when(repository.createCriteria(ShipmentReadEntity.class)).thenReturn(criteria);
        when(criteria.eq(anyString(), any())).thenReturn(criteria);
    }

    @Test
    void shouldWriteDeliveryDepartmentsWhenCreatingReadModel() {
        when(criteria.one()).thenReturn(Optional.empty());

        readModelRepository.sync(snapshot, new ShipmentDeliveryDetails(new DepartmentId(3L), new DepartmentId(5L),
                new DepartmentId(4L), plannedAt, null));

        final ArgumentCaptor<ShipmentReadEntity> entity = ArgumentCaptor.forClass(ShipmentReadEntity.class);
        verify(repository).create(entity.capture());
        assertEquals(new ShipmentDeliveryDetails(new DepartmentId(3L), new DepartmentId(5L), new DepartmentId(4L),
                        plannedAt, null),
                mapper.deliveryDetails(entity.getValue()));
    }

    @Test
    void shouldWriteAssignedCourierOfDelivery() {
        when(criteria.one()).thenReturn(Optional.empty());
        final ShipmentDeliveryDetails details = new ShipmentDeliveryDetails(new DepartmentId(3L), new DepartmentId(5L),
                new DepartmentId(5L), plannedAt, null, new SupplierCode("genfgr"), "Agnieszka Lewandowicz");

        readModelRepository.sync(snapshot, details);

        final ArgumentCaptor<ShipmentReadEntity> entity = ArgumentCaptor.forClass(ShipmentReadEntity.class);
        verify(repository).create(entity.capture());
        assertEquals(details, mapper.deliveryDetails(entity.getValue()));
    }

    @Test
    void shouldReplaceDepartmentsWithDeliveryDepartmentsWhenReadModelExists() {
        final ShipmentReadEntity current = mapper.withDeliveryDetails(mapper.toReadEntity(snapshot),
                new ShipmentDeliveryDetails(new DepartmentId(1L), new DepartmentId(2L), new DepartmentId(1L), null, null));
        when(criteria.one()).thenReturn(Optional.of(current));

        readModelRepository.sync(snapshot, new ShipmentDeliveryDetails(new DepartmentId(3L), new DepartmentId(5L),
                new DepartmentId(4L), plannedAt, null));

        final ArgumentCaptor<ShipmentReadEntity> entity = ArgumentCaptor.forClass(ShipmentReadEntity.class);
        verify(repository).update(entity.capture());
        assertEquals(new ShipmentDeliveryDetails(new DepartmentId(3L), new DepartmentId(5L), new DepartmentId(4L),
                        plannedAt, null),
                mapper.deliveryDetails(entity.getValue()));
    }

    @Test
    void shouldCreateReadModelWithoutPalletTrackingNumber() {
        when(criteria.one()).thenReturn(Optional.empty());

        readModelRepository.sync(snapshot);

        final ArgumentCaptor<ShipmentReadEntity> entity = ArgumentCaptor.forClass(ShipmentReadEntity.class);
        verify(repository).create(entity.capture());
        assertNull(entity.getValue().getPalletTrackingNumber());
    }

    @Test
    void shouldKeepPalletTrackingNumberWhenShipmentStateIsSynced() {
        final ShipmentReadEntity current = mapper.withPalletTrackingNumber(mapper.toReadEntity(snapshot),
                palletTrackingNumber);
        when(criteria.one()).thenReturn(Optional.of(current));

        readModelRepository.sync(snapshot);

        final ArgumentCaptor<ShipmentReadEntity> entity = ArgumentCaptor.forClass(ShipmentReadEntity.class);
        verify(repository).update(entity.capture());
        assertEquals(palletTrackingNumber, entity.getValue().getPalletTrackingNumber());
    }

    @Test
    void shouldKeepPalletTrackingNumberWhenDeliveryDetailsAreSynced() {
        final ShipmentReadEntity current = mapper.withPalletTrackingNumber(mapper.toReadEntity(snapshot),
                palletTrackingNumber);
        when(criteria.one()).thenReturn(Optional.of(current));

        readModelRepository.sync(snapshot, new ShipmentDeliveryDetails(new DepartmentId(3L), new DepartmentId(5L),
                new DepartmentId(4L), plannedAt, null));

        final ArgumentCaptor<ShipmentReadEntity> entity = ArgumentCaptor.forClass(ShipmentReadEntity.class);
        verify(repository).update(entity.capture());
        assertEquals(palletTrackingNumber, entity.getValue().getPalletTrackingNumber());
    }

    @Test
    void shouldWritePalletTrackingNumberWhenShipmentIsLoadedOnPallet() {
        final ShipmentReadEntity current = mapper.withIncidentReported(mapper.withDeliveryDetails(
                mapper.toReadEntity(snapshot), new ShipmentDeliveryDetails(new DepartmentId(1L),
                        new DepartmentId(2L), new DepartmentId(1L), plannedAt, null)), true);
        when(criteria.one()).thenReturn(Optional.of(current));

        readModelRepository.changePalletTrackingNumber(snapshot.shipmentId(), palletTrackingNumber);

        final ArgumentCaptor<ShipmentReadEntity> entity = ArgumentCaptor.forClass(ShipmentReadEntity.class);
        verify(repository).update(entity.capture());
        assertEquals(palletTrackingNumber, entity.getValue().getPalletTrackingNumber());
        assertEquals(mapper.deliveryDetails(current), mapper.deliveryDetails(entity.getValue()));
        assertEquals(true, mapper.incidentReported(entity.getValue()));
    }

    @Test
    void shouldClearPalletTrackingNumberWhenShipmentLeavesPallet() {
        final ShipmentReadEntity current = mapper.withPalletTrackingNumber(mapper.toReadEntity(snapshot),
                palletTrackingNumber);
        when(criteria.one()).thenReturn(Optional.of(current));

        readModelRepository.changePalletTrackingNumber(snapshot.shipmentId(), null);

        final ArgumentCaptor<ShipmentReadEntity> entity = ArgumentCaptor.forClass(ShipmentReadEntity.class);
        verify(repository).update(entity.capture());
        assertNull(entity.getValue().getPalletTrackingNumber());
    }

    @Test
    void shouldIgnorePalletTrackingNumberWhenReadModelDoesNotExist() {
        when(criteria.one()).thenReturn(Optional.empty());

        readModelRepository.changePalletTrackingNumber(snapshot.shipmentId(), palletTrackingNumber);

        verify(repository, never()).update(any());
        verify(repository, never()).create(any());
    }

    @Test
    void shouldFindPalletTrackingNumber() {
        final ShipmentReadEntity current = mapper.withPalletTrackingNumber(mapper.toReadEntity(snapshot),
                palletTrackingNumber);
        when(criteria.one()).thenReturn(Optional.of(current));

        assertEquals(Optional.of(palletTrackingNumber),
                readModelRepository.findPalletTrackingNumber(snapshot.shipmentId()));
    }

    @Test
    void shouldNotFindPalletTrackingNumberWhenShipmentIsNotOnPallet() {
        when(criteria.one()).thenReturn(Optional.of(mapper.toReadEntity(snapshot)));

        assertEquals(Optional.empty(), readModelRepository.findPalletTrackingNumber(snapshot.shipmentId()));
    }
}
