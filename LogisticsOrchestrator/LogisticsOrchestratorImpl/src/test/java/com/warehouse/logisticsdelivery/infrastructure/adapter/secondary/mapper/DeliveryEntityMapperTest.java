package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DeliveryStepId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryCommentCode;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryStepOutcome;
import com.warehouse.logisticsdelivery.domain.model.DeliveryStep;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DeliveryStepEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryEntityMapperTest {

    private final DeliveryEntityMapper mapper = Mappers.getMapper(DeliveryEntityMapper.class);

    @Test
    void shouldMapCommentCodeBetweenDomainAndEntity() {
        final DeliveryStep step = DeliveryStep.attempt(DeliveryId.generate(), DeliveryStatus.DEPOT, null, null, null,
                null, DeliveryCommentCode.REPLACED_BY_PALLET_DELIVERY, 1);

        final DeliveryStepEntity entity = mapper.toEntity(step);
        final DeliveryStep restored = mapper.toDomain(entity);

        assertThat(entity.getCommentCode()).isEqualTo(DeliveryCommentCode.REPLACED_BY_PALLET_DELIVERY);
        assertThat(restored.commentCode()).isEqualTo(DeliveryCommentCode.REPLACED_BY_PALLET_DELIVERY);
        assertThat(restored.comment()).isNull();
    }

    @Test
    void shouldKeepLegacyTextCommentWithoutCode() {
        final DeliveryStepEntity entity = DeliveryStepEntity.builder()
                .id(DeliveryStepId.generate())
                .deliveryId(DeliveryId.generate())
                .stepNumber(2)
                .attemptedAt(LocalDateTime.now())
                .outcome(DeliveryStepOutcome.IN_PROGRESS)
                .comment("Left at reception")
                .build();

        final DeliveryStep step = mapper.toDomain(entity);

        assertThat(step.comment()).isEqualTo("Left at reception");
        assertThat(step.commentCode()).isNull();
    }
}
