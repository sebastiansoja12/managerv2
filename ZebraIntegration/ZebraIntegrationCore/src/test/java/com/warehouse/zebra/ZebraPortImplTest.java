package com.warehouse.zebra;

import com.warehouse.zebra.domain.port.primary.ZebraPortImpl;
import com.warehouse.zebra.domain.port.secondary.ReturnServicePort;
import com.warehouse.zebra.domain.port.secondary.RouteLogServicePort;
import com.warehouse.zebra.domain.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ZebraPortImplTest {

    @Mock
    private ReturnServicePort returnServicePort;

    @Mock
    private RouteLogServicePort routeLogServicePort;

    private ZebraPortImpl zebraPort;

    private final DeviceInformation deviceInformation = DeviceInformation.builder()
            .depotCode("KT1")
            .username("s-soja")
            .version("1.0")
            .zebraId(1L)
            .build();

    @BeforeEach
    void setup() {
        zebraPort = new ZebraPortImpl(returnServicePort, routeLogServicePort);
    }

    @Test
    void shouldProcessReturnTypeRequest() {
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
                .returnRequests(List.of(returnRequest))
                .zebraDeviceInformation(deviceInformation)
                .build();

        final Response expectedResponse = Response.builder()
                .version(deviceInformation.getVersion())
                .zebraId(deviceInformation.getZebraId())
                .username(deviceInformation.getUsername())
                .processReturns(List.of(new ProcessReturn(1L, "PROCESSING"),
                        new ProcessReturn(2L, "PROCESSING")))
                .build();

        doReturn(expectedResponse)
                .when(returnServicePort)
                .processReturn(request);
        // when
        final Response response = zebraPort.processRequest(request);
        // then
        assertNotNull(response.processReturns());
        assertThat(response.processReturns())
                .extracting(ProcessReturn::processStatus, ProcessReturn::returnId)
                .containsExactly(tuple("PROCESSING", 1L), tuple( "PROCESSING", 2L));
    }

    @Test
    void shouldThrowExceptionWhenProcessTypeIsInvalid() {
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
                .processType(null)
                .returnRequests(List.of(returnRequest))
                .zebraDeviceInformation(deviceInformation)
                .build();

        // when
        final Executable executable = () -> zebraPort.processRequest(request);
        // then
        final RuntimeException exception = assertThrows(RuntimeException.class, executable);
        assertEquals("Invalid Process Type", exception.getMessage());
    }

}
