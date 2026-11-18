package com.warehouse.returning.application.service;

import com.warehouse.returning.application.port.secondary.ReturnReadModelRepository;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;
import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;
import com.warehouse.returning.domain.vo.ReturnPage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReturnReadModelServiceTest {
    private final ReturnRepository returnRepository = mock(ReturnRepository.class);
    private final ReturnReadModelRepository readModelRepository = mock(ReturnReadModelRepository.class);
    private final ReturnReadModelService service = new ReturnReadModelService(returnRepository, readModelRepository);

    @Test
    void shouldSyncReadModelFromCurrentReturnState() {
        final ReturnPackage returning = ReturnProcessingFixture.returning(ReturnStatus.PROCESSING);
        when(returnRepository.findDetailsById(returning.getReturnPackageId())).thenReturn(returning);

        service.syncReadModel(returning.getReturnPackageId());

        final ArgumentCaptor<ReturnPackageSnapshot> snapshot = ArgumentCaptor.forClass(ReturnPackageSnapshot.class);
        verify(readModelRepository).sync(snapshot.capture());
        assertEquals(returning.getReturnPackageId(), snapshot.getValue().returnPackageId());
        assertEquals(ReturnStatus.PROCESSING, snapshot.getValue().returnStatus());
    }

    @Test
    void shouldReadReturnsFromReadModel() {
        final DepartmentId departmentId = new DepartmentId(3L);
        final OperatorId operatorId = new OperatorId(7L);
        final ReturnPage page = new ReturnPage(
                List.of(ReturnProcessingFixture.returning(ReturnStatus.CREATED)), 0, 50, 1, 1);
        when(readModelRepository.findByDepartmentIdAndOperatorId(departmentId, operatorId, 0, 50)).thenReturn(page);

        final ReturnPage result = service.getReturns(departmentId, operatorId, 0, 50);

        assertSame(page, result);
        verifyNoInteractions(returnRepository);
    }
}
