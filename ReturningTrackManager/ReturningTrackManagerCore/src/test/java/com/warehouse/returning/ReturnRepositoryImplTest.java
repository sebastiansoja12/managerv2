package com.warehouse.returning;

import static com.warehouse.returning.domain.model.ReturnStatus.*;
import static com.warehouse.returning.infrastructure.adapter.secondary.enumeration.ReturnStatus.CANCELLED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

import java.util.Optional;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.returning.domain.model.Parcel;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturnInformationReadRepository;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturnReadRepository;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturningRepositoryImpl;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.exception.ReturnEntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ReturnRepositoryImplTest {

    @Mock
    private ReturnReadRepository repository;

    @Mock
    private ReturnInformationReadRepository returnInformationReadRepository;

    private ReturningRepositoryImpl returningRepository;

    private final String exceptionMessage = "Return Entity for parcel %s was not found";
    private final String returnEntityExceptionMessage = "Return Entity with id [%s] was not found";

    @BeforeEach
    void setup() {
        returningRepository = new ReturningRepositoryImpl(repository);
    }

    @Test
    void shouldSaveProcessing() {
        // given
        final ReturnPackageRequest returnPackage = ReturnPackageRequest.builder()
                .username("s-soja")
                .returnStatus(PROCESSING)
                .reason("Unavailable")
                .returnToken("returnToken")
                .parcel(createParcel(1L))
                .supplierCode("abc")
                .build();
        // when
        final ProcessReturn processReturn = returningRepository.save(returnPackage);
        // then
		assertThat(processReturn)
                .extracting(ProcessReturn::processStatus)
				.containsExactly("PROCESSING");
    }

    @Test
    void shouldSaveCreated() {
        // given
        final ReturnPackageRequest returnPackage = ReturnPackageRequest.builder()
                .username("s-soja")
                .returnStatus(CREATED)
                .reason("Unavailable")
                .returnToken("returnToken")
                .parcel(createParcel(1L))
                .supplierCode("abc")
                .build();
        // when
        final ProcessReturn processReturn = returningRepository.save(returnPackage);
        // then
        assertThat(processReturn)
                .extracting(ProcessReturn::processStatus)
                .containsExactly("CREATED");
    }

    @Test
    void shouldUnlock() {
        // given
        final ReturnEntity returnEntity = new ReturnEntity();
        returnEntity.setReturnToken("returnToken");
        returnEntity.setReturnStatus(CANCELLED);
        returnEntity.setUsername("s-soja");
        returnEntity.setSupplierCode("abc");
        returnEntity.setParcelId(1L);

        doReturn(Optional.of(returnEntity))
                .when(repository)
                .findFirstByParcelIdAndReturnToken(1L, "returnToken");
        // when
        final ReturnStatus returnStatus = returningRepository.unlockReturn(1L, "returnToken");
        // then
        assertThat(returnStatus).isEqualTo(PROCESSING);
    }

    @Test
    void shouldUpdate() {
        // given
        final Long parcelId = 1L;
        final ReturnPackageRequest returnPackage = ReturnPackageRequest.builder()
                .username("s-soja")
                .returnStatus(COMPLETED)
                .reason("Unavailable")
                .returnToken("returnToken")
                .parcel(createParcel(1L))
                .supplierCode("abc")
                .build();
        final ReturnEntity returnEntity = createReturnEntity();
        doReturn(Optional.of(returnEntity))
                .when(repository)
                .findFirstByParcelId(parcelId);
        // when
        final ProcessReturn processReturn = returningRepository.update(returnPackage);
        // then
        assertThat(processReturn)
                .extracting(ProcessReturn::processStatus)
                .containsExactly("COMPLETED");
    }

    @Test
    void shouldThrowReturnEntityNotFoundByParcelIdException() {
        // given
        final Long parcelId = 1L;
        final ReturnPackageRequest returnPackage = ReturnPackageRequest.builder()
                .username("s-soja")
                .returnStatus(COMPLETED)
                .reason("Unavailable")
                .returnToken("returnToken")
                .parcel(createParcel(1L))
                .supplierCode("abc")
                .build();
        // when
        final ThrowableAssert.ThrowingCallable executable = () -> returningRepository.update(returnPackage);
        // then
        assertThatThrownBy(executable)
                .isInstanceOf(ReturnEntityNotFoundException.class)
                .hasMessageContaining(String.format(exceptionMessage, parcelId));
    }

    @Test
    void shouldThrowReturnEntityNotFoundByParcelIdExceptionWhenParcelIsEmpty() {
        // given
        final ReturnPackageRequest returnPackage = ReturnPackageRequest.builder()
                .username("s-soja")
                .returnStatus(COMPLETED)
                .reason("Unavailable")
                .returnToken("returnToken")
                .supplierCode("abc")
                .build();
        // when
        final ThrowableAssert.ThrowingCallable executable = () -> returningRepository.update(returnPackage);
        // then
        assertThatThrownBy(executable)
                .isInstanceOf(ReturnEntityNotFoundException.class)
                .hasMessageContaining(String.format(exceptionMessage, (Object) null));
    }

    @Test
    void shouldThrowReturnEntityNotFoundException() {
        // given
        final ReturnId returnId = new ReturnId(1L);
        // when
        final ThrowableAssert.ThrowingCallable executable = () -> returningRepository.get(returnId);
        // then
        assertThatThrownBy(executable)
                .isInstanceOf(ReturnEntityNotFoundException.class)
                .hasMessageContaining(String.format(returnEntityExceptionMessage, returnId.getValue()));
    }

    private Parcel createParcel(Long id) {
        return Parcel.builder().id(id).build();
    }

    private ReturnEntity createReturnEntity() {
        final ReturnEntity returnEntity = new ReturnEntity();
        returnEntity.setId(1L);
        returnEntity.setReturnToken("returnToken");
        returnEntity.setReturnStatus(
                com.warehouse.returning.infrastructure.adapter.secondary.enumeration.ReturnStatus.PROCESSING);
        returnEntity.setUsername("s-soja");
        returnEntity.setSupplierCode("abc");
        returnEntity.setParcelId(1L);
        return returnEntity;
    }
}
