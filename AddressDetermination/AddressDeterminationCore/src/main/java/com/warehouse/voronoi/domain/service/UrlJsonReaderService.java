package com.warehouse.voronoi.domain.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URL;

public interface UrlJsonReaderService {

    JsonNode get(URL url) throws IOException;
}
