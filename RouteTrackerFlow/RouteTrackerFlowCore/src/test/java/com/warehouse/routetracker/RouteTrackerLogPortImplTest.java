package com.warehouse.routetracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.model.*;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.routetracker.domain.port.secondary.RouteRepository;
import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.domain.vo.Error;
import com.warehouse.routetracker.infrastructure.adapter.secondary.exception.RouteLogException;

@ExtendWith(MockitoExtension.class)
public class RouteTrackerLogPortImplTest {

    @Mock
    private RouteRepository repository;

    @InjectMocks
    private RouteTrackerLogPortImpl routeTrackerLogPort;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UUID ROUTE_ID = UUID.fromString("1d614a30-910f-486e-8c7b-e3043744088f");

    private final UUID ROUTE_ID_2 = UUID.fromString("f0a45a51-6d0f-45ab-a839-41f161208c65");

    public static final Long PARCEL_ID = 100001L;

    public static final Long PARCEL_ID2 = 100002L;


    @Test
    void shouldSaveSupplyRoute() {
        // given
        final String depotCode = "KT1";
        final String supplierCode = "abc";
        final DeliveryInformation deliveryInformation = DeliveryInformation.builder()
                .depotCode(depotCode)
                .deliveryParcelStatus(ParcelStatus.DELIVERY)
                .parcelId(PARCEL_ID)
                .supplierCode(supplierCode)
                .token("token")
                .build();

        final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                .supplierCode(supplierCode)
                .parcelId(PARCEL_ID)
                .depotCode(depotCode)
                .parcelStatus(ParcelStatus.DELIVERY)
                .build();

        // when
        routeTrackerLogPort.saveDelivery(Collections.singletonList(deliveryInformation));
        // then
        verify(repository, times(1)).save(routeLogRecord);
    }

    @Test
    void shouldNotSaveSupplyRouteWhenTokenDoesNotExist() {
        // given
        final DeliveryInformation deliveryInformation = DeliveryInformation.builder().build();
        final RouteLogRecord routeLogRecord = RouteLogRecord.builder().build();
        // when
        routeTrackerLogPort.saveDelivery(Collections.singletonList(deliveryInformation));
        // then
        verify(repository, times(0)).save(routeLogRecord);
    }

    @Test
    void shouldSaveRoutes() {
        // given
        final RouteRequest routeRequest = RouteRequest.builder()
                .parcelId(100001L)
                .build();

        final RouteRequest routeRequest2 = RouteRequest.builder()
                .parcelId(PARCEL_ID2)
                .build();

        final List<RouteRequest> requests = Arrays.asList(routeRequest, routeRequest2);

        // create expected route response objects with uuid
        final RouteResponse response = RouteResponse.builder()
                .id(ROUTE_ID)
                .build();
        final RouteResponse response2 = RouteResponse.builder()
                .id(ROUTE_ID_2)
                .build();
        // model route objects sent to liquibase
        final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                .parcelId(PARCEL_ID)
                .build();

        final RouteLogRecord routeLogRecord2 = RouteLogRecord.builder()
                .parcelId(PARCEL_ID2)
                .build();

        doReturn(response)
                .when(repository)
                .save(routeLogRecord);

        doReturn(response2)
                .when(repository)
                .save(routeLogRecord2);

        // when
        final List<RouteResponse> responses = routeTrackerLogPort.saveRoutes(requests);

        // then
        Assertions.assertEquals(expectedToBe(response), responses.get(0));
        Assertions.assertEquals(expectedToBe(response2), responses.get(1));
    }

    @Test
    void shouldDeleteRoute() {
        // given
        final String username = "s-soja";
        final RouteDeleteRequest deleteRequest = RouteDeleteRequest.builder()
                .id("d8d53e7d-9175-4b5b-bf0d-bc209549c3a9")
                .build();
        // when
        routeTrackerLogPort.deleteRoute(deleteRequest);
        // then
        verify(repository, times(1)).deleteRoute(deleteRequest);
    }

    @Test
    void shouldFindByUsername() {
        // given
        final String username = "s-soja";
        final List<RouteInformation> expectedRouteInformations = Collections.singletonList(
                RouteInformation.builder()
                        .parcelStatus(ParcelStatus.RETURN)
                        .build()
        );
        doReturn(expectedRouteInformations)
                .when(repository)
                .findByUsername(username);
        // when
        final List<RouteInformation> routeInformations = routeTrackerLogPort.findRoutesByUsername(username);
        // then
        routeInformations.forEach(
                routeInformation -> assertEquals(expectedToBe(routeInformation.getParcelStatus()), ParcelStatus.RETURN)
        );

    }

    @Test
    void shouldFindByParcelId() {
        // given
        final Long parcelId = 1L;
        final List<RouteInformation> expectedRouteInformations = Collections.singletonList(
                RouteInformation.builder()
                        .parcel(Parcel.builder().id(parcelId).build())
                        .parcelStatus(ParcelStatus.RETURN)
                        .build()
        );
        doReturn(expectedRouteInformations)
                .when(repository)
                .findByParcelId(parcelId);
        // when
        final List<RouteInformation> routeInformations = routeTrackerLogPort.getRouteListByParcelId(parcelId);
        // then
        routeInformations.forEach(
                routeInformation -> assertEquals(expectedToBe(routeInformation.getParcelStatus()), ParcelStatus.RETURN)
        );

    }

    @Test
    void shouldInitializeRouteProcess() {
        // given
        final ParcelId parcelId = ParcelId.builder().value(PARCEL_ID).build();
        final RouteProcess expectedRouteProcess = RouteProcess
                .builder()
                .processId(ROUTE_ID)
                .parcelId(PARCEL_ID)
                .build();

        doReturn(expectedRouteProcess)
                .when(repository)
                .save(any(RouteLogRecordToChange.class));
        // when
        final RouteProcess routeProcess = routeTrackerLogPort.initializeRouteProcess(parcelId);
        // then
        assertAll(
                () -> assertEquals(PARCEL_ID, routeProcess.getParcelId()),
                () -> assertEquals(ROUTE_ID, routeProcess.getProcessId())
        );
    }

    @Test
    void shouldSaveZebraIdInformation() {
        // given
        final ZebraIdInformation information = ZebraIdInformation
                .builder()
                .zebraId(1L)
                .parcelId(PARCEL_ID)
                .processType(ProcessType.RETURN)
                .build();

        final RouteLogRecordDetail routeLogRecordDetail = RouteLogRecordDetail
                .builder()
                .processType(ProcessType.RETURN)
                .id(1L)
                .build();

        final RouteLogRecordDetails routeLogRecordDetails = RouteLogRecordDetails
                .builder()
                .routeLogRecordDetailSet(Set.of(routeLogRecordDetail))
                .build();

        final RouteLogRecordToChange routeLogRecordToChange = RouteLogRecordToChange
                .builder()
                .id(ROUTE_ID)
                .routeLogRecordDetails(routeLogRecordDetails)
                .parcelId(PARCEL_ID)
                .build();

        doReturn(routeLogRecordToChange)
                .when(repository)
                .find(PARCEL_ID);

        doNothing()
                .when(repository)
                .update(routeLogRecordToChange);
        // when
        routeTrackerLogPort.saveZebraIdInformation(information);
        // then
        verify(repository, times(1)).update(any());
    }

    @Test
    void shouldSaveZebraIdInformationToNewProcessWhenGivenWasNotFound() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final Long zebraId = 1L;
        final ZebraIdInformation information = ZebraIdInformation
                .builder()
                .zebraId(zebraId)
                .parcelId(PARCEL_ID)
                .processType(processType)
                .build();

        final RouteLogRecordToChange routeLogRecordToChange = mock(RouteLogRecordToChange.class);

        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecordToChange);
        // when
        routeTrackerLogPort.saveZebraIdInformation(information);
        // then
        verify(routeLogRecordToChange).saveZebraIdInformation(processType, zebraId);
        verify(repository, times(1)).update(any());
    }
    
    @Test
    void shouldSaveZebraVersionInformation() {
        // given
        final ZebraVersionInformation information = ZebraVersionInformation
                .builder()
                .version("1.0")
                .parcelId(PARCEL_ID)
                .processType(ProcessType.RETURN)
                .build();

        final RouteLogRecordDetail routeLogRecordDetail = RouteLogRecordDetail
                .builder()
                .processType(ProcessType.RETURN)
                .id(1L)
                .build();

        final RouteLogRecordDetails routeLogRecordDetails = RouteLogRecordDetails
                .builder()
                .routeLogRecordDetailSet(Set.of(routeLogRecordDetail))
                .build();
        
        final RouteLogRecordToChange routeLogRecordToChange = RouteLogRecordToChange
                .builder()
                .id(ROUTE_ID)
                .routeLogRecordDetails(routeLogRecordDetails)
                .parcelId(PARCEL_ID)
                .build();

        doReturn(routeLogRecordToChange)
                .when(repository)
                .find(PARCEL_ID);

        doNothing()
                .when(repository)
                .update(routeLogRecordToChange);

        // when
        routeTrackerLogPort.saveZebraVersionInformation(information);
        // then
        assertThat(routeLogRecordToChange.getRouteLogRecordDetails()
                .getRouteLogRecordDetailSet())
                .extracting(RouteLogRecordDetail::getVersion)
                .containsExactly("1.0");
    }

    @Test
    void shouldSaveZebraVersionInformationAndCreateNewProcessWhenGivenWasNotFound() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final String version = "1.0";
        final ZebraVersionInformation information = ZebraVersionInformation
                .builder()
                .parcelId(PARCEL_ID)
                .version(version)
                .processType(processType)
                .build();

        final RouteLogRecordToChange routeLogRecordToChange = mock(RouteLogRecordToChange.class);

        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecordToChange);
        // when
        routeTrackerLogPort.saveZebraVersionInformation(information);
        // then
        verify(routeLogRecordToChange).saveZebraVersionInformation(processType, version);
        verify(repository, times(1)).update(any());
    }

    @Test
    void shouldSaveReturnCode() {
        // given
        final ErrorInformation information = ErrorInformation
                .builder()
                .error(new Error("1234"))
                .parcelId(PARCEL_ID)
                .build();

        final RouteLogRecordDetail routeLogRecordDetail = RouteLogRecordDetail
                .builder()
                .processType(ProcessType.RETURN)
                .id(1L)
                .build();

        final RouteLogRecordDetails routeLogRecordDetails = RouteLogRecordDetails
                .builder()
                .routeLogRecordDetailSet(Set.of(routeLogRecordDetail))
                .build();

        final RouteLogRecordToChange routeLogRecordToChange = RouteLogRecordToChange
                .builder()
                .id(ROUTE_ID)
                .routeLogRecordDetails(routeLogRecordDetails)
                .parcelId(PARCEL_ID)
                .build();

        doReturn(routeLogRecordToChange)
                .when(repository)
                .find(PARCEL_ID);

        doNothing()
                .when(repository)
                .update(routeLogRecordToChange);

        // when
        routeTrackerLogPort.saveReturnErrorCode(information);
        // then
        verify(repository, times(1)).update(routeLogRecordToChange);
        assertEquals("1234", routeLogRecordToChange.getReturnCode());
    }

    @Test
    void shouldSaveFaultDescription() {
        // given
        final ProcessType processType = ProcessType.REDIRECT;
        final RouteLogRecordDetail routeLogRecordDetail = RouteLogRecordDetail
                .builder()
                .processType(processType)
                .id(1L)
                .build();

        final RouteLogRecordDetails routeLogRecordDetails = RouteLogRecordDetails
                .builder()
                .routeLogRecordDetailSet(Set.of(routeLogRecordDetail))
                .build();

        final RouteLogRecordToChange routeLogRecordToChange = RouteLogRecordToChange
                .builder()
                .id(ROUTE_ID)
                .routeLogRecordDetails(routeLogRecordDetails)
                .parcelId(PARCEL_ID)
                .build();

        doReturn(routeLogRecordToChange)
                .when(repository)
                .find(PARCEL_ID);

        doNothing()
                .when(repository)
                .update(routeLogRecordToChange);

        // when
        routeTrackerLogPort.saveFaultDescription(processType, PARCEL_ID, "Error");
        // then
        verify(repository, times(1)).update(routeLogRecordToChange);
        assertEquals("Error", routeLogRecordToChange.getFaultDescription());
    }

    @Test
    void shouldSaveTerminalRequest() throws JsonProcessingException {
        // given
        final ProcessType processType = ProcessType.REDIRECT;
        final TerminalRequest request = TerminalRequest
                .builder()
                .parcelId(PARCEL_ID)
                .processType(processType)
                .build();
        final RouteLogRecordToChange routeLogRecordToChange = mock(RouteLogRecordToChange.class);
        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecordToChange);
        final String requestAsString = objectMapper.writeValueAsString(request);
        // when
        routeTrackerLogPort.saveTerminalRequest(request);
        // then
        verify(routeLogRecordToChange).saveTerminalRequest(processType, requestAsString);
    }

    @Test
    void shouldSaveReturnTrackRequest() throws JsonProcessingException {
        // given
        final ProcessType processType = ProcessType.REDIRECT;
        final ReturnTrackRequest request = ReturnTrackRequest
                .builder()
                .parcelId(PARCEL_ID)
                .username("s-soja")
                .depotCode("KT1")
                .processType(processType)
                .build();
        final RouteLogRecordToChange routeLogRecordToChange = mock(RouteLogRecordToChange.class);
        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecordToChange);
        final String requestAsString = objectMapper.writeValueAsString(request);
        // when
        routeTrackerLogPort.saveReturnTrackRequest(request);
        // then
        verify(routeLogRecordToChange).saveReturnTrackRequest(processType, requestAsString);
    }

    @Test
    void shouldNotSaveReturnTrackRequestWhenProcessWasNotFound() {
        // given
        final ProcessType processType = ProcessType.REDIRECT;
        final ReturnTrackRequest request = ReturnTrackRequest
                .builder()
                .parcelId(PARCEL_ID)
                .username("s-soja")
                .depotCode("KT1")
                .processType(processType)
                .build();
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(PARCEL_ID);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveReturnTrackRequest(request);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldFindRouteLogRecord() {
        // given
        final Long parcelId = 1L;
        final RouteLogRecordToChange expected = mock(RouteLogRecordToChange.class);
        when(repository.find(parcelId)).thenReturn(expected);
        // when
        final RouteLogRecordToChange routeLogRecordToChange = routeTrackerLogPort.find(parcelId);
        // then
        assertEquals(expected, routeLogRecordToChange);
    }

    @Test
    void shouldNotFindRouteLogRecord() {
        // given
        final Long parcelId = 1L;
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(parcelId);
        // when
        final Executable executable = () -> routeTrackerLogPort.find(parcelId);
        // then
        assertThrows(RouteLogException.class, executable);
    }

    @Test
    void shouldNotSaveTerminalRequestWhenProcessWasNotFound() {
        // given
        final ProcessType processType = ProcessType.REDIRECT;
        final TerminalRequest request = TerminalRequest
                .builder()
                .parcelId(PARCEL_ID)
                .processType(processType)
                .build();
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(PARCEL_ID);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveTerminalRequest(request);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveFaultDescriptionWhenProcessWasNotFound() {
        // given
        final ProcessType processType = ProcessType.REDIRECT;
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(PARCEL_ID);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveFaultDescription(processType, PARCEL_ID, "Error");
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveReturnCodeWhenProcessWasNotFound() {
        // given
        final ErrorInformation information = ErrorInformation
                .builder()
                .error(new Error("1234"))
                .parcelId(PARCEL_ID)
                .build();

        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(PARCEL_ID);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveReturnErrorCode(information);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveZebraIdInformationWhenProcessIsNotFound() {
        // given
        final ZebraIdInformation information = ZebraIdInformation
                .builder()
                .zebraId(1L)
                .parcelId(PARCEL_ID)
                .processType(ProcessType.RETURN)
                .build();

        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(PARCEL_ID);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveZebraIdInformation(information);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveZebraVersionInformationWhenProcessWasNotFound() {
        // given
        final ZebraVersionInformation information = ZebraVersionInformation
                .builder()
                .version("1.0")
                .parcelId(PARCEL_ID)
                .processType(ProcessType.RETURN)
                .build();

        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(PARCEL_ID);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveZebraVersionInformation(information);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }


    private <T> T expectedToBe(T t) {
        return t;
    }
}
