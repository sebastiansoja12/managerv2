package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.CountryBoundaryServicePort;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaBoundary;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.GeoPoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

public class CountryBoundaryServiceAdapter implements CountryBoundaryServicePort {

    private static final String RESOURCE = "/deliveryarea/poland-boundary.json";

    private final DeliveryAreaBoundary countryBoundary;

    public CountryBoundaryServiceAdapter(final ObjectMapper objectMapper) {
        try (InputStream input = CountryBoundaryServiceAdapter.class.getResourceAsStream(RESOURCE)) {
            if (input == null) {
                throw new IllegalStateException("Missing country boundary resource " + RESOURCE);
            }
            final List<GeoPoint> ring = new ArrayList<>();
            for (final JsonNode coordinate : objectMapper.readTree(input).path("coordinates")) {
                ring.add(new GeoPoint(coordinate.get(1).asDouble(), coordinate.get(0).asDouble()));
            }
            this.countryBoundary = DeliveryAreaBoundary.closedRing(ring);
        } catch (final IOException exception) {
            throw new UncheckedIOException("Unable to read country boundary", exception);
        }
    }

    @Override
    public DeliveryAreaBoundary countryBoundary() {
        return countryBoundary;
    }
}
