package com.warehouse.voronoi.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.port.secondary.GeolocationServiceProvider;
import com.warehouse.voronoi.domain.vo.GeocodingAddress;
import com.warehouse.voronoi.domain.vo.GeocodingConfig;
import com.warehouse.voronoi.infrastructure.adapter.secondary.dto.OpenCageGeocodingGeometry;
import com.warehouse.voronoi.infrastructure.adapter.secondary.dto.OpenCageGeocodingResponse;
import com.warehouse.voronoi.infrastructure.adapter.secondary.exception.CoordinatesTechnicalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.support.RestGatewaySupport;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

public class GeolocationOpenCageServiceAdapter extends RestGatewaySupport implements GeolocationServiceProvider {

    private static final int STATUS_OK = 200;
    private static final int RESULT_LIMIT = 1;
    private static final int NO_ANNOTATIONS = 1;

    @Override
    public boolean canHandle(final GeocodingProvider geocodingProvider) {
        return GeocodingProvider.OPEN_CAGE.equals(geocodingProvider);
    }

    @Override
    public Coordinates obtainCoordinates(final GeocodingAddress address, final GeocodingConfig config) {
        final URI uri = requestUri(address, config);
        final ResponseEntity<OpenCageGeocodingResponse> responseEntity =
                getRestTemplate().getForEntity(uri, OpenCageGeocodingResponse.class);
        final OpenCageGeocodingResponse response = responseEntity.getBody();

        if (response == null) {
            throw new CoordinatesTechnicalException("Failed to retrieve coordinates from the API.");
        }
        if (response.status() != null && response.status().code() != null
                && response.status().code() != STATUS_OK) {
            throw new CoordinatesTechnicalException("OpenCage geocoding failed: " + response.status().message());
        }

        return getCoordinates(response, address.formattedAddress());
    }

    private URI requestUri(final GeocodingAddress address, final GeocodingConfig geocodingConfig) {
        return UriComponentsBuilder.fromUriString(geocodingConfig.baseUrl())
                .queryParam("q", address.formattedAddress())
                .queryParam("key", geocodingConfig.apiKey())
                .queryParam("limit", RESULT_LIMIT)
                .queryParam("no_annotations", NO_ANNOTATIONS)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
    }

    private Coordinates getCoordinates(final OpenCageGeocodingResponse response, final String requestText) {
        if (response.results() == null || response.results().isEmpty()) {
            throw new CoordinatesTechnicalException("OpenCage returned no coordinates for address: " + requestText);
        }
        final OpenCageGeocodingGeometry geometry = response.results().getFirst().geometry();
        if (geometry == null || geometry.lat() == null || geometry.lng() == null) {
            throw new CoordinatesTechnicalException("Failed to retrieve coordinates from the API.");
        }
        return Coordinates.builder()
                .lat(geometry.lat())
                .lon(geometry.lng())
                .build();
    }
}
