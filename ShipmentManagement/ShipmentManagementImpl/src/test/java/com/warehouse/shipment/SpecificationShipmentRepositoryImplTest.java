package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;
import com.warehouse.shipment.infrastructure.adapter.secondary.SpecificationShipmentRepositoryImpl;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentReadEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentPersistenceMapper;

class SpecificationShipmentRepositoryImplTest {

    private final OperatorFilteredRepository<ShipmentReadEntity> repository = mock(OperatorFilteredRepository.class);
    private final Criteria<ShipmentReadEntity> criteria = mock(Criteria.class, RETURNS_SELF);
    private final SpecificationShipmentRepositoryImpl specificationRepository =
            new SpecificationShipmentRepositoryImpl(repository, mock(ShipmentPersistenceMapper.class));

    @Test
    void shouldApplyFiltersAndPagingWhenListing() {
        when(repository.createCriteria(ShipmentReadEntity.class)).thenReturn(criteria);
        when(criteria.list()).thenReturn(List.of());

        specificationRepository.list(searchCriteria(2, 20));

        verify(criteria).in("shipmentStatus", List.of(ShipmentStatus.CREATED));
        verify(criteria).desc("createdAt");
        verify(criteria).firstResult(40);
        verify(criteria).maxResults(20);
    }

    @Test
    void shouldCountWithSameFiltersWithoutPaging() {
        when(repository.createCriteria(ShipmentReadEntity.class)).thenReturn(criteria);
        when(criteria.count()).thenReturn(41L);

        final long count = specificationRepository.count(searchCriteria(2, 20));

        assertEquals(41L, count);
        verify(criteria).in("shipmentStatus", List.of(ShipmentStatus.CREATED));
        verify(criteria, never()).firstResult(40);
        verify(criteria, never()).maxResults(20);
    }

    private ShipmentSearchCriteria searchCriteria(final Integer page, final Integer size) {
        return new ShipmentSearchCriteria(null, null, List.of(ShipmentStatus.CREATED), List.of(), null, null,
                null, null, null, null, null, null, null, page, size);
    }
}
