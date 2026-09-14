package com.warehouse.routetracker.domain.service;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonToStringServiceImpl implements JsonToStringService {

    private final JsonMapper mapper = JsonMapper.builder().configureForJackson2().build();

    @Override
    public String convertToString(Object t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (JacksonException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
