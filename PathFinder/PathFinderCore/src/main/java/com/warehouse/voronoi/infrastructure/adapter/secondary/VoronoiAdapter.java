package com.warehouse.voronoi.infrastructure.adapter.secondary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.support.RestGatewaySupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.warehouse.positionstack.PositionStackProperties;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServiceConfiguration;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;
import com.warehouse.voronoi.infrastructure.adapter.secondary.exception.CoordinatesTechnicalException;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class VoronoiAdapter extends RestGatewaySupport implements VoronoiServicePort {

    @NonNull
    private final PositionStackProperties positionStackProperties;

    private final String DATA = "data";

    private final String LATITUDE = "latitude";

    private final String LONGITUDE = "longitude";

    @Override
    public Coordinates obtainCoordinates(String requestCity) {
        final VoronoiAdapterConfiguration voronoiAdapterConfiguration = new VoronoiAdapterConfiguration();
        return calculate(requestCity, voronoiAdapterConfiguration);
    }

    private Coordinates calculate(String city, VoronoiAdapterConfiguration voronoiAdapterConfiguration) {
        final String url = voronoiAdapterConfiguration.requestUrl(city);
        final ResponseEntity<JsonNode> responseEntity = getRestTemplate().getForEntity(url, JsonNode.class);
        final JsonNode jsonNode = responseEntity.getBody();

        if (jsonNode == null) {
            throw new CoordinatesTechnicalException("Failed to retrieve coordinates from the API.");
        }

        final String lat = jsonNode.get(DATA).get(0).get(LATITUDE).asText();
        final String lon = jsonNode.get(DATA).get(0).get(LONGITUDE).asText();
        return getCoordinates(lon, lat);
    }

    private Coordinates getCoordinates(String lon, String lat) {
        return Coordinates.builder()
                .lat(Double.parseDouble(lat))
                .lon(Double.parseDouble(lon))
                .build();
    }

    private class VoronoiAdapterConfiguration implements VoronoiServiceConfiguration {

        @Override
        public String getUrl() {
            return positionStackProperties.getUrl();
        }

        @Override
        public String getStage() {
            return positionStackProperties.getStage();
        }

        @Override
        public String requestUrl(String value) {
            return positionStackProperties.createRequest(value);
        }
    }

}
