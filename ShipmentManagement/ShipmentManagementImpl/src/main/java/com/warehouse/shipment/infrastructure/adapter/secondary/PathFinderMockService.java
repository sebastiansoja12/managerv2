package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import org.springframework.stereotype.Service;

import com.warehouse.shipment.domain.vo.Address;

@Service
public class PathFinderMockService {

    public VoronoiResponse determineDeliveryDepot(final Address address) {
        final String city = address.getCity();
        final String depCode = switch (city) {
            case "Katowice" -> "KT3";
            case "Gliwice" -> "KT1";
            case "Poznań" -> "POZ";
            case "Szczecin" -> "SZZ";
            case "Warszawa" -> "WA4";
            case "Kraków" -> "KR1";
            case "Rzeszów" -> "RZE";
            case "Wrocław" -> "WRO";
            case "Gdańsk" -> "GD1";
            default -> "NCS";
        };

        return new VoronoiResponse(new DepartmentCode(depCode));
    }

}