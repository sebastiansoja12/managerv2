package com.warehouse.voronoi.infrastructure.adapter.secondary.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class GeoapifyGeocodingResponseTest {

    @Test
    void shouldMapGeoapifyGeocodingResponse() throws Exception {
        final String response = """
                {
                  "results": [
                    {
                      "datasource": {
                        "sourcename": "openstreetmap",
                        "attribution": "OpenStreetMap contributors",
                        "license": "Open Database License",
                        "url": "https://www.openstreetmap.org/copyright"
                      },
                      "country": "Poland",
                      "country_code": "pl",
                      "state": "Greater Poland Voivodeship",
                      "city": "Poznan",
                      "postcode": "60-149",
                      "district": "Osiedle Mikolaja Kopernika",
                      "suburb": "Grunwald Poludnie",
                      "street": "Jugoslowianska",
                      "housenumber": "44a",
                      "iso3166_2": "PL-30",
                      "lon": 16.8655817,
                      "lat": 52.3910306,
                      "state_code": "WP",
                      "result_type": "building",
                      "formatted": "Jugoslowianska 44a, 60-149 Poznan, Poland",
                      "address_line1": "Jugoslowianska 44a",
                      "address_line2": "60-149 Poznan, Poland",
                      "timezone": {
                        "name": "Europe/Warsaw",
                        "offset_STD": "+01:00",
                        "offset_DST": "+02:00",
                        "offset_STD_seconds": 3600,
                        "offset_DST_seconds": 7200,
                        "abbreviation_STD": "CET",
                        "abbreviation_DST": "CEST"
                      },
                      "plus_code": "9F4R9VR8+C6",
                      "rank": {
                        "importance": 0.00007645456626225708,
                        "popularity": 8.532886416751557,
                        "confidence": 1,
                        "confidence_city_level": 1,
                        "confidence_street_level": 1,
                        "confidence_building_level": 1,
                        "match_type": "full_match"
                      },
                      "place_id": "511e8425c396dd3040591d5e6b4a0d324a40f00103f901d13e439600000000c00203",
                      "bbox": {
                        "lon1": 16.8655317,
                        "lat1": 52.3909806,
                        "lon2": 16.8656317,
                        "lat2": 52.3910806
                      }
                    }
                  ],
                  "query": {
                    "text": "Jugoslowianska 44A, 60-149, Poznan",
                    "parsed": {
                      "housenumber": "44a",
                      "street": "jugoslowianska",
                      "postcode": "60-149",
                      "city": "poznan",
                      "expected_type": "building"
                    }
                  }
                }
                """;

        final GeoapifyGeocodingResponse geocodingResponse =
                new ObjectMapper().readValue(response, GeoapifyGeocodingResponse.class);
        final GeoapifyGeocodingResult result = geocodingResponse.results().get(0);

        assertFalse(geocodingResponse.results().isEmpty());
        assertEquals(52.3910306, result.lat());
        assertEquals(16.8655817, result.lon());
        assertEquals("pl", result.countryCode());
        assertEquals("building", result.resultType());
        assertEquals("full_match", result.rank().matchType());
        assertEquals("building", geocodingResponse.query().parsed().expectedType());
    }
}
