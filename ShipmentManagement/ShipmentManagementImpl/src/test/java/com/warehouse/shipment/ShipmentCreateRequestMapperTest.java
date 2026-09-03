package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.domain.enumeration.PackagingType;
import com.warehouse.shipment.domain.vo.CustomerReference;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;
import com.warehouse.shipment.infrastructure.adapter.primary.api.DimensionsApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.LengthUnitDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.MoneyApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.PackagingTypeDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.PersonApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentCreateRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentPriorityDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentServiceLevelDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.WeightApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.WeightUnitDto;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;

class ShipmentCreateRequestMapperTest {

    private final ShipmentRequestMapper mapper = Mappers.getMapper(ShipmentRequestMapper.class);

    @Test
    void shouldMapParcelDetailsFromCreateRequest() {
        final ShipmentCreateRequestApi request = new ShipmentCreateRequestApi(
                person(CountryCode.PL), person(CountryCode.DE),
                new DimensionsApi(new BigDecimal("40"), new BigDecimal("30"), new BigDecimal("20"), LengthUnitDto.CM),
                new WeightApi(new BigDecimal("5.5"), WeightUnitDto.KG),
                "Electronics", new MoneyApi(new BigDecimal("2500"), "PLN"), "ORDER-123",
                new MoneyApi(new BigDecimal("25"), "PLN"), ShipmentPriorityDto.MEDIUM,
                PackagingTypeDto.BOX, ShipmentServiceLevelDto.EXPRESS,
                null, null, null, null);

        final ShipmentCreateCommand command = mapper.mapCreateRequest(request);

        assertThat(command.getDimensions()).isEqualTo(new Dimensions(
                new BigDecimal("40"), new BigDecimal("30"), new BigDecimal("20"), LengthUnit.CM));
        assertThat(command.getWeight()).isEqualTo(new Weight(new BigDecimal("5.5"), WeightUnit.KG));
        assertThat(command.getCustomerReference()).isEqualTo(new CustomerReference("ORDER-123"));
        assertThat(command.getContentDescription()).isEqualTo("Electronics");
        assertThat(command.getDeclaredValue().getAmount()).isEqualByComparingTo("2500");
        assertThat(command.getDeclaredValue().getCurrency()).isEqualTo(Currency.PLN);
        assertThat(command.getPackagingType()).isEqualTo(PackagingType.BOX);
        assertThat(command.getServiceLevel()).isEqualTo(ShipmentServiceLevel.EXPRESS);
        assertThat(command.getSender().getCountryCode()).isEqualTo(CountryCode.PL);
        assertThat(command.getRecipient().getCountryCode()).isEqualTo(CountryCode.DE);
    }

    @Test
    void shouldDeserializeCreateRequestWithPeopleAndServiceOptions() throws JsonProcessingException {
        final String json = """
                {
                  "sender": {
                    "firstName": "Jan", "lastName": "Nowak", "email": "jan@example.com",
                    "telephoneNumber": "123456789", "city": "Warsaw", "postalCode": "00-001",
                    "street": "Main 1", "countryCode": "PL"
                  },
                  "recipient": {
                    "firstName": "Anna", "lastName": "Kowalska", "email": "anna@example.com",
                    "telephoneNumber": "987654321", "city": "Berlin", "postalCode": "10115",
                    "street": "Street 2", "countryCode": "DE"
                  },
                  "packagingType": "BOX",
                  "serviceLevel": "EXPRESS"
                }
                """;

        final ShipmentCreateRequestApi request = new ObjectMapper().readValue(json, ShipmentCreateRequestApi.class);

        assertThat(request.sender().firstName()).isEqualTo("Jan");
        assertThat(request.sender().countryCode().name()).isEqualTo("PL");
        assertThat(request.recipient().firstName()).isEqualTo("Anna");
        assertThat(request.recipient().countryCode().name()).isEqualTo("DE");
        assertThat(request.packagingType()).isEqualTo(PackagingTypeDto.BOX);
        assertThat(request.serviceLevel()).isEqualTo(ShipmentServiceLevelDto.EXPRESS);
        assertThat(mapper.mapToParty(request.sender()).getFirstName()).isEqualTo("Jan");
    }

    private PersonApi person(final CountryCode countryCode) {
        return new PersonApi("First", "Last", "person@example.com", "123456789", "Warsaw", "00-001", "Main 1", countryCode);
    }
}
