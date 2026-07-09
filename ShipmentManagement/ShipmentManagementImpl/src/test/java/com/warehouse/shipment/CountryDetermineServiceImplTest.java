package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.recipient;
import static com.warehouse.shipment.DataTestCreator.sender;
import static com.warehouse.shipment.DataTestCreator.shipmentId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.ShipmentCreateCommand;
import com.warehouse.shipment.domain.port.secondary.CountryDetermineServicePort;
import com.warehouse.shipment.domain.port.secondary.CountryRepository;
import com.warehouse.shipment.domain.service.CountryDetermineServiceImpl;
import com.warehouse.shipment.domain.vo.CountryDetermine;
import com.warehouse.shipment.domain.vo.LocationInfo;
import com.warehouse.shipment.domain.vo.ShipmentCountry;

@ExtendWith(MockitoExtension.class)
class CountryDetermineServiceImplTest {

    @Mock
    private CountryDetermineServicePort countryDetermineServicePort;

    @Mock
    private CountryRepository countryRepository;

    @Test
    void shouldDetermineCountryByCode() {
        final CountryDetermineServiceImpl service =
                new CountryDetermineServiceImpl(countryDetermineServicePort, countryRepository);
        when(countryRepository.getCountryNameByCode(CountryCode.PL)).thenReturn(Country.POLAND);

        final Country country = service.determineCountryByCode(CountryCode.PL);

        assertEquals(Country.POLAND, country);
        verify(countryRepository).getCountryNameByCode(CountryCode.PL);
    }

    @Test
    void shouldReturnSuccessWhenCountryIsDeterminedForShipmentCreateCommand() {
        final CountryDetermineServiceImpl service =
                new CountryDetermineServiceImpl(countryDetermineServicePort, countryRepository);
        final ShipmentCreateCommand command = new ShipmentCreateCommand();
        command.setSender(sender());
        command.setRecipient(recipient());
        when(countryDetermineServicePort.determineCountry(any(LocationInfo.class)))
                .thenReturn(new ShipmentCountry(Country.POLAND, Country.GERMANY, shipmentId()));

        final Result<CountryDetermine, ErrorCode> result = service.determineCountry(command);

        assertTrue(result.isSuccess());
        verify(countryDetermineServicePort).determineCountry(any(LocationInfo.class));
    }
}
