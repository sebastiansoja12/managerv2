package com.warehouse.geocoding.infrastructure.adapter.secondary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.geocoding.domain.vo.Coordinates;
import com.warehouse.geocoding.domain.vo.GeocodingAddress;
import com.warehouse.geocoding.domain.vo.GeocodingConfig;
import com.warehouse.geocoding.infrastructure.adapter.secondary.exception.CoordinatesTechnicalException;

class GeolocationGeocodifyClientServiceAdapterTest {

    private static final GeocodingAddress ADDRESS =
            new GeocodingAddress("Berlin", "Pappelallee 78/79", "10437");
    private static final GeocodingConfig CONFIG =
            new GeocodingConfig(GeocodingProvider.GEOCODIFY, "geocodify-key",
                    "https://api.geocodify.com/v2/geocode", null, null);

    private GeolocationGeocodifyClientServiceAdapter adapter;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        final RestTemplate restTemplate = new RestTemplate();
        adapter = new GeolocationGeocodifyClientServiceAdapter();
        adapter.setRestTemplate(restTemplate);
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldHandleGeocodifyProvider() {
        assertTrue(adapter.canHandle(GeocodingProvider.GEOCODIFY));
    }

    @Test
    void shouldNotHandleOpenCageProvider() {
        assertFalse(adapter.canHandle(GeocodingProvider.OPEN_CAGE));
    }

    @Test
    void shouldMapCoordinatesFromGeocodifyFeatures() {
        expectGeocodifyRequest("""
                {
                  "meta": {
                    "code": 200
                  },
                  "response": {
                    "type": "FeatureCollection",
                    "features": [
                      {
                        "type": "Feature",
                        "geometry": {
                          "type": "Point",
                          "coordinates": [13.4142133, 52.5432379]
                        },
                        "properties": {
                          "label": "Pappelallee 78/79, Berlin, Germany",
                          "confidence": 1
                        }
                      }
                    ]
                  }
                }
                """);

        final Coordinates coordinates = adapter.obtainCoordinates(ADDRESS, CONFIG);

        assertEquals(52.5432379, coordinates.lat());
        assertEquals(13.4142133, coordinates.lon());
        server.verify();
    }

    @Test
    void shouldThrowWhenGeocodifyReturnsErrorMeta() {
        expectGeocodifyRequest("""
                {
                  "meta": {
                    "code": 401,
                    "error_type": "auth failed",
                    "error_detail": "Missing or invalid api credentials."
                  },
                  "response": []
                }
                """);

        final CoordinatesTechnicalException exception = assertThrows(CoordinatesTechnicalException.class,
                () -> adapter.obtainCoordinates(ADDRESS, CONFIG));

        assertEquals("Geocodify geocoding failed: Missing or invalid api credentials.", exception.getMessage());
        server.verify();
    }

    @Test
    void shouldThrowWhenGeocodifyReturnsNoFeatures() {
        expectGeocodifyRequest("""
                {
                  "meta": {
                    "code": 200
                  },
                  "response": {
                    "type": "FeatureCollection",
                    "features": []
                  }
                }
                """);

        final CoordinatesTechnicalException exception = assertThrows(CoordinatesTechnicalException.class,
                () -> adapter.obtainCoordinates(ADDRESS, CONFIG));

        assertEquals("Geocodify returned no coordinates for address: Pappelallee 78/79, 10437, Berlin",
                exception.getMessage());
        server.verify();
    }

    @Test
    void shouldThrowWhenGeocodifyFeatureHasMissingGeometry() {
        expectGeocodifyRequest("""
                {
                  "meta": {
                    "code": 200
                  },
                  "response": {
                    "type": "FeatureCollection",
                    "features": [
                      {
                        "type": "Feature",
                        "properties": {
                          "label": "Berlin, Germany"
                        }
                      }
                    ]
                  }
                }
                """);

        final CoordinatesTechnicalException exception = assertThrows(CoordinatesTechnicalException.class,
                () -> adapter.obtainCoordinates(ADDRESS, CONFIG));

        assertEquals("Failed to retrieve coordinates from the API.", exception.getMessage());
        server.verify();
    }

    private void expectGeocodifyRequest(final String response) {
        server.expect(once(), request -> {
                    assertEquals("api.geocodify.com", request.getURI().getHost());
                    assertEquals("/v2/geocode", request.getURI().getPath());
                    assertFalse(request.getURI().getRawQuery().contains(" "));

                    final String decodedQuery =
                            URLDecoder.decode(request.getURI().getRawQuery(), StandardCharsets.UTF_8);
                    assertTrue(decodedQuery.contains("q=Pappelallee 78/79, 10437, Berlin"));
                    assertTrue(decodedQuery.contains("api_key=geocodify-key"));
                })
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
    }
}
