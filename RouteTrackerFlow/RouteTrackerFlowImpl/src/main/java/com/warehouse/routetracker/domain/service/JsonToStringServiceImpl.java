package com.warehouse.routetracker.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonToStringServiceImpl implements JsonToStringService {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToString(Object t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
