package com.warehouse.returning;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturnReadRepository;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturningRepositoryImpl;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnEntity;

@ExtendWith(MockitoExtension.class)
public class ReturnRepositoryImplTest {

    @Mock
    private ReturnReadRepository repository;

    private ReturningRepositoryImpl returningRepository;

    @BeforeEach
    void setup() {
        returningRepository = new ReturningRepositoryImpl(repository);
    }

    @Test
    void shouldSave() {
        // given
        final ReturnPackage returnPackage = ReturnPackage.builder()
                .username("s-soja")
                .returnStatus(ReturnStatus.PROCESSING)
                .reason("Unavailable")
                .returnToken("returnToken")
                .parcelId(1L)
                .supplierCode("abc")
                .build();
        // when
        final ProcessReturn processReturn = returningRepository.save(returnPackage);
        // then
		assertThat(processReturn)
                .extracting(
                        ProcessReturn::processStatus)
				.containsExactly("PROCESSING");
    }

    private ReturnEntity createReturnEntity() {
        final ReturnEntity returnEntity = new ReturnEntity();
        returnEntity.setReturnToken("returnToken");
        returnEntity.setReturnStatus(
                com.warehouse.returning.infrastructure.adapter.secondary.enumeration.ReturnStatus.PROCESSING);
        returnEntity.setUsername("s-soja");
        returnEntity.setSupplierCode("abc");
        returnEntity.setParcelId(1L);
        return returnEntity;
    }

    private ReturnEntity savedReturnEntity() {
        final ReturnEntity returnEntity = new ReturnEntity();
        returnEntity.setReturnToken("returnToken");
        returnEntity.setReturnStatus(
                com.warehouse.returning.infrastructure.adapter.secondary.enumeration.ReturnStatus.PROCESSING);
        returnEntity.setUsername("s-soja");
        returnEntity.setSupplierCode("abc");
        returnEntity.setParcelId(1L);
        returnEntity.setId(1L);
        return returnEntity;
    }
}
