package com.warehouse.voronoi.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.port.secondary.GeolocationServiceProvider;
import com.warehouse.voronoi.domain.vo.GeocodingAddress;
import com.warehouse.voronoi.domain.vo.GeocodingConfig;
import com.warehouse.voronoi.infrastructure.adapter.secondary.dto.GeoapifyGeocodingFeature;
import com.warehouse.voronoi.infrastructure.adapter.secondary.dto.GeoapifyGeocodingResponse;
import com.warehouse.voronoi.infrastructure.adapter.secondary.dto.GeoapifyGeocodingResult;
import com.warehouse.voronoi.infrastructure.adapter.secondary.exception.CoordinatesTechnicalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.support.RestGatewaySupport;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

public class GeolocationGeoapifyServiceAdapter extends RestGatewaySupport implements GeolocationServiceProvider {

    private static final String FORMAT_JSON = "json";
    private static final int RESULT_LIMIT = 1;

    @Override
    public boolean canHandle(final GeocodingProvider geocodingProvider) {
        return GeocodingProvider.GEOAPIFY.equals(geocodingProvider);
    }

    @Override
    public Coordinates obtainCoordinates(final GeocodingAddress address, final GeocodingConfig config) {
        final URI uri = requestUri(address, config);
        final ResponseEntity<GeoapifyGeocodingResponse> responseEntity =
                getRestTemplate().getForEntity(uri, GeoapifyGeocodingResponse.class);
        final GeoapifyGeocodingResponse response = responseEntity.getBody();

        if (response == null) {
            throw new CoordinatesTechnicalException("Failed to retrieve coordinates from the API.");
        }
        if (response.error() != null || response.message() != null) {
            throw new CoordinatesTechnicalException("Geoapify geocoding failed: " + response.message());
        }

        return getCoordinates(response, address.formattedAddress());
    }

    private URI requestUri(final GeocodingAddress address, final GeocodingConfig geocodingConfig) {
        return UriComponentsBuilder.fromUriString(geocodingConfig.baseUrl())
                .queryParam("text", address.formattedAddress())
                .queryParam("format", FORMAT_JSON)
                .queryParam("limit", RESULT_LIMIT)
                .queryParam("apiKey", geocodingConfig.apiKey())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
    }

    private Coordinates getCoordinates(final GeoapifyGeocodingResponse response, final String requestText) {
        if (response.results() != null && !response.results().isEmpty()) {
            final GeoapifyGeocodingResult result = response.results().getFirst();
            return coordinates(result.lat(), result.lon());
        }

        if (response.features() != null && !response.features().isEmpty()) {
            final GeoapifyGeocodingFeature feature = response.features().getFirst();
            if (feature.properties() != null) {
                return coordinates(feature.properties().lat(), feature.properties().lon());
            }
            if (feature.geometry() != null && feature.geometry().coordinates() != null
                    && feature.geometry().coordinates().size() >= 2) {
                return coordinates(feature.geometry().coordinates().get(1), feature.geometry().coordinates().get(0));
            }
        }

        throw new CoordinatesTechnicalException("Geoapify returned no coordinates for address: " + requestText);
    }

    private Coordinates coordinates(final Double lat, final Double lon) {
        if (lat == null || lon == null) {
            throw new CoordinatesTechnicalException("Failed to retrieve coordinates from the API.");
        }
        return Coordinates.builder()
                .lat(lat)
                .lon(lon)
                .build();
    }
}
