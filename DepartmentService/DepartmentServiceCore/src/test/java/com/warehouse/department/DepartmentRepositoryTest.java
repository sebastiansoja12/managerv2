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
import com.warehouse.department.domain.vo.DepotCode;
import com.warehouse.department.infrastructure.adapter.secondary.DepotReadRepository;
import com.warehouse.department.infrastructure.adapter.secondary.DepotRepositoryImpl;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepotEntity;
import com.warehouse.department.infrastructure.adapter.secondary.exception.DepotNotFoundException;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepotMapper;

@ExtendWith(MockitoExtension.class)
public class DepartmentRepositoryTest {

    @Mock
    private DepotReadRepository repository;

    @Mock
    private DepotMapper mapper;

    @InjectMocks
    private DepotRepositoryImpl depotRepository;

    public static final String KT1 = "KT1";

    @Test
    public void shouldViewByDepotCode() {
        final Department department = new Department();
        department.setDepotCode(KT1);
        final DepotCode code = new DepotCode(KT1);

        final DepotEntity entity = new DepotEntity();
        entity.setDepotCode(KT1);

        when(repository.findByDepotCode(KT1)).thenReturn(Optional.of(entity));
        when(mapper.map(entity)).thenReturn(department);

        // when
        final Department result = depotRepository.findByCode(code);

        // then
        assertNotNull(result);
        Assertions.assertEquals(result.getDepotCode(), entity.getDepotCode());
    }


    @Test
    public void shouldThrowDepotNotFoundExceptionWhenNotFoundByDepotCode() {
        // given
        final String exceptionMessage = "Depot was not found";
        final DepotCode code = new DepotCode(KT1);

        when(repository.findByDepotCode(KT1)).thenReturn(Optional.empty());

        // when
        final Executable executable = () -> depotRepository.findByCode(code);

        // then
        final DepotNotFoundException exception = Assertions.assertThrows(DepotNotFoundException.class, executable);
        Assertions.assertEquals(exceptionMessage, exception.getMessage());
    }
}