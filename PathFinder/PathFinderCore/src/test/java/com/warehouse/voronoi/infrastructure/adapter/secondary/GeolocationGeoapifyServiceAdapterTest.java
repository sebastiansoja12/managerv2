package com.warehouse.voronoi.infrastructure.adapter.secondary;

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
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.vo.GeocodingAddress;
import com.warehouse.voronoi.domain.vo.GeocodingConfig;
import com.warehouse.voronoi.infrastructure.adapter.secondary.exception.CoordinatesTechnicalException;

class GeolocationGeoapifyServiceAdapterTest {

    private static final GeocodingAddress ADDRESS =
            new GeocodingAddress("Poznań", "Jugosłowiańska 44A", "60-149");
    private static final GeocodingConfig CONFIG =
            new GeocodingConfig(GeocodingProvider.GEOAPIFY, "geoapify-key",
                    "https://api.geoapify.com/v1/geocode/search", null, null);

    private GeolocationGeoapifyServiceAdapter adapter;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        final RestTemplate restTemplate = new RestTemplate();
        adapter = new GeolocationGeoapifyServiceAdapter();
        adapter.setRestTemplate(restTemplate);
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldHandleGeoapifyProvider() {
        assertTrue(adapter.canHandle(GeocodingProvider.GEOAPIFY));
    }

    @Test
    void shouldNotHandlePositionStackProvider() {
        assertFalse(adapter.canHandle(GeocodingProvider.POSITION_STACK));
    }

    @Test
    void shouldSendEncodedGeoapifyRequestWithTextFormatLimitAndApiKey() {
        expectGeoapifyRequest(resultResponse());

        adapter.obtainCoordinates(ADDRESS, CONFIG);

        server.verify();
    }

    @Test
    void shouldMapCoordinatesFromGeoapifyResults() {
        expectGeoapifyRequest(resultResponse());

        final Coordinates coordinates = adapter.obtainCoordinates(ADDRESS, CONFIG);

        assertEquals(52.3910306, coordinates.lat());
        assertEquals(16.8655817, coordinates.lon());
        server.verify();
    }

    @Test
    void shouldMapCoordinatesFromGeoapifyFeatureProperties() {
        expectGeoapifyRequest("""
                {
                  "features": [
                    {
                      "properties": {
                        "lat": 52.3910306,
                        "lon": 16.8655817
                      }
                    }
                  ]
                }
                """);

        final Coordinates coordinates = adapter.obtainCoordinates(ADDRESS, CONFIG);

        assertEquals(52.3910306, coordinates.lat());
        assertEquals(16.8655817, coordinates.lon());
        server.verify();
    }

    @Test
    void shouldMapCoordinatesFromGeoapifyFeatureGeometry() {
        expectGeoapifyRequest("""
                {
                  "features": [
                    {
                      "geometry": {
                        "type": "Point",
                        "coordinates": [16.8655817, 52.3910306]
                      }
                    }
                  ]
                }
                """);

        final Coordinates coordinates = adapter.obtainCoordinates(ADDRESS, CONFIG);

        assertEquals(52.3910306, coordinates.lat());
        assertEquals(16.8655817, coordinates.lon());
        server.verify();
    }

    @Test
    void shouldThrowWhenGeoapifyReturnsErrorResponse() {
        expectGeoapifyRequest("""
                {
                  "statusCode": 401,
                  "error": "Unauthorized",
                  "message": "Invalid API key"
                }
                """);

        final CoordinatesTechnicalException exception = assertThrows(CoordinatesTechnicalException.class,
                () -> adapter.obtainCoordinates(ADDRESS, CONFIG));

        assertEquals("Geoapify geocoding failed: Invalid API key", exception.getMessage());
        server.verify();
    }

    @Test
    void shouldThrowWhenGeoapifyReturnsNoCoordinates() {
        expectGeoapifyRequest("""
                {
                  "results": [],
                  "features": []
                }
                """);

        final CoordinatesTechnicalException exception = assertThrows(CoordinatesTechnicalException.class,
                () -> adapter.obtainCoordinates(ADDRESS, CONFIG));

        assertEquals("Geoapify returned no coordinates for address: Jugosłowiańska 44A, 60-149, Poznań",
                exception.getMessage());
        server.verify();
    }

    @Test
    void shouldThrowWhenGeoapifyResultHasMissingLatitude() {
        expectGeoapifyRequest("""
                {
                  "results": [
                    {
                      "lon": 16.8655817
                    }
                  ]
                }
                """);

        final CoordinatesTechnicalException exception = assertThrows(CoordinatesTechnicalException.class,
                () -> adapter.obtainCoordinates(ADDRESS, CONFIG));

        assertEquals("Failed to retrieve coordinates from the API.", exception.getMessage());
        server.verify();
    }

    private void expectGeoapifyRequest(final String response) {
        server.expect(once(), request -> {
                    assertEquals("api.geoapify.com", request.getURI().getHost());
                    assertEquals("/v1/geocode/search", request.getURI().getPath());
                    assertFalse(request.getURI().getRawQuery().contains(" "));

                    final String decodedQuery =
                            URLDecoder.decode(request.getURI().getRawQuery(), StandardCharsets.UTF_8);
                    assertTrue(decodedQuery.contains("text=Jugosłowiańska 44A, 60-149, Poznań"));
                    assertTrue(decodedQuery.contains("format=json"));
                    assertTrue(decodedQuery.contains("limit=1"));
                    assertTrue(decodedQuery.contains("apiKey=geoapify-key"));
                })
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
    }

    private String resultResponse() {
        return """
                {
                  "results": [
                    {
                      "country": "Poland",
                      "country_code": "pl",
                      "city": "Poznan",
                      "postcode": "60-149",
                      "street": "Jugosłowiańska",
                      "housenumber": "44a",
                      "lon": 16.8655817,
                      "lat": 52.3910306,
                      "result_type": "building",
                      "formatted": "Jugosłowiańska 44a, 60-149 Poznan, Poland"
                    }
                  ],
                  "query": {
                    "text": "Jugosłowiańska 44A, 60-149, Poznań"
                  }
                }
                """;
    }
}
