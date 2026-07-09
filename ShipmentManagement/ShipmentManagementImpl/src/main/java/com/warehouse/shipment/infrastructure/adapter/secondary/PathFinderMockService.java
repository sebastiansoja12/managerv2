package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.vo.VoronoiResponse;
import org.springframework.stereotype.Service;

import com.warehouse.shipment.domain.vo.Address;

@Service
public class PathFinderMockService {

    public VoronoiResponse determineDeliveryDepot(Address address) {
        final String city = address.getCity();
        return switch (city) {
            case "Katowice" -> new VoronoiResponse("KT3");
            case "Gliwice" -> new VoronoiResponse("KT1");
            case "Poznań" -> new VoronoiResponse("POZ");
            case "Szczecin" -> new VoronoiResponse("SZZ");
            case "Warszawa" -> new VoronoiResponse("WA4");
            case "Kraków" -> new VoronoiResponse("KR1");
            case "Rzeszów" -> new VoronoiResponse("RZE");
            case "Wrocław" -> new VoronoiResponse("WRO");
            case "Gdańsk" -> new VoronoiResponse("GD1");
            default -> new VoronoiResponse("NCS");
        };
    }

}