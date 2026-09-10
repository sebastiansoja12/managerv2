package com.warehouse.departmentmap.infrastructure.adapter.primary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.warehouse.departmentmap.InMemoryDepartmentMapStore;
import com.warehouse.departmentmap.application.port.primary.DepartmentMapPortImpl;
import com.warehouse.departmentmap.application.port.primary.PalletLocationPortImpl;
import com.warehouse.departmentmap.domain.service.StorageLocationSynchronizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;
import java.util.UUID;

import static com.warehouse.departmentmap.DepartmentMapFixture.DEPARTMENT_ID;
import static com.warehouse.departmentmap.DepartmentMapFixture.OPERATOR_ID;
import static com.warehouse.departmentmap.DepartmentMapFixture.USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class DepartmentMapControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        final InMemoryDepartmentMapStore store = new InMemoryDepartmentMapStore(Set.of(DEPARTMENT_ID), OPERATOR_ID,
                USER_ID);
        final DepartmentMapController controller = new DepartmentMapController(
                new DepartmentMapPortImpl(store, store, store, store, new StorageLocationSynchronizer()),
                new PalletLocationPortImpl(store, store, store, store));
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new DepartmentMapExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void shouldCreateMapSaveLayoutAndPlacePallet() throws Exception {
        final JsonNode created = json(mockMvc.perform(post("/department-maps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"departmentId\":100,\"name\":\"Hall A\",\"width\":40,\"height\":20}"))
                .andReturn(), 201);
        final String mapId = created.get("mapId").asText();
        final String locationId = UUID.randomUUID().toString();

        final JsonNode saved = json(mockMvc.perform(put("/department-maps/" + mapId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Hall A\",\"width\":40,\"height\":20,\"gridSize\":0.5,\"version\":0,"
                                + "\"elements\":[{\"elementId\":\"" + locationId + "\",\"type\":\"storage_location\","
                                + "\"x\":1,\"y\":1,\"width\":1.2,\"height\":0.8,\"rotation\":90,"
                                + "\"locationCode\":\"a-01\",\"capacity\":1},"
                                + "{\"type\":\"sorter\",\"label\":\"Sorter\",\"x\":5,\"y\":10,\"width\":20,"
                                + "\"height\":1.2,\"rotation\":0}]}"))
                .andReturn(), 200);
        final JsonNode placed = json(mockMvc.perform(post("/department-maps/locations/" + locationId + "/pallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"palletTrackingNumber\":\"pal000000000001\"}"))
                .andReturn(), 201);
        final JsonNode map = json(mockMvc.perform(get("/department-maps/" + mapId)).andReturn(), 200);

        assertEquals(1, saved.get("version").asLong());
        assertEquals("A-01", saved.get("elements").get(0).get("locationCode").asText());
        assertEquals("SORTER", saved.get("elements").get(1).get("type").asText());
        assertEquals("PAL000000000001", placed.get("palletTrackingNumber").asText());
        assertEquals("A-01", placed.get("locationCode").asText());
        assertEquals("OCCUPIED", map.get("locations").get(0).get("status").asText());
        assertEquals(100, map.get("departmentId").asLong());
        assertEquals(1, json(mockMvc.perform(get("/department-maps").param("departmentId", "100")).andReturn(), 200)
                .size());
    }

    @Test
    void shouldRejectInvalidElementType() throws Exception {
        final String mapId = json(mockMvc.perform(post("/department-maps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"departmentId\":100,\"name\":\"Hall A\",\"width\":40,\"height\":20}"))
                .andReturn(), 201).get("mapId").asText();

        final JsonNode problem = json(mockMvc.perform(put("/department-maps/" + mapId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Hall A\",\"width\":40,\"height\":20,\"version\":0,"
                        + "\"elements\":[{\"type\":\"DOOR\",\"x\":1,\"y\":1,\"width\":1,\"height\":1}]}"))
                .andReturn(), 400);

        assertEquals("DEPARTMENT_MAP_INVALID", problem.get("properties").get("code").asText());
    }

    @Test
    void shouldRejectMalformedLocationId() throws Exception {
        final JsonNode problem = json(mockMvc.perform(post("/department-maps/locations/not-a-uuid/pallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"palletTrackingNumber\":\"PAL1\"}"))
                .andReturn(), 400);

        assertEquals("DEPARTMENT_MAP_INVALID", problem.get("properties").get("code").asText());
    }

    private JsonNode json(final MvcResult result, final int expectedStatus) throws Exception {
        assertEquals(expectedStatus, result.getResponse().getStatus(), result.getResponse().getContentAsString());
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }
}
