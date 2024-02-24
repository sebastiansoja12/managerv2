package com.warehouse.zebra;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.zebra.domain.port.primary.ZebraInitializePortImpl;
import com.warehouse.zebra.domain.port.secondary.RouteLogServicePort;
import com.warehouse.zebra.domain.vo.*;

@ExtendWith(MockitoExtension.class)
public class ZebraInitializePortImplTest {


    @Mock
    private RouteLogServicePort routeLogServicePort;

    private ZebraInitializePortImpl zebraInitializePort;

    private final DeviceInformation deviceInformation = DeviceInformation.builder()
            .depotCode("KT1")
            .username("s-soja")
            .version("1.0")
            .zebraId(1L)
            .build();

    private final static UUID ROUTE_PROCESS_ID = UUID.fromString("2ea6ba6b-3f01-4d04-ad6d-952a186f48ac");

    @BeforeEach
    void setup() {
        zebraInitializePort = new ZebraInitializePortImpl(routeLogServicePort);
    }

    @Test
    void shouldProcessRequest() {
        // given
        final List<ParcelCreatedRequest> parcelCreatedRequests = List.of(ParcelCreatedRequest.builder()
                .parcelId(1L)
                .parcelRelatedId(null)
                .build());
        final Request request = Request.builder()
                .processType(ProcessType.CREATED)
                .zebraDeviceInformation(deviceInformation)
                .parcelCreatedRequests(parcelCreatedRequests)
                .build();
        final RouteProcess routeProcess = new RouteProcess(1L, LogStatus.OK);
        when(routeLogServicePort.initializeProcess(1L)).thenReturn(routeProcess);
        // when
        final Response response = zebraInitializePort.processRequest(request);
        // then
        assertThat(response.routeProcesses())
                .extracting(RouteProcess::getLogStatus, RouteProcess::getParcelId)
                .containsExactly(tuple(LogStatus.OK, 1L));
    }

    @Test
    void shouldThrowInvalidProcessTypeException() {
        // given
        final List<ParcelCreatedRequest> parcelCreatedRequests = List.of(ParcelCreatedRequest.builder()
                .parcelId(1L)
                .parcelRelatedId(null)
                .build());
        final Request request = Request.builder()
                .processType(ProcessType.REROUTE)
                .zebraDeviceInformation(deviceInformation)
                .parcelCreatedRequests(parcelCreatedRequests)
                .build();
        // when
        final Executable executable = () -> zebraInitializePort.processRequest(request);
        // then
        final RuntimeException exception = assertThrows(RuntimeException.class, executable);
        assertEquals("Invalid Process Type", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenErrorInCommunicationToTrackerFails() {
        // given
        final List<ParcelCreatedRequest> parcelCreatedRequests = List.of(ParcelCreatedRequest.builder()
                .parcelId(1L)
                .parcelRelatedId(null)
                .build());
        final Request request = Request.builder()
                .processType(ProcessType.CREATED)
                .zebraDeviceInformation(deviceInformation)
                .parcelCreatedRequests(parcelCreatedRequests)
                .build();
        doThrow(new RuntimeException("Critical error while logging process"))
                .when(routeLogServicePort)
                .initializeProcess(1L);
        // when
        final Executable executable = () -> zebraInitializePort.processRequest(request);
        // then
        final RuntimeException exception = assertThrows(RuntimeException.class, executable);
        assertEquals("Critical error while logging process", exception.getMessage());
    }
}
