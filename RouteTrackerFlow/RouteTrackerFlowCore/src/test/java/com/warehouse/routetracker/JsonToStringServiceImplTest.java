package com.warehouse.routetracker;

import com.warehouse.routetracker.domain.service.JsonToStringServiceImpl;
import com.warehouse.routetracker.domain.vo.ReturnTrackRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonToStringServiceImplTest {

    private final JsonToStringServiceImpl jsonToStringService = new JsonToStringServiceImpl();

    @Test
    void shouldMapToString() {
        // given
        final ReturnTrackRequest request = new ReturnTrackRequest();
        // when
        final String mappedJsonString = jsonToStringService.convertToString(request);
        // then
        assertNotNull(mappedJsonString);
    }
}
