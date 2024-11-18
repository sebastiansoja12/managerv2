package com.warehouse.routetracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.*;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.routetracker.domain.port.secondary.RouteLogRepository;
import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.domain.vo.Error;
import com.warehouse.routetracker.infrastructure.adapter.secondary.exception.RouteLogException;

@ExtendWith(MockitoExtension.class)
public class RouteTrackerLogPortImplTest {

    @Mock
    private RouteLogRepository repository;

    @InjectMocks
    private RouteTrackerLogPortImpl routeTrackerLogPort;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UUID processId = UUID.fromString("1d614a30-910f-486e-8c7b-e3043744088f");

    private final ShipmentId shipmentId = new ShipmentId(100001L);
    
    private final TerminalId terminalId = new TerminalId(1L);

    @Test
    void shouldInitializeRouteProcess() {
        // given
        final RouteProcess expectedRouteProcess = new RouteProcess(shipmentId, processId);

        doReturn(expectedRouteProcess)
                .when(repository)
                .save(any(RouteLogRecord.class));
        // when
        final RouteProcess routeProcess = routeTrackerLogPort.initializeRouteProcess(shipmentId);
        // then
        assertAll(
                () -> assertEquals(shipmentId, routeProcess.getShipmentId()),
                () -> assertEquals(processId, routeProcess.getProcessId())
        );
    }

    @Test
    void shouldSaveTerminalIdInformation() {
        // given
        final TerminalIdInformation information = new TerminalIdInformation(ProcessType.RETURN, shipmentId, terminalId);

        final RouteLogRecordDetail routeLogRecordDetail = RouteLogRecordDetail
                .builder()
                .processType(ProcessType.RETURN)
                .id(1L)
                .build();

        final RouteLogRecordDetails routeLogRecordDetails = RouteLogRecordDetails
                .builder()
                .routeLogRecordDetailSet(Set.of(routeLogRecordDetail))
                .build();

        final RouteLogRecord routeLogRecord = RouteLogRecord
                .builder()
                .id(processId)
                .routeLogRecordDetails(routeLogRecordDetails)
                .parcelId(shipmentId.getValue())
                .build();

        doReturn(routeLogRecord)
                .when(repository)
                .find(shipmentId);

        doNothing()
                .when(repository)
                .update(routeLogRecord);
        // when
        routeTrackerLogPort.saveTerminalIdInformation(information);
        // then
        verify(repository, times(1)).update(any());
    }

    @Test
    void shouldSaveTerminalIdInformationToNewProcessWhenGivenWasNotFound() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final TerminalIdInformation information = new TerminalIdInformation(ProcessType.RETURN, shipmentId, terminalId);
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(shipmentId)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveTerminalIdInformation(information);
        // then
        verify(routeLogRecord).saveTerminalId(processType, terminalId);
        verify(repository, times(1)).update(any());
    }
    
    @Test
    void shouldSaveZebraVersionInformation() {
        // given
        final TerminalVersionInformation information = new TerminalVersionInformation("1.0", shipmentId, ProcessType.RETURN);

        final RouteLogRecordDetail routeLogRecordDetail = RouteLogRecordDetail
                .builder()
                .processType(ProcessType.RETURN)
                .id(1L)
                .build();

        final RouteLogRecordDetails routeLogRecordDetails = RouteLogRecordDetails
                .builder()
                .routeLogRecordDetailSet(Set.of(routeLogRecordDetail))
                .build();
        
        final RouteLogRecord routeLogRecord = RouteLogRecord
                .builder()
                .id(processId)
                .routeLogRecordDetails(routeLogRecordDetails)
                .parcelId(shipmentId.getValue())
                .build();

        doReturn(routeLogRecord)
                .when(repository)
                .find(shipmentId);

        doNothing()
                .when(repository)
                .update(routeLogRecord);

        // when
        routeTrackerLogPort.saveZebraVersionInformation(information);
        // then
        assertThat(routeLogRecord.getRouteLogRecordDetails()
                .getRouteLogRecordDetailSet())
                .extracting(RouteLogRecordDetail::getVersion)
                .containsExactly("1.0");
    }

    @Test
    void shouldSaveZebraVersionInformationAndCreateNewProcessWhenGivenWasNotFound() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final String version = "1.0";
        final TerminalVersionInformation information = new TerminalVersionInformation("1.0", shipmentId, ProcessType.RETURN);
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(shipmentId)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveZebraVersionInformation(information);
        // then
        verify(routeLogRecord).saveTerminalVersion(processType, version);
        verify(repository, times(1)).update(any());
    }

    @Test
    void shouldSaveReturnCode() {
        // given
        final ErrorInformation information = new ErrorInformation(shipmentId, new Error("1234"));

        final RouteLogRecordDetail routeLogRecordDetail = RouteLogRecordDetail
                .builder()
                .processType(ProcessType.RETURN)
                .id(1L)
                .build();

        final RouteLogRecordDetails routeLogRecordDetails = RouteLogRecordDetails
                .builder()
                .routeLogRecordDetailSet(Set.of(routeLogRecordDetail))
                .build();

        final RouteLogRecord routeLogRecord = RouteLogRecord
                .builder()
                .id(processId)
                .routeLogRecordDetails(routeLogRecordDetails)
                .parcelId(shipmentId.getValue())
                .build();

        doReturn(routeLogRecord)
                .when(repository)
                .find(shipmentId);

        doNothing()
                .when(repository)
                .update(routeLogRecord);

        // when
        routeTrackerLogPort.saveReturnErrorCode(information);
        // then
        verify(repository, times(1)).update(routeLogRecord);
        assertEquals("1234", routeLogRecord.getReturnCode());
    }

    @Test
    void shouldSaveFaultDescription() {
        // given
        final ProcessType processType = ProcessType.REDIRECT;
        final String description = "Error";
        final FaultDescription faultDescription = new FaultDescription(processType, shipmentId, description);
        final RouteLogRecordDetail routeLogRecordDetail = RouteLogRecordDetail
                .builder()
                .processType(processType)
                .id(1L)
                .build();

        final RouteLogRecordDetails routeLogRecordDetails = RouteLogRecordDetails
                .builder()
                .routeLogRecordDetailSet(Set.of(routeLogRecordDetail))
                .build();

        final RouteLogRecord routeLogRecord = RouteLogRecord
                .builder()
                .id(processId)
                .routeLogRecordDetails(routeLogRecordDetails)
                .parcelId(shipmentId.getValue())
                .build();

        doReturn(routeLogRecord)
                .when(repository)
                .find(shipmentId);

        doNothing()
                .when(repository)
                .update(routeLogRecord);

        // when
        routeTrackerLogPort.saveFaultDescription(faultDescription);
        // then
        verify(repository, times(1)).update(routeLogRecord);
        assertEquals(description, routeLogRecord.getFaultDescription());
    }

    @Test
    void shouldSaveTerminalRequest() {
        // given
        final ProcessType processType = ProcessType.REDIRECT;
        final String requestValue = "request";
        final TerminalRequest request = new TerminalRequest(processType, shipmentId, requestValue);
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(shipmentId)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveTerminalRequest(request);
        // then
        verify(routeLogRecord).updateRequest(processType, requestValue);
    }

    @Test
    void shouldSaveReturnTrackRequest() throws JsonProcessingException {
        // given
        final ProcessType processType = ProcessType.REDIRECT;
        final ReturnTrackRequest request = new ReturnTrackRequest(shipmentId, processType, "s-soja", "KT1");
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(shipmentId)).thenReturn(routeLogRecord);
        final String requestAsString = objectMapper.writeValueAsString(request);
        // when
        routeTrackerLogPort.saveReturnTrackRequest(request);
        // then
        verify(routeLogRecord).updateRequest(processType, requestAsString);
    }

    @Test
    void shouldSaveDeliveryReturnRequest() throws JsonProcessingException {
        // given
        final ProcessType processType = ProcessType.RETURN;
		final DeliveryReturnRequest request = new DeliveryReturnRequest(UUID.randomUUID(), shipmentId, "RETURN",
				"12345", "OK", "KT1", "s-soja", processType);
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(shipmentId)).thenReturn(routeLogRecord);
        final String requestAsString = objectMapper.writeValueAsString(request);
        // when
        routeTrackerLogPort.saveDeliveryReturnRequest(request);
        // then
        verify(routeLogRecord).updateRequest(processType, requestAsString);
    }

    @Test
    void shouldSaveUsername() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final String username = "s-soja";
        final UsernameRequest request = new UsernameRequest(username, shipmentId, processType);
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(shipmentId)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveUsername(request);
        // then
        verify(routeLogRecord).saveUsername(processType, username);
    }

    @Test
    void shouldSaveDepotCode() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final String depotCode = "KT1";
        final DepotCodeRequest request = new DepotCodeRequest(depotCode, shipmentId, processType);
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(shipmentId)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveDepotCode(request);
        // then
        verify(routeLogRecord).saveDepotCode(processType, depotCode);
    }

    @Test
    void shouldSaveSupplierCode() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final String supplierCode = "abc";
        final SupplierCodeRequest request = new SupplierCodeRequest(supplierCode, shipmentId, processType);
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(shipmentId)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveSupplierCode(request);
        // then
        verify(routeLogRecord).saveSupplierCode(processType, supplierCode);
    }

    @Test
    void shouldSaveDescription() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final String value = "value";
        final DescriptionRequest request = new DescriptionRequest(value, shipmentId, processType);
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(shipmentId)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveDescription(request);
        // then
        verify(routeLogRecord).saveDescription(processType, value);
    }


    @Test
    void shouldSaveDeliveryStatus() {
        // given
        final ProcessType processType = ProcessType.MISS;
        final DeliveryStatusRequest request = new DeliveryStatusRequest(shipmentId, "ABC", "KT1", processType);
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(shipmentId)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveDeliveryStatus(request);
        // then
        verify(routeLogRecord).updateShipmentStatus(processType, ParcelStatus.DELIVERY);
    }

    @Test
    void shouldNotSaveDeliveryStatus() {
        // given
        final ProcessType processType = ProcessType.MISS;
        final DeliveryStatusRequest request = new DeliveryStatusRequest(shipmentId, "ABC", "KT1", processType);
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(shipmentId);
        // when
        final Executable executable = () -> routeTrackerLogPort.saveDeliveryStatus(request);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveReturnTrackRequestWhenProcessWasNotFound() {
        // given
        final ProcessType processType = ProcessType.REDIRECT;
        final ReturnTrackRequest request = new ReturnTrackRequest(shipmentId, processType, "s-soja", "KT1");
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(shipmentId);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveReturnTrackRequest(request);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveDeliveryReturnRequestWhenProcessWasNotFound() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final DeliveryReturnRequest request = new DeliveryReturnRequest(null, shipmentId, "RETURN", "12345",
                "OK", "KT1", "s-soja", processType);
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(shipmentId);
        // when
        final Executable executable = () -> routeTrackerLogPort.saveDeliveryReturnRequest(request);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveSupplierCode() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final SupplierCodeRequest request = new SupplierCodeRequest("abc", shipmentId, processType);
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(shipmentId);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveSupplierCode(request);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveUsername() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final UsernameRequest request = new UsernameRequest("abc", shipmentId, processType);
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(shipmentId);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveUsername(request);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveDepotCode() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final DepotCodeRequest request = new DepotCodeRequest("abc", shipmentId, processType);
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(shipmentId);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveDepotCode(request);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveDescription() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final DescriptionRequest request = new DescriptionRequest("value", shipmentId, processType);
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(shipmentId);
        // when
        final Executable executable = () -> routeTrackerLogPort.saveDescription(request);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldFindRouteLogRecord() {
        // given
        final RouteLogRecord expected = mock(RouteLogRecord.class);
        when(repository.find(shipmentId)).thenReturn(expected);
        // when
        final RouteLogRecord routeLogRecord = routeTrackerLogPort.find(shipmentId);
        // then
        assertEquals(expected, routeLogRecord);
    }

    @Test
    void shouldNotFindRouteLogRecord() {
        // given
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(shipmentId);
        // when
        final Executable executable = () -> routeTrackerLogPort.find(shipmentId);
        // then
        assertThrows(RouteLogException.class, executable);
    }

    @Test
    void shouldNotSaveTerminalRequestWhenProcessWasNotFound() {
        // given
        final ProcessType processType = ProcessType.REDIRECT;
        final TerminalRequest request = new TerminalRequest(processType, shipmentId, "requestAsJson");
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(shipmentId);

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
        final FaultDescription faultDescription = new FaultDescription(processType, shipmentId, "faultDescription");
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(shipmentId);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveFaultDescription(faultDescription);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveReturnCodeWhenProcessWasNotFound() {
        // given
        final ErrorInformation information = new ErrorInformation(shipmentId, new Error("12345"));

        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(shipmentId);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveReturnErrorCode(information);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveTerminalIdInformationWhenProcessIsNotFound() {
        // given
        final TerminalIdInformation information = new TerminalIdInformation(ProcessType.MISS, shipmentId, new TerminalId(1L));
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(shipmentId);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveTerminalIdInformation(information);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveZebraVersionInformationWhenProcessWasNotFound() {
        // given
        final TerminalVersionInformation information = new TerminalVersionInformation("1.0", shipmentId, ProcessType.MISS);

        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(shipmentId);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveZebraVersionInformation(information);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }
}
