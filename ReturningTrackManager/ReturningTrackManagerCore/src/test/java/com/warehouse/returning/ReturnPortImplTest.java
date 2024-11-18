package com.warehouse.returning;


import static com.warehouse.returning.domain.model.ReturnStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import com.warehouse.returning.domain.port.secondary.RouteLogServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.returning.domain.exception.DepotCodeMissingException;
import com.warehouse.returning.domain.exception.UsernameMissingException;
import com.warehouse.returning.domain.model.*;
import com.warehouse.returning.domain.port.primary.ReturnPortImpl;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.service.ReturnService;
import com.warehouse.returning.domain.service.ReturnServiceImpl;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.domain.vo.ReturnResponse;

@ExtendWith(MockitoExtension.class)
public class ReturnPortImplTest {

    @Mock
    private ReturnRepository returnRepository;

    @Mock
    private RouteLogServicePort routeLogServicePort;

    private ReturnPortImpl returnPort;

    private static final String RECIPIENT_NOT_AVAILABLE = "Recipient not available";

    private static final String USERNAME = "s-soja";

    @BeforeEach
    void setup() {
        final ReturnService returnService = new ReturnServiceImpl(returnRepository);
        returnPort = new ReturnPortImpl(returnService, routeLogServicePort);
    }

    @Test
    void shouldProcessTheReturn() {
        // given
        final String depotCode = "KT1";
		final ReturnRequest request = buildReturnRequest(depotCode, USERNAME, "returnToken", CREATED);

        final ReturnPackageRequest returnPackage = ReturnPackageRequest.builder()
                .depotCode(depotCode)
                .supplierCode("abc")
                .returnToken("returnToken")
                .returnStatus(PROCESSING)
                .parcel(createParcel())
                .reason(RECIPIENT_NOT_AVAILABLE)
                .username(USERNAME)
                .build();

        final ProcessReturn processReturn = new ProcessReturn(1L, 1L, "PROCESSING");

        doReturn(processReturn)
                .when(returnRepository)
                .save(returnPackage);

        // when
        final ReturnResponse response = returnPort.process(request);
        // then
        assertThat(response.processReturn())
                .flatExtracting(ProcessReturn::processStatus)
                .contains(PROCESSING.name());
        // and status of request was changed to CREATED
        assertThat(request.getRequests())
                .flatExtracting(ReturnPackageRequest::getReturnStatus)
                .contains(PROCESSING);
    }

    @Test
    void shouldCompleteTheReturn() {
        // given
        final String depotCode = "KT1";
        final ReturnRequest request = buildReturnRequest(depotCode, USERNAME, "returnToken", PROCESSING);

        final ReturnPackageRequest returnPackage = ReturnPackageRequest.builder()
                .depotCode(depotCode)
                .supplierCode("abc")
                .returnToken("returnToken")
                .returnStatus(COMPLETED)
                .parcel(createParcel())
                .reason(RECIPIENT_NOT_AVAILABLE)
                .username(USERNAME)
                .build();

        final ProcessReturn processReturn = new ProcessReturn(1L, 1L, "COMPLETED");

        doReturn(processReturn)
                .when(returnRepository)
                .update(returnPackage);

        // when
        final ReturnResponse response = returnPort.process(request);
        // then
        assertThat(response.processReturn())
                .flatExtracting(ProcessReturn::processStatus)
                .contains(COMPLETED.name());
    }

    @Test
    void shouldChangeReturnStatusToProcessingForCancelledParcels() {
        // given
        final String depotCode = "KT1";
        final ReturnRequest request = buildReturnRequest(depotCode, USERNAME, "returnToken", CANCELLED);
        
        doReturn(PROCESSING)
                .when(returnRepository)
                .unlockReturn(1L, "returnToken");

        // when
        returnPort.process(request);
        // then and status of request was changed to PROCESSING
        assertThat(request.getRequests())
                .flatExtracting(ReturnPackageRequest::getReturnStatus)
                .contains(PROCESSING);
    }

    @Test
    void shouldUpdateReturnToCompletedWhenWasProcessed() {
        // given
        final String depotCode = "KT1";
        final String returnToken = "returnToken";
        final ReturnRequest request = buildReturnRequest(depotCode, USERNAME, returnToken, PROCESSING);
        final ReturnPackageRequest returnPackage = ReturnPackageRequest.builder()
                .depotCode(depotCode)
                .supplierCode("abc")
                .returnToken(returnToken)
                .returnStatus(COMPLETED)
                .parcel(createParcel())
                .reason(RECIPIENT_NOT_AVAILABLE)
                .username(USERNAME)
                .build();

        final ProcessReturn processReturn = new ProcessReturn(1L, 1L, "COMPLETED");

        doReturn(processReturn)
                .when(returnRepository)
                .update(returnPackage);
        // when
        final ReturnResponse response = returnPort.process(request);
        // then
        assertThat(response.processReturn())
                .flatExtracting(ProcessReturn::processStatus)
                .contains("COMPLETED");
    }

    @Test
    void shouldNotContinueProcessWhenReceivedCompletedReturn() {
        // given
        final String depotCode = "KT1";
        final ReturnRequest request = buildReturnRequest(depotCode, USERNAME, "returnToken", COMPLETED);
        // when
        returnPort.process(request);
        // then nothing changed in request
        assertThat(request.getRequests())
                .flatExtracting(ReturnPackageRequest::getReturnStatus)
                .contains(COMPLETED);
    }

    @Test
    void shouldCollectDataWithParcelsThatHaveNoReturnToken() {
        // given
        final ReturnRequest request = buildReturnRequest("depotCode", USERNAME, null, CREATED);
        // when
        final ReturnResponse response = returnPort.process(request);
        // then
		assertThat(response.processReturn())
				.extracting(ProcessReturn::shipmentId, ProcessReturn::returnId, ProcessReturn::processStatus)
				.containsExactly(tuple(1L, null, "Return token not available"));
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        // given
        final ReturnRequest request = buildReturnRequest("KT1", null, "returnToken", CANCELLED);
        // when
        final Executable executable = () -> returnPort.process(request);
        // then
        final UsernameMissingException exception = assertThrows(UsernameMissingException.class, executable);
        assertEquals("Username is missing", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDepotCodeIsNull() {
        // given
        final ReturnRequest request = buildReturnRequest(null, USERNAME, "returnToken", CANCELLED);
        // when
        final Executable executable = () -> returnPort.process(request);
        // then
        final DepotCodeMissingException exception = assertThrows(DepotCodeMissingException.class, executable);
        assertEquals("Depot code is missing", exception.getMessage());
    }

	private ReturnRequest buildReturnRequest(String depotCode, String username, String returnToken,
			ReturnStatus returnStatus) {
        return ReturnRequest.builder()
                .requests(createReturnPackageRequest(returnStatus, returnToken))
                .depotCode(depotCode)
                .username(username)
                .build();
    }

	private List<ReturnPackageRequest> createReturnPackageRequest(ReturnStatus returnStatus, String returnToken) {
        return List.of(
                ReturnPackageRequest.builder()
                .returnToken(returnToken)
                .returnStatus(returnStatus)
                .parcel(createParcel())
                .reason(RECIPIENT_NOT_AVAILABLE)
                .supplierCode("abc")
                .build());
    }

    private Parcel createParcel() {
        return Parcel.builder()
                .destination("KT1")
                .id(1L)
                .parcelRelatedId(null)
                .parcelSize(Size.TEST)
                .parcelType(ParcelType.PARENT)
                .status(Status.RETURN)
                .build();
    }
}
