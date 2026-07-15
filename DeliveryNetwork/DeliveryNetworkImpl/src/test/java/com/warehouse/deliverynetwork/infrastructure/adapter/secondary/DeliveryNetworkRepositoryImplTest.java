package com.warehouse.deliverynetwork.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.deliverynetwork.domain.model.DeliveryNetwork;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity.DeliveryNetworkEntity;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity.DeliveryNetworkId;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.mapper.DeliveryNetworkPersistenceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryNetworkRepositoryImplTest {

    @Mock
    private OperatorFilteredRepository<DeliveryNetworkEntity> repository;

    @Mock
    private Criteria<DeliveryNetworkEntity> criteria;

    private DeliveryNetworkRepositoryImpl deliveryNetworkRepository;

    @BeforeEach
    void setUp() {
        this.deliveryNetworkRepository = new DeliveryNetworkRepositoryImpl(
                this.repository, new DeliveryNetworkPersistenceMapper());
        when(this.repository.createCriteria(DeliveryNetworkEntity.class)).thenReturn(this.criteria);
        when(this.criteria.maxResults(1)).thenReturn(this.criteria);
    }

    @Test
    void shouldCreateNetworkWhenCurrentOperatorHasNoConfiguration() {
        when(this.criteria.one()).thenReturn(Optional.empty());

        this.deliveryNetworkRepository.save(network());

        verify(this.repository).create(any(DeliveryNetworkEntity.class));
        verify(this.repository, never()).update(any(DeliveryNetworkEntity.class));
    }

    @Test
    void shouldUpdateNetworkFoundByTenantFilteredCriteria() {
        final DeliveryNetworkEntity existingEntity = new DeliveryNetworkEntity(
                DeliveryNetworkId.generate(), Set.of());
        existingEntity.assignOperator(OperatorId.of(10L));
        when(this.criteria.one()).thenReturn(Optional.of(existingEntity));

        this.deliveryNetworkRepository.save(network());

        verify(this.repository).update(existingEntity);
        verify(this.repository, never()).create(any(DeliveryNetworkEntity.class));
    }

    private static DeliveryNetwork network() {
        return new DeliveryNetwork(
                OperatorId.of(10L),
                Set.of(new DepartmentConnection(new DepartmentId(1L), new DepartmentId(2L))));
    }
}
