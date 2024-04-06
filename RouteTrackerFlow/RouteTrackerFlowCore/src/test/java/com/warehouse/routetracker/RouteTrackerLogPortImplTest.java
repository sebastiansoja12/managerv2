package com.warehouse.routetracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Set;
import java.util.UUID;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.model.DeliveryReturnRequest;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetail;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetails;
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

    private final UUID ROUTE_ID = UUID.fromString("1d614a30-910f-486e-8c7b-e3043744088f");

    public static final Long PARCEL_ID = 100001L;


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
                .save(any(RouteLogRecord.class));
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

        final RouteLogRecord routeLogRecord = RouteLogRecord
                .builder()
                .id(ROUTE_ID)
                .routeLogRecordDetails(routeLogRecordDetails)
                .parcelId(PARCEL_ID)
                .build();

        doReturn(routeLogRecord)
                .when(repository)
                .find(PARCEL_ID);

        doNothing()
                .when(repository)
                .update(routeLogRecord);
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

        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);

        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveZebraIdInformation(information);
        // then
        verify(routeLogRecord).saveZebraIdInformation(processType, zebraId);
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
        
        final RouteLogRecord routeLogRecord = RouteLogRecord
                .builder()
                .id(ROUTE_ID)
                .routeLogRecordDetails(routeLogRecordDetails)
                .parcelId(PARCEL_ID)
                .build();

        doReturn(routeLogRecord)
                .when(repository)
                .find(PARCEL_ID);

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
        final ZebraVersionInformation information = ZebraVersionInformation
                .builder()
                .parcelId(PARCEL_ID)
                .version(version)
                .processType(processType)
                .build();

        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);

        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveZebraVersionInformation(information);
        // then
        verify(routeLogRecord).saveZebraVersionInformation(processType, version);
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

        final RouteLogRecord routeLogRecord = RouteLogRecord
                .builder()
                .id(ROUTE_ID)
                .routeLogRecordDetails(routeLogRecordDetails)
                .parcelId(PARCEL_ID)
                .build();

        doReturn(routeLogRecord)
                .when(repository)
                .find(PARCEL_ID);

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
                .id(ROUTE_ID)
                .routeLogRecordDetails(routeLogRecordDetails)
                .parcelId(PARCEL_ID)
                .build();

        doReturn(routeLogRecord)
                .when(repository)
                .find(PARCEL_ID);

        doNothing()
                .when(repository)
                .update(routeLogRecord);

        // when
        routeTrackerLogPort.saveFaultDescription(processType, PARCEL_ID, "Error");
        // then
        verify(repository, times(1)).update(routeLogRecord);
        assertEquals("Error", routeLogRecord.getFaultDescription());
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
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecord);
        final String requestAsString = objectMapper.writeValueAsString(request);
        // when
        routeTrackerLogPort.saveTerminalRequest(request);
        // then
        verify(routeLogRecord).updateRequest(processType, requestAsString);
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
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecord);
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
        final DeliveryReturnRequest request = DeliveryReturnRequest
                .builder()
                .parcelId(PARCEL_ID)
                .username("s-soja")
                .depotCode("KT1")
                .processType(processType)
                .deliveryStatus("RETURN")
                .returnToken("12345")
                .updateStatus("OK")
                .build();
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecord);
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
        final UsernameRequest request = UsernameRequest
                .builder()
                .parcelId(PARCEL_ID)
                .username("s-soja")
                .processType(processType)
                .build();
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveUsername(request);
        // then
        verify(routeLogRecord).saveUsername(processType, "s-soja");
    }

    @Test
    void shouldSaveDepotCode() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final DepotCodeRequest request = DepotCodeRequest
                .builder()
                .parcelId(PARCEL_ID)
                .depotCode("KT1")
                .processType(processType)
                .build();
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveDepotCode(request);
        // then
        verify(routeLogRecord).saveDepotCode(processType, "KT1");
    }

    @Test
    void shouldSaveSupplierCode() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final SupplierCodeRequest request = SupplierCodeRequest
                .builder()
                .parcelId(PARCEL_ID)
                .supplierCode("abc")
                .processType(processType)
                .build();
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveSupplierCode(request);
        // then
        verify(routeLogRecord).saveSupplierCode(processType, "abc");
    }

    @Test
    void shouldSaveDescription() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final DescriptionRequest request = DescriptionRequest
                .builder()
                .parcelId(PARCEL_ID)
                .processType(processType)
                .value("value")
                .build();
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveDescription(request);
        // then
        verify(routeLogRecord).saveDescription(processType, "value");
    }

    @Test
    void shouldSaveDeliveryStatus() {
        // given
        final ProcessType processType = ProcessType.MISS;
        final DeliveryStatusRequest request = DeliveryStatusRequest
                .builder()
                .parcelId(PARCEL_ID)
                .processType(processType)
                .depotCode("KT1")
                .supplierCode("ABC")
                .build();
        final RouteLogRecord routeLogRecord = mock(RouteLogRecord.class);
        when(repository.find(PARCEL_ID)).thenReturn(routeLogRecord);
        // when
        routeTrackerLogPort.saveDeliveryStatus(request);
        // then
        verify(routeLogRecord).saveParcelStatus(processType, ParcelStatus.DELIVERY);
    }

    @Test
    void shouldNotSaveDeliveryStatus() {
        // given
        final ProcessType processType = ProcessType.MISS;
        final DeliveryStatusRequest request = DeliveryStatusRequest
                .builder()
                .parcelId(PARCEL_ID)
                .processType(processType)
                .depotCode("KT1")
                .supplierCode("ABC")
                .build();
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(PARCEL_ID);
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
    void shouldNotSaveDeliveryReturnRequestWhenProcessWasNotFound() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final DeliveryReturnRequest request = DeliveryReturnRequest
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
        final Executable executable = () -> routeTrackerLogPort.saveDeliveryReturnRequest(request);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotSaveSupplierCode() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final SupplierCodeRequest request = SupplierCodeRequest
                .builder()
                .parcelId(PARCEL_ID)
                .supplierCode("abc")
                .processType(processType)
                .build();
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(PARCEL_ID);

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
        final UsernameRequest request = UsernameRequest
                .builder()
                .parcelId(PARCEL_ID)
                .username("s-soja")
                .processType(processType)
                .build();
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(PARCEL_ID);

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
        final DepotCodeRequest request = DepotCodeRequest
                .builder()
                .parcelId(PARCEL_ID)
                .depotCode("KT1")
                .processType(processType)
                .build();
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(PARCEL_ID);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveDepotCode(request);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldNotDescription() {
        // given
        final ProcessType processType = ProcessType.RETURN;
        final DescriptionRequest request = DescriptionRequest
                .builder()
                .parcelId(PARCEL_ID)
                .value("value")
                .processType(processType)
                .build();
        doThrow(new RouteLogException("Route log was not found"))
                .when(repository)
                .find(PARCEL_ID);

        // when
        final Executable executable = () -> routeTrackerLogPort.saveDescription(request);
        // then
        assertThrows(RouteLogException.class, executable);
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldFindRouteLogRecord() {
        // given
        final Long parcelId = 1L;
        final RouteLogRecord expected = mock(RouteLogRecord.class);
        when(repository.find(parcelId)).thenReturn(expected);
        // when
        final RouteLogRecord routeLogRecord = routeTrackerLogPort.find(parcelId);
        // then
        assertEquals(expected, routeLogRecord);
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
}
