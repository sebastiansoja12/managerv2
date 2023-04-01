package com.warehouse.depot;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.model.DepotCode;
import com.warehouse.depot.domain.model.DepotId;
import com.warehouse.depot.infrastructure.secondary.DepotReadRepository;
import com.warehouse.depot.infrastructure.secondary.DepotRepositoryImpl;
import com.warehouse.depot.infrastructure.secondary.entity.DepotEntity;
import com.warehouse.depot.infrastructure.secondary.exception.DepotNotFoundException;
import com.warehouse.depot.infrastructure.secondary.mapper.DepotMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepotRepositoryTest {

    @Mock
    private DepotReadRepository repository;

    @Mock
    private DepotMapper mapper;

    @InjectMocks
    private DepotRepositoryImpl depotRepository;

    public static final String KT1 = "KT1";

    @Test
    public void shouldSaveEntity() {
        // Given
        final Depot depot = Depot.builder()
                .depotCode("KT1")
                .city("Gliwice")
                .street("Mrągowska 11")
                .country("Poland")
                .build();

        final DepotEntity entity = new DepotEntity();

        when(mapper.map(depot)).thenReturn(entity);

        //when
        depotRepository.save(depot);

        //then
        verify(repository).save(entity);
    }

    @Test
    public void shouldViewByDepotCode() {
        final Depot depot = Depot.builder()
                .depotCode(KT1)
                .city("Gliwice")
                .street("Mrągowska 11")
                .country("Poland")
                .build();
        final DepotCode code = new DepotCode(KT1);

        final DepotEntity entity = new DepotEntity();
        entity.setDepotCode(KT1);

        when(repository.findByDepotCode(KT1)).thenReturn(Optional.of(entity));
        when(mapper.map(entity)).thenReturn(depot);

        // when
        final Depot result = depotRepository.viewByCode(code);

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
        final Executable executable = () -> depotRepository.viewByCode(code);

        // then
        final DepotNotFoundException exception = Assertions.assertThrows(DepotNotFoundException.class, executable);
        Assertions.assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void shouldViewById() {
        final Depot depot = Depot.builder()
                .depotCode(KT1)
                .city("Gliwice")
                .street("Mrągowska 11")
                .country("Poland")
                .build();

        final DepotId id = new DepotId(1L);
        final DepotEntity entity = new DepotEntity();
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.map(entity)).thenReturn(depot);

        // when
        final Depot result = depotRepository.viewById(id);

        // then
        assertNotNull(result);
        Assertions.assertEquals(depot.getDepotCode(), result.getDepotCode());
    }

    @Test
    public void shouldThrowDepotNotFoundExceptionWhenNotFoundById() {
        // given
        final String exceptionMessage = "Depot was not found";
        final DepotId id = new DepotId(1L);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when
        final Executable executable = () -> depotRepository.viewById(id);

        // then
        final DepotNotFoundException exception = Assertions.assertThrows(DepotNotFoundException.class, executable);
        Assertions.assertEquals(exceptionMessage, exception.getMessage());

    }
}