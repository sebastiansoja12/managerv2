package com.warehouse.geocoding.infrastructure.adapter.secondary.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.json.JsonMapper;

class GeocodifyGeocodingResponseTest {

    @Test
    void shouldMapGeocodifyGeocodingResponse() throws Exception {
        final String response = """
                {
                  "meta": {
                    "code": 200
                  },
                  "response": {
                    "geocoding": {
                      "version": "0.2",
                      "query": {
                        "text": "Jugoslowianska 44A, 60-149, Poznan"
                      }
                    },
                    "type": "FeatureCollection",
                    "features": [
                      {
                        "type": "Feature",
                        "geometry": {
                          "type": "Point",
                          "coordinates": [16.8655817, 52.3910306]
                        },
                        "properties": {
                          "id": "pl/wielkopolskie:5a2c1f",
                          "gid": "openaddresses:address:pl/wielkopolskie:5a2c1f",
                          "layer": "address",
                          "source": "openaddresses",
                          "name": "Jugoslowianska 44a",
                          "housenumber": "44a",
                          "street": "Jugoslowianska",
                          "postalcode": "60-149",
                          "confidence": 1,
                          "match_type": "exact",
                          "accuracy": "point",
                          "country": "Poland",
                          "country_code": "PL",
                          "region": "Greater Poland",
                          "locality": "Poznan",
                          "label": "Jugoslowianska 44a, Poznan, Poland"
                        }
                      }
                    ],
                    "bbox": [16.8655817, 52.3910306, 16.8655817, 52.3910306]
                  }
                }
                """;

        final GeocodifyGeocodingResponse geocodingResponse =
                JsonMapper.builder().configureForJackson2().build().readValue(response, GeocodifyGeocodingResponse.class);
        final GeocodifyGeocodingFeature feature = geocodingResponse.response().features().get(0);

        assertEquals(200, geocodingResponse.meta().code());
        assertEquals("FeatureCollection", geocodingResponse.response().type());
        assertEquals(List.of(16.8655817, 52.3910306), feature.geometry().coordinates());
        assertEquals("Point", feature.geometry().type());
        assertEquals("PL", feature.properties().countryCode());
        assertEquals("exact", feature.properties().matchType());
        assertEquals(1.0, feature.properties().confidence());
        assertEquals("Jugoslowianska 44a, Poznan, Poland", feature.properties().label());
    }

    @Test
    void shouldMapGeocodifyErrorResponseWithEmptyResponseArray() throws Exception {
        final String response = """
                {
                  "meta": {
                    "code": 401,
                    "error_type": "auth failed",
                    "error_detail": "Missing or invalid api credentials."
                  },
                  "response": []
                }
                """;

        final GeocodifyGeocodingResponse geocodingResponse =
                JsonMapper.builder().configureForJackson2().build().readValue(response, GeocodifyGeocodingResponse.class);

        assertEquals(401, geocodingResponse.meta().code());
        assertEquals("auth failed", geocodingResponse.meta().errorType());
        assertEquals("Missing or invalid api credentials.", geocodingResponse.meta().errorDetail());
        assertNotNull(geocodingResponse.response());
        assertTrue(geocodingResponse.response().features().isEmpty());
    }
}
