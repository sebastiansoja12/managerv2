package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.warehouse.commonassets.enumeration.CancellationReason;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.vo.CustomerReference;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;
import org.junit.jupiter.api.Test;

import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentReadEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentPersistenceMapper;

class ShipmentPersistenceMapperTest {

    private final ShipmentPersistenceMapper mapper = new ShipmentPersistenceMapper();

    @Test
    void shouldRehydrateExistingStateWithoutChangingTimestamps() {
        final Shipment original = DataTestCreator.shipment();

        final ShipmentEntity entity = this.mapper.toEntity(original);
        final Shipment rehydrated = this.mapper.toDomain(entity);

        assertThat(rehydrated.getShipmentId()).isEqualTo(original.getShipmentId());
        assertThat(rehydrated.getShipmentStatus()).isEqualTo(original.getShipmentStatus());
        assertThat(rehydrated.getShipmentType()).isEqualTo(original.getShipmentType());
        assertThat(rehydrated.getCreatedAt()).isEqualTo(original.getCreatedAt());
        assertThat(rehydrated.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
        assertThat(rehydrated.getExternalShipmentId()).isEqualTo(original.getExternalShipmentId());
        assertThat(rehydrated.getTrackingNumber()).isEqualTo(original.getTrackingNumber());
        assertThat(rehydrated.getSender().getAddress().getCountryCode()).isEqualTo(CountryCode.PL);
        assertThat(rehydrated.getRecipient().getAddress().getCountryCode()).isEqualTo(CountryCode.DE);
    }

    @Test
    void shouldPersistAndRehydrateAcceptanceTimestamp() {
        final Shipment original = DataTestCreator.shipment();
        original.prepareShipmentToSend();
        original.notifyShipmentAccepted();

        final Shipment rehydrated = this.mapper.toDomain(this.mapper.toEntity(original));

        assertThat(rehydrated.getAcceptedAt()).isEqualTo(original.getAcceptedAt());
    }

    @Test
    void shouldPersistAndRehydrateCancellationInformation() {
        final Shipment original = DataTestCreator.shipment();
        final LocalDateTime cancelledAt = original.getCreatedAt().plusMinutes(5);
        original.markAsCanceledWithoutPolicy(CancellationReason.CUSTOMER_REQUEST, cancelledAt);

        final Shipment rehydrated = this.mapper.toDomain(this.mapper.toEntity(original));

        assertThat(rehydrated.getCancelledAt()).isEqualTo(cancelledAt);
        assertThat(rehydrated.getCancellationReason()).isEqualTo(CancellationReason.CUSTOMER_REQUEST);
    }

    @Test
    void shouldCopyLifecycleInformationToReadModel() {
        final Shipment original = DataTestCreator.shipment();
        final LocalDateTime cancelledAt = original.getCreatedAt().plusMinutes(5);
        original.markAsCanceledWithoutPolicy(CancellationReason.CUSTOMER_REQUEST, cancelledAt);

        final Shipment rehydrated = this.mapper.toDomain(this.mapper.toReadEntity(original.snapshot(), null));

        assertThat(rehydrated.getCancelledAt()).isEqualTo(cancelledAt);
        assertThat(rehydrated.getCancellationReason()).isEqualTo(CancellationReason.CUSTOMER_REQUEST);
    }

    @Test
    void shouldIncludeSignatureIdWhenCreatingReadEntity() {
        final Shipment original = DataTestCreator.shipment();
        final SignatureId signatureId = new SignatureId(42L);

        final ShipmentReadEntity readEntity = this.mapper.toReadEntity(original.snapshot(), signatureId);

        assertThat(readEntity.getSignatureId()).isEqualTo(signatureId);
    }

    @Test
    void shouldPersistAndRehydrateNewShipmentDetails() {
        final Dimensions dimensions = new Dimensions(
                new BigDecimal("40"), new BigDecimal("30"), new BigDecimal("20"), LengthUnit.CM);
        final Weight weight = new Weight(new BigDecimal("5.5"), WeightUnit.KG);
        final Money declaredValue = new Money(new BigDecimal("2500.00"), Currency.PLN);
        final Shipment original = DataTestCreator.shipmentWithParcelDetails();

        final Shipment rehydrated = this.mapper.toDomain(this.mapper.toEntity(original));

        assertThat(rehydrated.getDimensions()).isEqualTo(dimensions);
        assertThat(rehydrated.getWeight()).isEqualTo(weight);
        assertThat(rehydrated.getCustomerReference()).isEqualTo(new CustomerReference("ORDER-2026-12345"));
        assertThat(rehydrated.getContentDescription()).isEqualTo("Electronics");
        assertThat(rehydrated.getSender().getAddress().getCountryCode()).isEqualTo(CountryCode.PL);
        assertThat(rehydrated.getRecipient().getAddress().getCountryCode()).isEqualTo(CountryCode.DE);
        assertThat(rehydrated.getDeclaredValue().getAmount()).isEqualByComparingTo(declaredValue.getAmount());
        assertThat(rehydrated.getDeclaredValue().getCurrency()).isEqualTo(declaredValue.getCurrency());
        assertThat(rehydrated.getPrice().getAmount()).isNotEqualByComparingTo(declaredValue.getAmount());
    }

    @Test
    void shouldCopyNewShipmentDetailsToReadModel() {
        final Shipment original = DataTestCreator.shipmentWithParcelDetails();
        final Dimensions dimensions = new Dimensions(
                new BigDecimal("40"), new BigDecimal("30"), new BigDecimal("20"), LengthUnit.CM);
        final Weight weight = new Weight(new BigDecimal("5.5"), WeightUnit.KG);

        final Shipment rehydrated = this.mapper.toDomain(this.mapper.toReadEntity(original.snapshot(), null));

        assertThat(rehydrated.getDimensions()).isEqualTo(dimensions);
        assertThat(rehydrated.getWeight()).isEqualTo(weight);
        assertThat(rehydrated.getCustomerReference()).isEqualTo(new CustomerReference("ORDER-2026-12345"));
        assertThat(rehydrated.getContentDescription()).isEqualTo("Electronics");
    }
}
