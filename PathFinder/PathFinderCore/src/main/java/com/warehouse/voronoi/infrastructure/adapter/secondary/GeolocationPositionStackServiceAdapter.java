package com.warehouse.voronoi.infrastructure.adapter.secondary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.support.RestGatewaySupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.positionstack.PositionStackProperties;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.port.secondary.GeolocationServiceProvider;
import com.warehouse.voronoi.domain.port.secondary.PositionStackRepository;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServiceConfiguration;
import com.warehouse.voronoi.domain.vo.GeocodingConfig;
import com.warehouse.voronoi.infrastructure.adapter.secondary.exception.CoordinatesTechnicalException;

import lombok.NonNull;


public class GeolocationPositionStackServiceAdapter extends RestGatewaySupport implements GeolocationServiceProvider {

    @NonNull
    private final PositionStackProperties positionStackProperties;

    private final PositionStackRepository positionStackRepository;

    private final String DATA = "data";

    private final String LATITUDE = "latitude";

    private final String LONGITUDE = "longitude";

    public GeolocationPositionStackServiceAdapter(final @NonNull PositionStackProperties positionStackProperties,
                                                  final PositionStackRepository positionStackRepository) {
        this.positionStackProperties = positionStackProperties;
        this.positionStackRepository = positionStackRepository;
    }

    @Override
    public boolean canHandle(final GeocodingProvider geocodingProvider) {
        return GeocodingProvider.POSITION_STACK.equals(geocodingProvider);
    }

    @Override
    public Coordinates obtainCoordinates(final String requestCity, final GeocodingConfig config) {
        final PositionStackAdapterConfiguration positionStackAdapterConfiguration = new PositionStackAdapterConfiguration(config);
        return calculate(requestCity, positionStackAdapterConfiguration);
    }

    private Coordinates calculate(final String city, final PositionStackAdapterConfiguration positionStackAdapterConfiguration) {
        final String url = positionStackAdapterConfiguration.requestUrl(city);
        final ResponseEntity<JsonNode> responseEntity = getRestTemplate().getForEntity(url, JsonNode.class);
        final JsonNode jsonNode = responseEntity.getBody();

        if (jsonNode == null) {
            throw new CoordinatesTechnicalException("Failed to retrieve coordinates from the API.");
        }

        final String lat = jsonNode.get(DATA).get(0).get(LATITUDE).asText();
        final String lon = jsonNode.get(DATA).get(0).get(LONGITUDE).asText();
        return getCoordinates(lon, lat);
    }

    private Coordinates getCoordinates(final String lon, final String lat) {
        return Coordinates.builder()
                .lat(Double.parseDouble(lat))
                .lon(Double.parseDouble(lon))
                .build();
    }

    private class PositionStackAdapterConfiguration implements VoronoiServiceConfiguration {

        private final GeocodingConfig geocodingConfig;

        private PositionStackAdapterConfiguration(final GeocodingConfig config) {
            this.geocodingConfig = config;
        }

        @Override
        public String getUrl() {
            return positionStackProperties.getUrl();
        }

        @Override
        public String getStage() {
            return positionStackProperties.getStage();
        }

        @Override
        public String requestUrl(final String value) {
            return positionStackProperties.createRequest(geocodingConfig.baseUrl(),
                    geocodingConfig.apiKey(), value);
        }
    }

}
