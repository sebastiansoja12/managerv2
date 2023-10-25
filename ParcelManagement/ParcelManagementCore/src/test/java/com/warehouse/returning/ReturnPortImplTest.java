package com.warehouse.returning;


import com.warehouse.returning.domain.exception.ReturnTokenMissingException;
import com.warehouse.returning.domain.model.*;
import com.warehouse.returning.domain.port.primary.ReturnPortImpl;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.service.ReturnService;
import com.warehouse.returning.domain.service.ReturnServiceImpl;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.domain.vo.ReturnResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.warehouse.returning.domain.model.ReturnStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ReturnPortImplTest {

    @Mock
    private ReturnRepository returnRepository;

    private ReturnPortImpl returnPort;

    private static final String RECIPIENT_NOT_AVAILABLE = "Recipient not available";

    private static final String USERNAME = "s-soja";



    @BeforeEach
    void setup() {
        final ReturnService returnService = new ReturnServiceImpl(returnRepository);
        returnPort = new ReturnPortImpl(returnService);
    }

    @Test
    void shouldProcessTheReturn() {
        // given
        final String depotCode = "KT1";
		final ReturnRequest request = buildReturnRequest(depotCode, USERNAME, "returnToken", CREATED);

        final ReturnPackage returnPackage = ReturnPackage.builder()
                .depotCode(depotCode)
                .supplierCode("abc")
                .returnToken("returnToken")
                .returnStatus(ReturnStatus.PROCESSING)
                .parcelId(1L)
                .reason(RECIPIENT_NOT_AVAILABLE)
                .username(USERNAME)
                .build();

        final ProcessReturn processReturn = new ProcessReturn(1L, "PROCESSING");

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
                .contains(CREATED);
    }

    @Test
    void shouldChangeReturnStatusToCreatedForCancelledParcels() {
        // given
        final String depotCode = "KT1";
        final ReturnRequest request = buildReturnRequest(depotCode, USERNAME, "returnToken", CANCELLED);

        final ReturnPackage returnPackage = ReturnPackage.builder()
                .depotCode(depotCode)
                .supplierCode("abc")
                .returnToken("returnToken")
                .returnStatus(PROCESSING)
                .parcelId(1L)
                .reason(RECIPIENT_NOT_AVAILABLE)
                .username(USERNAME)
                .build();

        final ProcessReturn processReturn = new ProcessReturn(1L, "PROCESSING");

        doReturn(processReturn)
                .when(returnRepository)
                .save(returnPackage);

        // when
        final ReturnResponse response = returnPort.process(request);
        // then
        assertEquals(List.of(processReturn), response.processReturn());
        // and status of request was changed to CREATED
        assertThat(request.getRequests())
                .flatExtracting(ReturnPackageRequest::getReturnStatus)
                .contains(CREATED);
    }

    @Test
    void shouldThrowExceptionWhenReturnTokenIsNotAvailable() {
        // given
        final ReturnRequest request = buildReturnRequest("depotCode", USERNAME, null, CREATED);
        // when
        final Executable executable = () -> returnPort.process(request);
        // then
        final ReturnTokenMissingException exception = assertThrows(ReturnTokenMissingException.class, executable);
        assertEquals("Return token is missing", exception.getMessage());
        assertEquals(8080, exception.getCode());
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        // given
        final ReturnRequest request = buildReturnRequest("KT1", null, "returnToken", CANCELLED);
        // when
        final Executable executable = () -> returnPort.process(request);
        // then
        final RuntimeException exception = assertThrows(RuntimeException.class, executable);
        assertEquals("Username is missing", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDepotCodeIsNull() {
        // given
        final ReturnRequest request = buildReturnRequest(null, USERNAME, "returnToken", CANCELLED);
        // when
        final Executable executable = () -> returnPort.process(request);
        // then
        final RuntimeException exception = assertThrows(RuntimeException.class, executable);
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
