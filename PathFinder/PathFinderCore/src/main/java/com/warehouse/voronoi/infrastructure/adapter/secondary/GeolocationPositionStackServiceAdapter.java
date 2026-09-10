package com.warehouse.voronoi.infrastructure.adapter.secondary;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.support.RestGatewaySupport;
import org.springframework.web.util.UriComponentsBuilder;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.positionstack.PositionStackProperties;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.port.secondary.GeolocationServiceProvider;
import com.warehouse.voronoi.domain.port.secondary.PositionStackRepository;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServiceConfiguration;
import com.warehouse.voronoi.domain.vo.GeocodingAddress;
import com.warehouse.voronoi.domain.vo.GeocodingConfig;
import com.warehouse.voronoi.infrastructure.adapter.secondary.dto.PositionStackGeocodingResponse;
import com.warehouse.voronoi.infrastructure.adapter.secondary.dto.PositionStackGeocodingResult;
import com.warehouse.voronoi.infrastructure.adapter.secondary.exception.CoordinatesTechnicalException;

import lombok.NonNull;


public class GeolocationPositionStackServiceAdapter extends RestGatewaySupport implements GeolocationServiceProvider {

    @NonNull
    private final PositionStackProperties positionStackProperties;

    private final PositionStackRepository positionStackRepository;

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
    public Coordinates obtainCoordinates(final GeocodingAddress address, final GeocodingConfig config) {
        final PositionStackAdapterConfiguration positionStackAdapterConfiguration = new PositionStackAdapterConfiguration(config);
        return calculate(address, positionStackAdapterConfiguration);
    }

    private Coordinates calculate(final GeocodingAddress address,
                                  final PositionStackAdapterConfiguration positionStackAdapterConfiguration) {
        final URI uri = positionStackAdapterConfiguration.requestUri(address.formattedAddress());
        final ResponseEntity<PositionStackGeocodingResponse> responseEntity =
                getRestTemplate().getForEntity(uri, PositionStackGeocodingResponse.class);
        final PositionStackGeocodingResponse response = responseEntity.getBody();

        if (response == null || response.data() == null || response.data().isEmpty()) {
            throw new CoordinatesTechnicalException("Failed to retrieve coordinates from the API.");
        }

        final PositionStackGeocodingResult result = response.data().get(0);
        return getCoordinates(result.longitude(), result.latitude());
    }

    private Coordinates getCoordinates(final Double lon, final Double lat) {
        return Coordinates.builder()
                .lat(lat)
                .lon(lon)
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
            return requestUri(value).toString();
        }

        private URI requestUri(final String value) {
            return UriComponentsBuilder.fromUriString(geocodingConfig.baseUrl())
                    .queryParam("access_key", apiKey())
                    .queryParam("query", value)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUri();
        }

        private String apiKey() {
            return geocodingConfig.apiKey() == null ? null : geocodingConfig.apiKey().trim();
        }
    }

}
