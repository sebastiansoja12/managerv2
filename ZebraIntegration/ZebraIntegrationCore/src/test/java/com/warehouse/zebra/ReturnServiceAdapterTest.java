package com.warehouse.zebra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
import com.warehouse.zebra.domain.vo.*;
import com.warehouse.zebra.infrastructure.adapter.secondary.ReturnServiceAdapter;
import com.warehouse.zebra.infrastructure.adapter.secondary.properties.ReturnProperty;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = ReturnServiceAdapterTest.ReturnServiceAdapterTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReturnServiceAdapterTest {

    @ComponentScan(basePackages = { "com.warehouse.zebra" })
    @EntityScan(basePackages = { "com.warehouse.zebra" })
    @EnableJpaRepositories(basePackages = { "com.warehouse.zebra"})
    @PropertySource("classpath:application.properties")
    static class ReturnServiceAdapterTestConfiguration {


    }

    @Autowired
    private ReturnServiceAdapter returnServiceAdapter;

    @Autowired
    private ReturnProperty returnProperty;
    
    private MockRestServiceServer mockRestServiceServer;

    private final DeviceInformation deviceInformation = DeviceInformation.builder()
            .depotCode("KT1")
            .username("s-soja")
            .version("1.0")
            .zebraId(1L).build();

    private final String URL = "http://localhost:8080/v2/api/%s";

    private final String ENDPOINT = "returns";

    private final String PROCESS_RETURN_RESPONSE_PATH = "/returnintegration/returnResponse.json";

    @BeforeEach
    void setup() {
        mockRestServiceServer = MockRestServiceServer.createServer(returnServiceAdapter);
    }
    
    @Test
    void shouldProcessTheReturnRequest() {
        // given
        final ReturnRequest returnRequest = ReturnRequest.builder()
                .returnStatus(ReturnStatus.CREATED)
                .returnToken("returnToken")
                .parcel(Parcel.builder()
                        .destination("KT1")
                        .id(1L)
                        .parcelRelatedId(null)
                        .parcelSize(Size.TEST)
                        .parcelType(ParcelType.PARENT)
                        .build())
                .reason("reason")
                .supplierCode("abc")
                .build();

        final Request request = Request.builder()
                .processType(ProcessType.RETURN)
                .requests(List.of(returnRequest))
                .zebraDeviceInformation(deviceInformation).build();

        final Resource resource = new ClassPathResource(PROCESS_RETURN_RESPONSE_PATH);
        mockRestServiceServer.expect(requestTo(String.format(URL, ENDPOINT)))
                .andRespond(withSuccess(resource, MediaType.APPLICATION_JSON));
        // when
        final Response response = returnServiceAdapter.processReturn(request);
        // then
        assertNotNull(response.processReturns());
        assertThat(response.processReturns())
                .extracting(ProcessReturn::processStatus, ProcessReturn::returnId)
                .containsExactly(tuple("PROCESSING", 1L), tuple( "PROCESSING", 2L));

        mockRestServiceServer.verify();
    }


}
