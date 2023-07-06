package com.warehouse.voronoi.infrastructure.adapter.secondary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fire.positionstack.PositionStackProperties;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Depot;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.service.UrlJsonReaderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class VoronoiAdapter implements VoronoiServicePort {

    private final PositionStackProperties positionStackProperties;

    private final ComputeService computeService;

    private final UrlJsonReaderService jsonReaderService;

    private final String DATA = "data";

    private final String LATITUDE = "latitude";

    private final String LONGITUDE = "longitude";

    @Override
    public String findFastestRoute(List<Depot> depots, String requestCity) {
        final String url = createRequest(requestCity);
        JsonNode jsonNode = null;
        try {
            final URL requestUrl = urlConverter(url);
            jsonNode = result(requestUrl);
        } catch (MalformedURLException e) {
            log.error("Error registered: {0} ", e.getCause());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final String lat = jsonNode.get(DATA).get(0).get(LATITUDE).asText();
        final String lon = jsonNode.get(DATA).get(0).get(LONGITUDE).asText();
        final Coordinates coordinates = getCoordinates(lon, lat);
        return computeService.computeLength(coordinates, depots);

    }

    private Coordinates getCoordinates(String lon, String lat) {
        return Coordinates.builder()
                .lat(Double.parseDouble(lat))
                .lon(Double.parseDouble(lon))
                .build();
    }

    private JsonNode result(URL url) throws IOException {
        return jsonReaderService.get(url);
    }

    private String createRequest(String city) {
        return positionStackProperties.createRequest(city);
    }


    private URL urlConverter(String url) throws MalformedURLException {
        return new URL(url);
    }
}
