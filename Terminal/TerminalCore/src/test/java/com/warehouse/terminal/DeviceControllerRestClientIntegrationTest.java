package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.configuration.DeviceControllerRestClientTestConfiguration;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.exception.DeviceNotFoundException;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.command.*;
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.port.primary.DevicePort;
import com.warehouse.terminal.domain.vo.DeviceCreateResult;

@SpringBootTest(
        classes = DeviceControllerRestClientTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc(addFilters = false)
class DeviceControllerRestClientIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DevicePort devicePort;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        reset(devicePort);
        this.restClient = RestClient.builder()
                .requestFactory(new MockMvcClientHttpRequestFactory(mockMvc))
                .baseUrl("http://localhost")
                .build();
    }

    @Test
    void shouldGetDeviceById() {
        final String deviceId = "TL:dc-001";
        when(devicePort.getDevice(new DeviceId(deviceId))).thenReturn(terminal(deviceId, DeviceStatus.ACTIVE, 1001L, "KT1"));

        final ResponseEntity<Map<String, Object>> response = restClient
                .get()
                .uri("/devices/{id}", deviceId)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());

        final Map<String, Object> body = response.getBody();
        final Map<String, Object> id = castMap(body.get("deviceId"));
        final Map<String, Object> username = castMap(body.get("username"));
        final Map<String, Object> depotCode = castMap(body.get("depotCode"));

        assertEquals(deviceId, id.get("value"));
        assertEquals("TERMINAL", body.get("deviceType"));
        assertEquals("1001", username.get("value"));
        assertEquals("KT1", depotCode.get("value"));
        assertEquals(Boolean.TRUE, body.get("active"));
    }

    @Test
    void shouldReturnNotFoundWhenDeviceDoesNotExist() throws Exception {
        final String deviceId = "TL:dc-missing";
        when(devicePort.getDevice(any())).thenThrow(DeviceNotFoundException.forDeviceId(new DeviceId(deviceId)));

        final HttpClientErrorException.NotFound ex = org.junit.jupiter.api.Assertions.assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> restClient
                        .get()
                        .uri("/devices/{id}", deviceId)
                        .retrieve()
                        .toBodilessEntity()
        );

        final Map<String, Object> body = objectMapper.readValue(ex.getResponseBodyAsString(), new TypeReference<>() {
        });

        assertEquals(404, ex.getStatusCode().value());
        assertEquals("Device Not Found", body.get("title"));
        assertEquals("https://warehouse.dev/problems/device-not-found", body.get("type"));
    }

    @Test
    void shouldGetAllDevices() {
        final Device terminal = terminal("TL:dc-101", DeviceStatus.ACTIVE, 1010L, "KT1");
        final Device mobile = mobile("MB:dc-102", DeviceStatus.ACTIVE, 1020L, "KT2");
        final Device scanner = scanner("SC:dc-103", DeviceStatus.BLOCKED, 1030L, "KT3");

        when(devicePort.allDevices()).thenReturn(List.of(terminal, mobile, scanner));

        final ResponseEntity<List<Map<String, Object>>> response = restClient
                .get()
                .uri("/devices")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());

        final Map<String, Object> scannerPayload = response.getBody()
                .stream()
                .filter(it -> "SCANNER".equals(it.get("deviceType")))
                .findFirst()
                .orElseThrow();

        assertEquals(Boolean.FALSE, scannerPayload.get("active"));
    }

    @Test
    void shouldUpdateStatus() {
        final String deviceId = "SC:dc-200";

        final ResponseEntity<Void> response = restClient
                .put()
                .uri("/devices/{id}/statuses?value=LOST", deviceId)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

        final org.mockito.ArgumentCaptor<DeviceStatusUpdateCommand> captor = org.mockito.ArgumentCaptor.forClass(DeviceStatusUpdateCommand.class);
        verify(devicePort).updateStatusField(captor.capture());

        assertEquals(deviceId, captor.getValue().deviceId().value());
        assertEquals(DeviceStatus.LOST, captor.getValue().status());
    }

    @Test
    void shouldUpdateVersion() {
        final String deviceId = "MB:dc-210";

        final ResponseEntity<Void> response = restClient
                .put()
                .uri("/devices/{id}/versions?value=3.4.5", deviceId)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

        final org.mockito.ArgumentCaptor<DeviceVersionUpdateCommand> captor = org.mockito.ArgumentCaptor.forClass(DeviceVersionUpdateCommand.class);
        verify(devicePort).updateVersionField(captor.capture());

        assertEquals(deviceId, captor.getValue().deviceId().value());
        assertEquals("3.4.5", captor.getValue().version());
    }

    @Test
    void shouldUpdateIdentity() {
        final String deviceId = "TL:dc-220";
        final Map<String, Object> identity = new LinkedHashMap<>();
        identity.put("serialNumber", "SN-220");

        final ResponseEntity<Void> response = restClient
                .put()
                .uri("/devices/{id}/identities", deviceId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(identity)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

        final org.mockito.ArgumentCaptor<DeviceIdentityUpdateCommand> captor = org.mockito.ArgumentCaptor.forClass(DeviceIdentityUpdateCommand.class);
        verify(devicePort).updateIdentityField(captor.capture());

        assertEquals(deviceId, captor.getValue().deviceId().value());
        assertEquals("SN-220", captor.getValue().identity().getSerialNumber());
    }

    @Test
    void shouldCreateDeviceForScanner() {
        when(devicePort.create(any())).thenReturn(new DeviceCreateResult(new DeviceId("SC:created-001")));

        final Map<String, Object> request = validCreateRequest("SCANNER");
        request.put("scanType", "BARCODE");
        request.put("scannerType", "HANDHELD");

        final ResponseEntity<Void> response = restClient
                .post()
                .uri("/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

        final org.mockito.ArgumentCaptor<DeviceCreateCommand> captor = org.mockito.ArgumentCaptor.forClass(DeviceCreateCommand.class);
        verify(devicePort).create(captor.capture());

        assertEquals(DeviceType.SCANNER, captor.getValue().getDeviceType());
        assertEquals(1001L, captor.getValue().getUserId().value());
        assertEquals("KT1", captor.getValue().getDepartmentCode().value());
        assertEquals(Scanner.ScanType.BARCODE, captor.getValue().getScanType());
        assertEquals(Scanner.ScannerType.HANDHELD, captor.getValue().getScannerType());
    }

    @Test
    void shouldReturnBadRequestWhenCreatePayloadIsInvalid() throws Exception {
        final Map<String, Object> request = validCreateRequest("MOBILE");
        request.put("scanType", "QRCODE");

        final HttpClientErrorException.BadRequest ex = org.junit.jupiter.api.Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restClient
                        .post()
                        .uri("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request)
                        .retrieve()
                        .toBodilessEntity()
        );

        final Map<String, Object> body = objectMapper.readValue(ex.getResponseBodyAsString(), new TypeReference<>() {
        });

        assertEquals(400, ex.getStatusCode().value());
        assertEquals("Invalid Device Create Request", body.get("title"));
        assertEquals("https://warehouse.dev/problems/device-create-request-validation", body.get("type"));
        verify(devicePort, never()).create(any());
    }

    @Test
    void shouldUpdateDeviceProfile() {
        final Map<String, Object> request = new LinkedHashMap<>();
        request.put("deviceId", Map.of("value", "TL:dc-300"));
        request.put("status", "BLOCKED");

        final ResponseEntity<Void> response = restClient
                .put()
                .uri("/devices/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

        final org.mockito.ArgumentCaptor<DeviceUpdateCommand> captor = org.mockito.ArgumentCaptor.forClass(DeviceUpdateCommand.class);
        verify(devicePort).updateDevice(captor.capture());

        assertEquals("TL:dc-300", captor.getValue().deviceId().value());
        assertEquals(DeviceStatus.BLOCKED, captor.getValue().status());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatePayloadHasNoUpdatableFields() throws Exception {
        final Map<String, Object> request = new LinkedHashMap<>();
        request.put("deviceId", Map.of("value", "TL:dc-301"));

        final HttpClientErrorException.BadRequest ex = org.junit.jupiter.api.Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restClient
                        .put()
                        .uri("/devices/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request)
                        .retrieve()
                        .toBodilessEntity()
        );

        final Map<String, Object> body = objectMapper.readValue(ex.getResponseBodyAsString(), new TypeReference<>() {
        });

        assertEquals(400, ex.getStatusCode().value());
        assertEquals("Invalid Device Update Request", body.get("title"));
        assertEquals("https://warehouse.dev/problems/device-update-request-validation", body.get("type"));
        verify(devicePort, never()).updateDevice(any());
    }

    private Terminal terminal(final String deviceId, final DeviceStatus status, final long userId, final String departmentCode) {
        return new Terminal(
                new DeviceId(deviceId),
                null,
                null,
                null,
                null,
                null,
                OwnershipProfile.initializeOwnership("OPERATOR", new UserId(userId), new DepartmentCode(departmentCode), null)
        );
    }

    private Mobile mobile(final String deviceId, final DeviceStatus status, final long userId, final String departmentCode) {
        return new Mobile(
                new DeviceId(deviceId),
                null,
                null,
                null,
                null,
                null,
                OwnershipProfile.initializeOwnership("OPERATOR", new UserId(userId), new DepartmentCode(departmentCode), null)
        );
    }

    private Scanner scanner(final String deviceId, final DeviceStatus status, final long userId, final String departmentCode) {
        return new Scanner(
                new DeviceId(deviceId),
                new ExternalId<>("ext-" + deviceId),
                DeviceType.SCANNER,
                status,
                null,
                null,
                null,
                OwnershipProfile.initializeOwnership("OPERATOR", new UserId(userId), new DepartmentCode(departmentCode), null),
                java.time.Instant.now(),
                java.time.Instant.now(),
                Scanner.ScanType.BARCODE,
                Scanner.ScannerType.HANDHELD
        );
    }

    private Map<String, Object> validCreateRequest(final String deviceType) {
        final Map<String, Object> request = new LinkedHashMap<>();
        request.put("userId", Map.of("value", 1001));
        request.put("departmentCode", Map.of("value", "KT1"));
        request.put("supplierCode", Map.of("value", "SUP-01"));
        request.put("version", Map.of("value", "1.0.0"));
        request.put("deviceUserType", "OPERATOR");
        request.put("deviceType", deviceType);

        request.put("identity", Map.of("serialNumber", "SERIAL-1001"));
        request.put("hardware", Map.of("manufacturer", "Acme"));
        request.put("software", new LinkedHashMap<>());
        request.put("network", Map.of("networkType", "WIFI"));
        request.put("security", Map.of("failedLoginAttempts", 0));
        request.put("location", Map.of("latitude", 52.1, "longitude", 21.0));

        return request;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castMap(final Object value) {
        return (Map<String, Object>) value;
    }
}
