package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderMockAdapter;
import com.warehouse.shipment.infrastructure.adapter.secondary.PathFinderMockService;

class PathFinderMockAdapterTest {

    private final PathFinderMockService mockService = new PathFinderMockService();
    private final PathFinderMockAdapter pathFinderMockAdapter = new PathFinderMockAdapter(mockService);

    @ParameterizedTest
    @CsvSource({
            "Katowice, Katowicka 1, 40-136, KT3",
            "Gliwice, Zwycięstwa 10, 44-100, KT1",
            "Poznań, Półwiejska 5, 60-001, POZ",
            "Szczecin, Wojska Polskiego 1, 70-001, SZZ",
            "Warszawa, Marszałkowska 1, 00-001, WA4",
            "Kraków, Floriańska 1, 31-001, KR1",
            "Rzeszów, Rejtana 1, 35-001, RZE",
            "Wrocław, Rynek 1, 50-001, WRO",
            "Gdańsk, Długa 1, 80-001, GD1",
            "UnknownCity, Testowa 1, 00-000, NCS"
    })
    void shouldDetermineDestination(
            final String city,
            final String street,
            final String postalCode,
            final String expectedDepot) {
        final Address address = new Address(city, street, postalCode);
        final Result<VoronoiResponse, ErrorCode> result =
                pathFinderMockAdapter.determineDeliveryDepartment(address);
        assertTrue(result.isSuccess());
        assertEquals(expectedDepot, result.getSuccess().getValue());
    }
}
