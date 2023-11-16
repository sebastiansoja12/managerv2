package com.warehouse.deliverytoken;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
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
import com.warehouse.deliverytoken.domain.vo.Parcel;
import com.warehouse.deliverytoken.domain.vo.ParcelId;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.ParcelServiceAdapter;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception.TechnicalException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = ParcelServiceAdapterIntegrationTest.ParcelServiceAdapterIntegrationTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ParcelServiceAdapterIntegrationTest {

    @ComponentScan(basePackages = { "com.warehouse.deliverytoken" })
    @EntityScan(basePackages = { "com.warehouse.deliverytoken" })
    @EnableJpaRepositories(basePackages = { "com.warehouse.deliverytoken"})
    static class ParcelServiceAdapterIntegrationTestConfiguration {

    }

    @Autowired
    private ParcelServiceAdapter parcelServiceAdapter;

    private MockRestServiceServer mockRestServiceServer;


    private final static Long PARCEL_ID = 1L;

    private final static String URL = "http://localhost:8080/v2/api/shipments/%s";

    private static final String PARCEL_RESPONSE_JSON_URL =
            "/parcelserviceadapterintegration/parcel_1_response.json";

    @BeforeEach
    void setup() {
        mockRestServiceServer = MockRestServiceServer.createServer(parcelServiceAdapter);
    }

    @Test
    void shouldDownloadParcel() {
        // given
        final Resource resource = new ClassPathResource(PARCEL_RESPONSE_JSON_URL);
        final ParcelId parcelId = new ParcelId(PARCEL_ID);
        mockRestServiceServer.expect(requestTo(String.format(URL, PARCEL_ID)))
                .andRespond(withSuccess(resource, MediaType.APPLICATION_JSON));
        // when
        final Parcel parcel = parcelServiceAdapter.downloadParcel(parcelId);
        // then
        mockRestServiceServer.verify();
        assertNotNull(parcel);
    }

    @Test
    void shouldFailAndThrowTechnicalException() {
        // given
        final ParcelId parcelId = new ParcelId(PARCEL_ID);
        mockRestServiceServer.expect(requestTo(String.format(URL, PARCEL_ID)))
                .andRespond(withServerError());
        // when
        final Executable executable = () -> parcelServiceAdapter.downloadParcel(parcelId);
        // then
        assertThrows(TechnicalException.class, executable);
        mockRestServiceServer.verify();
    }
}
