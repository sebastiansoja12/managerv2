package com.warehouse.geocoding.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.geocoding.domain.vo.Coordinates;
import com.warehouse.geocoding.application.port.secondary.GeocodingProviderServicePort;
import com.warehouse.geocoding.domain.vo.GeocodingAddress;
import com.warehouse.geocoding.domain.vo.GeocodingConfig;
import com.warehouse.geocoding.infrastructure.adapter.secondary.dto.GeocodifyGeocodingFeature;
import com.warehouse.geocoding.infrastructure.adapter.secondary.dto.GeocodifyGeocodingFeatureCollection;
import com.warehouse.geocoding.infrastructure.adapter.secondary.dto.GeocodifyGeocodingMeta;
import com.warehouse.geocoding.infrastructure.adapter.secondary.dto.GeocodifyGeocodingResponse;
import com.warehouse.geocoding.infrastructure.adapter.secondary.exception.CoordinatesTechnicalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.support.RestGatewaySupport;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GeolocationGeocodifyClientServiceAdapter extends RestGatewaySupport implements GeocodingProviderServicePort {

    private static final int STATUS_OK = 200;

    @Override
    public boolean canHandle(final GeocodingProvider geocodingProvider) {
        return GeocodingProvider.GEOCODIFY.equals(geocodingProvider);
    }

    @Override
    public Coordinates obtainCoordinates(final GeocodingAddress address, final GeocodingConfig config) {
        final URI uri = requestUri(address, config);
        final ResponseEntity<GeocodifyGeocodingResponse> responseEntity =
                getRestTemplate().getForEntity(uri, GeocodifyGeocodingResponse.class);
        final GeocodifyGeocodingResponse response = responseEntity.getBody();

        if (response == null) {
            throw new CoordinatesTechnicalException("Failed to retrieve coordinates from the API.");
        }
        final GeocodifyGeocodingMeta meta = response.meta();
        if (meta != null && meta.code() != null && meta.code() != STATUS_OK) {
            throw new CoordinatesTechnicalException("Geocodify geocoding failed: " + meta.errorDetail());
        }

        return getCoordinates(response.response(), address.formattedAddress());
    }

    private URI requestUri(final GeocodingAddress address, final GeocodingConfig geocodingConfig) {
        return UriComponentsBuilder.fromUriString(geocodingConfig.baseUrl())
                .queryParam("api_key", geocodingConfig.apiKey())
                .queryParam("q", address.formattedAddress())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
    }

    private Coordinates getCoordinates(final GeocodifyGeocodingFeatureCollection featureCollection,
                                       final String requestText) {
        if (featureCollection == null || featureCollection.features() == null
                || featureCollection.features().isEmpty()) {
            throw new CoordinatesTechnicalException("Geocodify returned no coordinates for address: " + requestText);
        }
        final GeocodifyGeocodingFeature feature = featureCollection.features().getFirst();
        if (feature.geometry() == null || feature.geometry().coordinates() == null
                || feature.geometry().coordinates().size() < 2) {
            throw new CoordinatesTechnicalException("Failed to retrieve coordinates from the API.");
        }
        final List<Double> coordinates = feature.geometry().coordinates();
        if (coordinates.get(0) == null || coordinates.get(1) == null) {
            throw new CoordinatesTechnicalException("Failed to retrieve coordinates from the API.");
        }
        return Coordinates.builder()
                .lat(coordinates.get(1))
                .lon(coordinates.get(0))
                .build();
    }
}
