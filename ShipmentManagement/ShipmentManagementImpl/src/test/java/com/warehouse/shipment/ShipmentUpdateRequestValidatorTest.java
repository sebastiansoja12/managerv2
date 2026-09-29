package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.warehouse.shipment.application.service.PriceService;
import com.warehouse.shipment.infrastructure.adapter.primary.api.DimensionsApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.LengthUnitDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.MoneyApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.PersonApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentConfigurationApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentPriorityDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentStatusDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentUpdateRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.WeightApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.WeightUnitDto;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.ShipmentValidationException;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidatorImpl;

class ShipmentUpdateRequestValidatorTest {

    private final ShipmentRequestValidatorImpl validator = new ShipmentRequestValidatorImpl(mock(PriceService.class));

    @Test
    void shouldRejectUpdateWhenAnyDimensionIsNotPositive() {
        final ShipmentUpdateRequestApi request = new ShipmentUpdateRequestApi(
                new ShipmentIdDto(42L), person(), person(),
                new com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCodeApi("WAW"),
                new DimensionsApi(BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.TEN, LengthUnitDto.CM),
                new WeightApi(BigDecimal.ONE, WeightUnitDto.KG), null, null, null,
                new MoneyApi(BigDecimal.TEN, "PLN"), null, ShipmentPriorityDto.MEDIUM,
                ShipmentStatusDto.CREATED, "PL", "DE", new ShipmentConfigurationApi(false, false, false));

        assertThatThrownBy(() -> validator.validateBody(request))
                .isInstanceOf(ShipmentValidationException.class);
    }

    private PersonApi person() {
        return new PersonApi("First", "Last", "person@example.com", "123456789", "Warsaw", "00-001", "Main 1");
    }
}
