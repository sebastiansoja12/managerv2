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

class GeolocationOpenCageClientServiceAdapterTest {

    private static final GeocodingAddress ADDRESS =
            new GeocodingAddress("Berlin", "Pappelallee 78/79", "10437");
    private static final GeocodingConfig CONFIG =
            new GeocodingConfig(GeocodingProvider.OPEN_CAGE, "opencage-key",
                    "https://api.opencagedata.com/geocode/v1/json", null, null);

    private GeolocationOpenCageClientServiceAdapter adapter;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        final RestTemplate restTemplate = new RestTemplate();
        adapter = new GeolocationOpenCageClientServiceAdapter();
        adapter.setRestTemplate(restTemplate);
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldHandleOpenCageProvider() {
        assertTrue(adapter.canHandle(GeocodingProvider.OPEN_CAGE));
    }

    @Test
    void shouldNotHandleGeoapifyProvider() {
        assertFalse(adapter.canHandle(GeocodingProvider.GEOAPIFY));
    }

    @Test
    void shouldMapCoordinatesFromOpenCageResults() {
        expectOpenCageRequest("""
                {
                  "results": [
                    {
                      "confidence": 10,
                      "formatted": "Office Club, Pappelallee 78/79, 10437 Berlin, Germany",
                      "geometry": {
                        "lat": 52.5432379,
                        "lng": 13.4142133
                      }
                    }
                  ],
                  "status": {
                    "code": 200,
                    "message": "OK"
                  },
                  "total_results": 1
                }
                """);

        final Coordinates coordinates = adapter.obtainCoordinates(ADDRESS, CONFIG);

        assertEquals(52.5432379, coordinates.lat());
        assertEquals(13.4142133, coordinates.lon());
        server.verify();
    }

    @Test
    void shouldThrowWhenOpenCageReturnsErrorStatus() {
        expectOpenCageRequest("""
                {
                  "results": [],
                  "status": {
                    "code": 401,
                    "message": "invalid API key"
                  }
                }
                """);

        final CoordinatesTechnicalException exception = assertThrows(CoordinatesTechnicalException.class,
                () -> adapter.obtainCoordinates(ADDRESS, CONFIG));

        assertEquals("OpenCage geocoding failed: invalid API key", exception.getMessage());
        server.verify();
    }

    @Test
    void shouldThrowWhenOpenCageReturnsNoResults() {
        expectOpenCageRequest("""
                {
                  "results": [],
                  "status": {
                    "code": 200,
                    "message": "OK"
                  },
                  "total_results": 0
                }
                """);

        final CoordinatesTechnicalException exception = assertThrows(CoordinatesTechnicalException.class,
                () -> adapter.obtainCoordinates(ADDRESS, CONFIG));

        assertEquals("OpenCage returned no coordinates for address: Pappelallee 78/79, 10437, Berlin",
                exception.getMessage());
        server.verify();
    }

    @Test
    void shouldThrowWhenOpenCageResultHasMissingGeometry() {
        expectOpenCageRequest("""
                {
                  "results": [
                    {
                      "formatted": "Berlin, Germany"
                    }
                  ],
                  "status": {
                    "code": 200,
                    "message": "OK"
                  }
                }
                """);

        final CoordinatesTechnicalException exception = assertThrows(CoordinatesTechnicalException.class,
                () -> adapter.obtainCoordinates(ADDRESS, CONFIG));

        assertEquals("Failed to retrieve coordinates from the API.", exception.getMessage());
        server.verify();
    }

    private void expectOpenCageRequest(final String response) {
        server.expect(once(), request -> {
                    assertEquals("api.opencagedata.com", request.getURI().getHost());
                    assertEquals("/geocode/v1/json", request.getURI().getPath());
                    assertFalse(request.getURI().getRawQuery().contains(" "));

                    final String decodedQuery =
                            URLDecoder.decode(request.getURI().getRawQuery(), StandardCharsets.UTF_8);
                    assertTrue(decodedQuery.contains("q=Pappelallee 78/79, 10437, Berlin"));
                    assertTrue(decodedQuery.contains("key=opencage-key"));
                    assertTrue(decodedQuery.contains("limit=1"));
                    assertTrue(decodedQuery.contains("no_annotations=1"));
                })
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
    }
}
