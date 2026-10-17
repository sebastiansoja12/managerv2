package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.returning.api.dto.ReturnDetailsDto;
import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.infrastructure.dto.CurrentUserAuthenticationDto;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.application.port.secondary.DepartmentServicePort;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnDetailsMapper;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnPackageOutboundMapper;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnPackageResponseMapper;
import com.warehouse.tools.returning.ReturnProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class ReturningTrackManagerServiceClientAdapterTest {
    private final DepartmentServicePort departments = mock(DepartmentServicePort.class);
    private MockRestServiceServer server;
    private ReturningTrackManagerServiceClientAdapter client;

    @BeforeEach
    void setUp() {
        final RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        final ReturnProperties properties = new ReturnProperties();
        properties.setUrl("http://rtm");
        properties.setEndpoint("/returns");
        final CurrentUserApiService user = mock(CurrentUserApiService.class);
        when(user.getCurrentUserAuthentication()).thenReturn(new CurrentUserAuthenticationDto("test-token", null));
        client = new ReturningTrackManagerServiceClientAdapter(builder, properties, user,
                new ReturnPackageOutboundMapper(), new ReturnPackageResponseMapper(), new ReturnDetailsMapper(departments));
    }

    @Test
    void shouldProcessCompleteAndCancelTheExactReturnPackage() {
        server.expect(requestTo("http://rtm/returns/123/process")).andExpect(method(HttpMethod.PUT))
                .andExpect(header("Authorization", "Bearer test-token")).andRespond(withNoContent());
        server.expect(requestTo("http://rtm/returns/123/complete")).andExpect(method(HttpMethod.PUT)).andRespond(withNoContent());
        server.expect(requestTo("http://rtm/returns/123")).andExpect(method(HttpMethod.DELETE)).andRespond(withNoContent());

        client.startProcessing(new ReturnPackageId(123L));
        client.complete(new ReturnPackageId(123L));
        client.cancel(new ReturnPackageId(123L));

        server.verify();
    }

    @Test
    void shouldReadReturnDetailsAndResolveDepartmentCodes() {
        when(departments.getCode(new DepartmentId(3L))).thenReturn("KT1");
        server.expect(requestTo("http://rtm/returns/123")).andRespond(withSuccess("""
                {"returnPackageId":{"value":123},"shipmentId":{"value":456},"reason":"Damaged",
                 "returnStatus":"CREATED","returnToken":{"value":"TOKEN"},
                 "assignedDepartmentId":{"value":3},"reasonCode":{"value":"DAMAGED"}}
                """, MediaType.APPLICATION_JSON));

        final ReturnDetailsDto details = client.getDetails(new ReturnPackageId(123L));

        assertEquals(123L, details.returnPackageId().value());
        assertEquals(456L, details.shipmentId().value());
        assertEquals("KT1", details.assignedDepartmentCode().value());
        assertEquals("DAMAGED", details.reasonCode().value());
        assertNull(details.returnedDepartmentCode());
        server.verify();
    }

    @Test
    void shouldHandleShipmentWithoutReturn() {
        server.expect(requestTo("http://rtm/returns/shipment/456")).andRespond(withNoContent());

        assertTrue(client.findByShipmentId(new ShipmentId(456L)).isEmpty());
        server.verify();
    }
}
