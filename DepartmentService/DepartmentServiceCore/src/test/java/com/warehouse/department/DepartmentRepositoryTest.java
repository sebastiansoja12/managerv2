package com.warehouse.department;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.infrastructure.adapter.secondary.DepartmentRepositoryImpl;
import com.warehouse.department.infrastructure.adapter.secondary.DepartmentReadRepository;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
import com.warehouse.department.infrastructure.adapter.secondary.exception.DepotNotFoundException;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepotMapper;

@ExtendWith(MockitoExtension.class)
public class DepartmentRepositoryTest {

    @Mock
    private DepartmentReadRepository repository;

    @Mock
    private DepotMapper mapper;

    @InjectMocks
    private DepartmentRepositoryImpl depotRepository;

    public static final String KT1 = "KT1";

    @Test
    public void shouldViewByDepotCode() {
        final Department department = new Department();
        department.setDepotCode(KT1);
        final DepartmentCode code = new DepartmentCode(KT1);

        final DepartmentEntity entity = new DepartmentEntity();
        entity.setDepartmentCode(KT1);

        when(repository.findByDepartmentCode(KT1)).thenReturn(Optional.of(entity));
        when(mapper.map(entity)).thenReturn(department);

        // when
        final Department result = depotRepository.findByCode(code);

        // then
        assertNotNull(result);
        Assertions.assertEquals(result.getDepotCode(), entity.getDepartmentCode());
    }


    @Test
    public void shouldThrowDepotNotFoundExceptionWhenNotFoundByDepotCode() {
        // given
        final String exceptionMessage = "Department was not found";
        final DepartmentCode code = new DepartmentCode(KT1);

        when(repository.findByDepartmentCode(KT1)).thenReturn(Optional.empty());

        // when
        final Executable executable = () -> depotRepository.findByCode(code);

        // then
        final DepotNotFoundException exception = Assertions.assertThrows(DepotNotFoundException.class, executable);
        Assertions.assertEquals(exceptionMessage, exception.getMessage());
    }
}