package com.warehouse.voronoi.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.URL;

/**
 * Requesting API for given destination
 */
@AllArgsConstructor
public class UrlReaderServiceImpl implements UrlJsonReaderService {

    @Override
    public JsonNode get(URL url) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(url);
    }
}
