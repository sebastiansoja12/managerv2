package com.warehouse.zebra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.client.MockRestServiceServer;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.model.Parcel;
import com.warehouse.commonassets.request.Request;
import com.warehouse.commonassets.request.ReturnRequest;
import com.warehouse.commonassets.response.Response;
import com.warehouse.commonassets.vo.DeviceInformation;
import com.warehouse.commonassets.vo.ProcessReturn;
import com.warehouse.tools.returning.ReturnProperties;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.zebra.infrastructure.adapter.secondary.ReturnServiceAdapter;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = ReturnServiceAdapterIntegrationTest.ReturnServiceAdapterTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReturnServiceAdapterIntegrationTest {

    @ComponentScan(basePackages = { "com.warehouse.zebra", "com.warehouse.tools.returning",
            "com.warehouse.tools.routelog" })
    @EntityScan(basePackages = { "com.warehouse.zebra", "com.warehouse.tools.returning",
            "com.warehouse.tools.routelog" })
    @EnableJpaRepositories(basePackages = { "com.warehouse.zebra", "com.warehouse.tools.returning",
            "com.warehouse.tools.routelog" })
    @PropertySource("classpath:application.properties")
    static class ReturnServiceAdapterTestConfiguration {

        @MockBean
        public RouteTrackerLogProperties routeTrackerLogProperties;

    }

    @Autowired
    private ReturnServiceAdapter returnServiceAdapter;

    @Autowired
    private ReturnProperties returnProperties;
    
    private MockRestServiceServer mockRestServiceServer;

    private final DeviceInformation deviceInformation = new DeviceInformation("1.0", 1L, "s-soja", "KT1");

    private final String URL = "http://localhost:8080/v2/api/%s";

    private final String ENDPOINT = "returns";

    private final String PROCESS_RETURN_RESPONSE_PATH = "/returnintegration/returnResponse.json";
    private final String UPDATE_PROCESS_RETURN_RESPONSE_PATH = "/returnintegration/updateReturnResponse.json";

    @BeforeEach
    void setup() {
        mockRestServiceServer = MockRestServiceServer.createServer(returnServiceAdapter);
    }
    
    @Test
    void shouldProcessTheReturnRequest() {
        // given
        final Parcel parcel = new Parcel(1L, Size.TEST, "KT1", ParcelStatus.RETURN, ShipmentType.PARENT,
                null);
        final ReturnRequest returnRequest =  new ReturnRequest(parcel, null, ReturnStatus.PROCESSING, "123",
                "s-soja");

        final Request request = new Request(ProcessType.RETURN,
                deviceInformation,
                Collections.singletonList(returnRequest),
                Collections.emptyList());

        final Resource resource = new ClassPathResource(PROCESS_RETURN_RESPONSE_PATH);
        mockRestServiceServer.expect(requestTo(String.format(URL, ENDPOINT)))
                .andRespond(withSuccess(resource, MediaType.APPLICATION_JSON));
        // when
        final Response response = returnServiceAdapter.processReturn(request);
        // then
        assertNotNull(response.getProcessReturns());
        assertThat(response.getProcessReturns())
                .extracting(ProcessReturn::processStatus, ProcessReturn::returnId)
                .containsExactly(tuple("PROCESSING", 1L), tuple( "PROCESSING", 2L));

        mockRestServiceServer.verify();
    }

    @Test
    void shouldUpdateTheReturnRequestProcess() {
        // given
        final Parcel parcel = new Parcel(1L, Size.TEST,
                "KT1",
                ParcelStatus.RETURN,
                ShipmentType.PARENT,
                null);
        final ReturnRequest returnRequest =  new ReturnRequest(parcel,
                null,
                ReturnStatus.PROCESSING,
                "123",
                "s-soja");

        final Request request = new Request(ProcessType.RETURN,
                deviceInformation,
                Collections.singletonList(returnRequest),
                Collections.emptyList());

        final Resource resource = new ClassPathResource(UPDATE_PROCESS_RETURN_RESPONSE_PATH);
        mockRestServiceServer.expect(requestTo(String.format(URL, ENDPOINT)))
                .andRespond(withSuccess(resource, MediaType.APPLICATION_JSON));
        // when
        final Response response = returnServiceAdapter.processReturn(request);
        // then
        assertNotNull(response.getProcessReturns());
        assertThat(response.getProcessReturns())
                .extracting(ProcessReturn::processStatus, ProcessReturn::returnId)
                .containsExactly(tuple("COMPLETED", 1L), tuple( "COMPLETED", 2L));

        mockRestServiceServer.verify();
    }
}
