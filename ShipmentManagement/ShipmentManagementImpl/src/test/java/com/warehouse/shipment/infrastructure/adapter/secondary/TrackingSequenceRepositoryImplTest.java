package com.warehouse.shipment.infrastructure.adapter.secondary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.shipment.domain.model.TrackingSequence;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.TrackingSequenceEntity;

@ExtendWith(MockitoExtension.class)
class TrackingSequenceRepositoryImplTest {

    @Mock
    private TrackingSequenceReadRepository repository;

    @Test
    void shouldSaveNewSequenceWithNullVersion() {
        final TrackingSequenceRepositoryImpl adapter = new TrackingSequenceRepositoryImpl(repository);
        final TrackingSequenceEntity savedEntity = new TrackingSequenceEntity("TRACKING_NUMBER_MGR", 2L, 0L);
        when(repository.saveAndFlush(any(TrackingSequenceEntity.class))).thenReturn(savedEntity);

        adapter.save(new TrackingSequence("TRACKING_NUMBER_MGR", 2L));

        final ArgumentCaptor<TrackingSequenceEntity> captor =
                ArgumentCaptor.forClass(TrackingSequenceEntity.class);
        verify(repository).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getVersion()).isNull();
    }

    @Test
    void shouldMapExistingSequenceWithVersion() {
        final TrackingSequenceRepositoryImpl adapter = new TrackingSequenceRepositoryImpl(repository);
        when(repository.findById("TRACKING_NUMBER_MGR"))
                .thenReturn(Optional.of(new TrackingSequenceEntity("TRACKING_NUMBER_MGR", 2L, 3L)));

        final Optional<TrackingSequence> sequence = adapter.findById("TRACKING_NUMBER_MGR");

        assertThat(sequence).isPresent();
        assertThat(sequence.get().getVersion()).isEqualTo(3L);
    }
}
