package com.warehouse.shipment.infrastructure.adapter.secondary;

import org.springframework.stereotype.Service;

import com.warehouse.shipment.domain.model.Address;
import com.warehouse.shipment.domain.model.City;

@Service
public class PathFinderMockService {

    public City determineDeliveryDepot(Address address) {
        final String city = address.getCity();
        return switch (city) {
            case "Katowice" -> new City("KT3");
            case "Gliwice" -> new City("KT1");
            case "Poznań" -> new City("POZ");
            case "Szczecin" -> new City("SZZ");
            case "Warszawa" -> new City("WA4");
            case "Kraków" -> new City("KR1");
            case "Rzeszów" -> new City("RZE");
            case "Wrocław" -> new City("WRO");
            case "Gdańsk" -> new City("GD1");
            default -> new City("NCS");
        };
    }

}