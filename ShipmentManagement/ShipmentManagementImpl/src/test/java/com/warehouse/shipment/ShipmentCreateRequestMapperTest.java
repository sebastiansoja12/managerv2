package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.primary.command.ShipmentUpdateCommand;
import com.warehouse.shipment.domain.vo.CustomerReference;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;
import com.warehouse.shipment.infrastructure.adapter.primary.api.DimensionsApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.LengthUnitDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.MoneyApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.PersonApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentCreateRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentPriorityDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentConfigurationApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentStatusDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentUpdateRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.WeightApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.WeightUnitDto;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;

class ShipmentCreateRequestMapperTest {

    private final ShipmentRequestMapper mapper = Mappers.getMapper(ShipmentRequestMapper.class);

    @Test
    void shouldMapParcelDetailsFromCreateRequest() {
        final ShipmentCreateRequestApi request = new ShipmentCreateRequestApi(
                person(), person(),
                new DimensionsApi(new BigDecimal("40"), new BigDecimal("30"), new BigDecimal("20"), LengthUnitDto.CM),
                new WeightApi(new BigDecimal("5.5"), WeightUnitDto.KG),
                "Electronics", new MoneyApi(new BigDecimal("2500"), "PLN"), "ORDER-123",
                new MoneyApi(new BigDecimal("25"), "PLN"), null, ShipmentPriorityDto.MEDIUM,
                "PL", "DE", null, null, null, null);

        final ShipmentCreateCommand command = mapper.mapCreateRequest(request);

        assertThat(command.getDimensions()).isEqualTo(new Dimensions(
                new BigDecimal("40"), new BigDecimal("30"), new BigDecimal("20"), LengthUnit.CM));
        assertThat(command.getWeight()).isEqualTo(new Weight(new BigDecimal("5.5"), WeightUnit.KG));
        assertThat(command.getCustomerReference()).isEqualTo(new CustomerReference("ORDER-123"));
        assertThat(command.getContentDescription()).isEqualTo("Electronics");
        assertThat(command.getDeclaredValue().getAmount()).isEqualByComparingTo("2500");
        assertThat(command.getDeclaredValue().getCurrency()).isEqualTo(Currency.PLN);
        assertThat(command.getShipmentSize()).isNull();
    }

    @Test
    void shouldMapParcelDetailsFromUpdateRequest() {
        final ShipmentUpdateRequestApi request = new ShipmentUpdateRequestApi(
                new ShipmentIdDto(42L), person(), person(),
                new com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCodeApi("WAW"),
                new DimensionsApi(new BigDecimal("40"), new BigDecimal("30"), new BigDecimal("20"), LengthUnitDto.CM),
                new WeightApi(new BigDecimal("5.5"), WeightUnitDto.KG), "Electronics",
                new MoneyApi(new BigDecimal("2500"), "PLN"), "ORDER-123",
                new MoneyApi(new BigDecimal("25"), "PLN"), null, ShipmentPriorityDto.MEDIUM,
                ShipmentStatusDto.CREATED, "PL", "DE", new ShipmentConfigurationApi(false, false, false));

        final ShipmentUpdateCommand command = mapper.map(request);

        assertThat(command.getDimensions()).isEqualTo(new Dimensions(
                new BigDecimal("40"), new BigDecimal("30"), new BigDecimal("20"), LengthUnit.CM));
        assertThat(command.getWeight()).isEqualTo(new Weight(new BigDecimal("5.5"), WeightUnit.KG));
        assertThat(command.getCustomerReference()).isEqualTo(new CustomerReference("ORDER-123"));
        assertThat(command.getContentDescription()).isEqualTo("Electronics");
        assertThat(command.getDeclaredValue().getAmount()).isEqualByComparingTo("2500");
        assertThat(command.getDeclaredValue().getCurrency()).isEqualTo(Currency.PLN);
    }

    private PersonApi person() {
        return new PersonApi("First", "Last", "person@example.com", "123456789", "Warsaw", "00-001", "Main 1");
    }
}
